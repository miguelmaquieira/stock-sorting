package com.mgm.stocksorting.service.sorting;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.mgm.stocksorting.domain.ProductDomain;

/**
 * Please add your description here.
 *
 * @author Miguel Maquieira
 */
@Component
public class SortingStrategyScore implements SortingStrategy
{
    @Override
    public List<ProductDomain> sort( final Map<ProductDomain, Double> scoredProducts, final boolean ascending )
    {
        Comparator<ProductDomain> comparator = Comparator.comparing( scoredProducts::get );
        if ( !ascending )
        {
            comparator = comparator.reversed();
        }
        return scoredProducts.keySet().stream().sorted( comparator ).toList();
    }
}
