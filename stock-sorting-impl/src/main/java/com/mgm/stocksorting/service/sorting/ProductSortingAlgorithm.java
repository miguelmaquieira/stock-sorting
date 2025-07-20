package com.mgm.stocksorting.service.sorting;

import java.util.List;
import java.util.Set;

import com.mgm.stocksorting.domain.ProductDomain;

/**
 * Defines a contract for sorting products using a given query.
 *
 * @author Miguel Maquieira
 */
public interface ProductSortingAlgorithm
{
    /**
     * Sorts a list of products using the provided query.
     *
     * @param products the products to sort
     * @param rules list of rules to be used within the sorting algo
     * @param ascending whether the products should be sorting in ascending order
     * @return sorted list
     */
    List<ProductDomain> sort( List<ProductDomain> products, Set<ScoringRule> rules, boolean ascending );
}
