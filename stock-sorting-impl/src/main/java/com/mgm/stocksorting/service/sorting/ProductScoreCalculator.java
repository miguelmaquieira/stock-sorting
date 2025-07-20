package com.mgm.stocksorting.service.sorting;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.mgm.stocksorting.domain.ProductDomain;

/**
 * Calculates the aggregate score for each product based on a set of weighted scoring rules.
 *  * <p>
 *  * This class encapsulates the logic for computing the total score for each {@link ProductDomain}
 *  * using all provided {@link ScoringRule}s. Each rule contributes a weighted score based on a
 *  * normalized value (e.g., max sales or average stock), and the scores are summed for final ranking.
 *  * <p>
 *  * This class is stateless and is typically created per sorting request to accommodate dynamic rule sets.
 *
 * @author Miguel Maquieira
 */
public class ProductScoreCalculator
{
    private final Set<ScoringRule> rules;

    public ProductScoreCalculator( final Set<ScoringRule> rules )
    {
        this.rules = rules;
    }

    public Map<ProductDomain, Double> calculateScores( final List<ProductDomain> products )
    {
        return products.stream().collect( Collectors.toMap(
            p -> p,
            p -> rules.stream().mapToDouble( r -> {
                double normalizedValue = r.computeNormalizedValue( products );
                return r.computeScore( p, normalizedValue );
            } ).sum()
        ) );
    }
}
