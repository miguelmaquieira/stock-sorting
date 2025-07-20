package com.mgm.stocksorting.service.sorting;

import java.util.List;
import java.util.Map;

import com.mgm.stocksorting.domain.ProductDomain;
import com.mgm.stocksorting.domain.StockSizeDomain;

/**
 * Scoring rule that computes a product score based on weighted stock levels.
 * Uses size-based weighting for stock quantities.
 *
 * @author Miguel Maquieira
 */
public class ScoringRuleStock extends ScoringRule
{
    private static final double DEFAULT_NORMALIZED_VALUE = 0.0;
    private static final double NORMALIZED_VALUE_WITHOUT_STOCK_SIZE = StockSizeDomain.values().length;
    private static final double DEFAULT_STOCK_SIZE_WEIGHT = 1.0 / NORMALIZED_VALUE_WITHOUT_STOCK_SIZE;
    private final Map<String, Double> sizeWeights;
    private final boolean computeStockSize;

    public ScoringRuleStock( final double weight, final Map<String, Double> stockSizeMap,
        final boolean computeStockSize )
    {
        super( weight);
        this.sizeWeights = ( stockSizeMap != null && !stockSizeMap.isEmpty() ) ?
            stockSizeMap :
            Map.of(
                StockSizeDomain.S.name(), DEFAULT_STOCK_SIZE_WEIGHT,
                StockSizeDomain.M.name(), DEFAULT_STOCK_SIZE_WEIGHT,
                StockSizeDomain.L.name(), DEFAULT_STOCK_SIZE_WEIGHT );
        this.computeStockSize = computeStockSize;
    }

    @Override
    public double computeScore( final ProductDomain product, final double normalizationValue )
    {
        if ( normalizationValue < 0 )
        {
            throw new IllegalArgumentException( "Normalization value must be >= 0. Received: " + normalizationValue );
        }

        if ( normalizationValue == DEFAULT_NORMALIZED_VALUE )
        {
            return DEFAULT_NORMALIZED_VALUE;
        }
        double stockWeighted = computeWeightedStock( product );
        double normalized = stockWeighted / normalizationValue;
        return normalized * getWeight();
    }

    @Override
    public double computeNormalizedValue( final List<ProductDomain> products )
    {
        if ( products == null || products.isEmpty() )
        {
            return DEFAULT_NORMALIZED_VALUE;
        }

        return computeStockSize ?
            products.stream().mapToDouble( this::computeWeightedStock ).max().orElse( DEFAULT_NORMALIZED_VALUE ) :
            NORMALIZED_VALUE_WITHOUT_STOCK_SIZE;

    }

    private double computeWeightedStock( final ProductDomain product )
    {
        if ( product.stock() == null || product.stock().isEmpty() )
        {
            return DEFAULT_NORMALIZED_VALUE;
        }

        long sizesWithStock = product.stock().entrySet().stream().filter( e -> e.getValue() > 0 ).count();
        double coverageRatio = sizesWithStock / ( double ) StockSizeDomain.values().length;

        double weightedUnits = product.stock()
            .entrySet()
            .stream()
            .mapToDouble( e -> e.getValue() * sizeWeights.getOrDefault( e.getKey(), 0.0 ) )
            .sum();

        return coverageRatio * weightedUnits;
    }
}
