package com.mgm.stocksorting.service.sorting;

import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.mgm.stocksorting.domain.ProductDomain;
import com.mgm.stocksorting.domain.SortingCriteriaDomain;

/**
 * Default implementation of the product sorting algorithm using weighted sales and stock.
 *
 * @author Miguel Maquieira
 */

@Component
public class DefaultProductSortingAlgorithm implements ProductSortingAlgorithm
{
    @Override
    public List<ProductDomain> sort( final List<ProductDomain> products, final SortingCriteriaDomain query )
    {
        if ( products == null || products.isEmpty() )
        {
            return List.of();
        }

        var salesRule = new ScoringRuleSales();
        var stockRule = new ScoringRuleStock( query.getStockSizeWeights() , query.isComputeStockSize() );

        double normalizedSalesValue = salesRule.computeNormalizedValue( products );
        double normalizedStockValue = stockRule.computeNormalizedValue( products );

        record ProductScore( ProductDomain product, double score )
        {
        }
        ;

        var scored = products.stream()
            .map( p -> new ProductScore( p,
                salesRule.computeScore( p, normalizedSalesValue, query.getSalesWeight() ) +
                stockRule.computeScore( p, normalizedStockValue, query.getStockWeight() ) ) )
            .sorted( Comparator.comparingDouble( ProductScore::score ) )
            .toList();

        var sorted = scored.stream().map(ProductScore::product).toList();

        return query.isAscending() ? sorted : sorted.reversed();
    }
}
