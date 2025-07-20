package com.mgm.stocksorting.service.sorting;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.mgm.stocksorting.domain.ProductDomain;

/**
 * Default implementation of {@link SortingStrategy} that orders products
 * based on their computed scores.
 *
 * <p>This strategy performs a simple sort over the input score map:
 * products are ordered in either ascending or descending order of their
 * associated score values. No tie-breaking logic is applied — the natural
 * iteration order of equal scores is preserved.</p>
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
