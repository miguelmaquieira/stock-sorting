package com.mgm.stocksorting.service.sorting;

import java.util.List;

import com.mgm.stocksorting.domain.ProductDomain;

/**
 * Defines a rule for scoring a product based on a specific criterion.
 *
 * @author Miguel Maquieira
 */
public interface ScoringRule
{
    /**
     * Computes the normalized, weighted score for a product.
     *
     * @param product the product to score
     * @param normalizationValue the normalization value of this criterion across all products
     * @param weight the weight to apply to this criteria
     * @return the computed score
     */
    double computeScore( ProductDomain product, double normalizationValue, double weight );

    /**
     * Finds the normalized value of this criterion for a given list of products.
     *
     * @param products the list of products
     * @return the maximum value found
     */
    double computeNormalizedValue( List<ProductDomain> products );
}
