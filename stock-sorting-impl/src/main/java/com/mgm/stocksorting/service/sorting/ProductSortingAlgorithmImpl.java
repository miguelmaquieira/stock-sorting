package com.mgm.stocksorting.service.sorting;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.mgm.stocksorting.domain.ProductDomain;

import lombok.RequiredArgsConstructor;

/**
 * Default implementation of the product sorting algorithm using weighted sales and stock.
 *
 * @author Miguel Maquieira
 */
@Component
@RequiredArgsConstructor
public class ProductSortingAlgorithmImpl implements ProductSortingAlgorithm
{
    private final SortingStrategy sortingStrategy;

    @Override
    public List<ProductDomain> sort( final List<ProductDomain> products, final Set<ScoringRule> rules,
        final boolean ascending )
    {
        if ( products == null || products.isEmpty() )
        {
            return List.of();
        }

        ScoringRuleValidator.validate( rules );

        var scoreCalculator = new ProductScoreCalculator( rules );
        var scores = scoreCalculator.calculateScores( products );
        return sortingStrategy.sort( scores, ascending );
    }
}
