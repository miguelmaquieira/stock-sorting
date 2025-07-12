package com.mgm.stocksorting.service.sorting;

import java.util.List;

import com.mgm.stocksorting.domain.ProductDomain;

/**
 * Scoring rule that computes the score based on product sales.
 *
 * @author Miguel Maquieira
 */
public class ScoringRuleSales implements ScoringRule
{
    private static final double DEFAULT_MAX_VALUE = 0.0;

    @Override
    public double computeScore( final ProductDomain product, final double normalizationValue, final double weight )
    {
        if ( normalizationValue < 0 )
        {
            throw new IllegalArgumentException( "'maxValue' must be greater than 0." );
        }

        if ( weight < 0 )
        {
            throw new IllegalArgumentException( "'weight' must be greater than 0." );
        }

        if ( normalizationValue == DEFAULT_MAX_VALUE )
        {
            return DEFAULT_MAX_VALUE;
        }
        double normalized = ( double ) product.sales() / normalizationValue;
        return normalized * weight;
    }

    @Override
    public double computeNormalizedValue( final List<ProductDomain> products )
    {
        return products == null ?
            DEFAULT_MAX_VALUE :
            products.stream().mapToDouble( ProductDomain::sales ).max().orElse( DEFAULT_MAX_VALUE );
    }
}
