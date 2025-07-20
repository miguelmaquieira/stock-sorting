package com.mgm.stocksorting.service.sorting;

import java.util.Set;

/**
 * Utility class that performs sanity checks on a collection of {@link ScoringRule} instances
 * before they are used in score calculation or product sorting.
 *
 * @author Miguel Maquieira
 */
public class ScoringRuleValidator
{
    public static void validate( final Set<ScoringRule> rules )
    {
        if ( rules == null || rules.isEmpty() )
        {
            throw new IllegalArgumentException( "At least one rule must be provided" );
        }
        if ( rules.stream().anyMatch( r -> r.getWeight() < 0 ) )
        {
            throw new IllegalArgumentException( "Rule weights must be non-negative" );
        }
        var sum = rules.stream().mapToDouble( ScoringRule::getWeight ).sum();
        if ( sum != 1.0 )
        {
            throw new IllegalArgumentException( "Weights should add up 1.0" );
        }
    }
}
