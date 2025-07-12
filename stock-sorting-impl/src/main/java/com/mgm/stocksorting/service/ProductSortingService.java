package com.mgm.stocksorting.service;

import java.util.List;

import com.mgm.stocksorting.controller.model.Product;
import com.mgm.stocksorting.controller.model.ProductSortQuery;

/**
 * Service interface for sorting products based on weighted criteria.
 * Example use case: sort a list of T-shirt products where sales and stock
 * levels influence the final order shown to customers.
 *
 * @author Miguel Maquieira
 */
public interface ProductSortingService
{
    /**
     * Sorts products using the provided weighted criteria.
     *
     * @param sortingQuery the sorting criteria and weights
     * @return a list of products sorted by their computed score
     */
    List<Product> sortProducts( ProductSortQuery sortingQuery );
}
