package com.mgm.stocksorting.service.sorting;

import java.util.List;

import com.mgm.stocksorting.domain.ProductDomain;

import lombok.Getter;

/**
 * Defines a rule for scoring a product based on a specific criterion.
 *
 * @author Miguel Maquieira
 */
public abstract class ScoringRule
{

    protected ScoringRule( final double weight )
    {
        if ( weight < 0 )
        {
            throw new IllegalArgumentException( "`weight` must be greater than zero." );
        }
        this.weight = weight;
    }

    @Getter
    private final double weight;
    /**
     * Computes the normalized, weighted score for a product.
     *
     * @param product the product to score
     * @param normalizationValue the normalization value of this criterion across all products
     * @return the computed score
     */
    abstract double computeScore( ProductDomain product, double normalizationValue );

    /**
     * Finds the normalized value of this criterion for a given list of products.
     *
     * @param products the list of products
     * @return the maximum value found
     */
        abstract  double computeNormalizedValue( List<ProductDomain> products );
}
