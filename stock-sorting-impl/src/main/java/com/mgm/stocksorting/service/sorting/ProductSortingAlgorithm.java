package com.mgm.stocksorting.service.sorting;

import java.util.List;

import com.mgm.stocksorting.domain.ProductDomain;
import com.mgm.stocksorting.domain.SortingCriteriaDomain;

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
     * @param query    the sorting criteria
     * @return sorted list
     */
    List<ProductDomain> sort( List<ProductDomain> products, SortingCriteriaDomain query );
}
