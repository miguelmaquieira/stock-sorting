package com.mgm.stocksorting.service.sorting;

import java.util.List;
import java.util.Map;

import com.mgm.stocksorting.domain.ProductDomain;

/**
 * Please add your description here.
 *
 * @author Miguel Maquieira
 */
public interface SortingStrategy
{
    List<ProductDomain> sort( Map<ProductDomain, Double> scoredProducts, boolean ascending );
}
