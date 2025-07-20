package com.mgm.stocksorting.service.sorting;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.mgm.stocksorting.domain.ProductDomain;

// CSOFF: Javadoc
@TestMethodOrder( MethodOrderer.MethodName.class )
class ScoringRuleSalesTest
{
    private static final List<ProductDomain> PRODUCTS = List.of(
        new ProductDomain( 1L, "A", 100, Map.of() ),
        new ProductDomain( 2L, "B", 200, Map.of() ),
        new ProductDomain( 3L, "C", 150, Map.of() )
    );
    private ScoringRuleSales cut;


    @BeforeEach
    void setUp()
    {
        cut = new ScoringRuleSales( 0.5 );
    }

    @Test
    void computeNormalizationSalesWhenProductListSetShouldReturnMaxValue()
    {
        double expectedMaxValue = 200.0;
        double max = cut.computeNormalizedValue( PRODUCTS );

        assertEquals( expectedMaxValue, max );
    }

    @Test
    void computeNormalizedValueWhenProductListEmptyShouldReturnZero()
    {
        double max = cut.computeNormalizedValue( List.of() );

        assertEquals( 0.0, max );
    }

    @Test
    void computeNormalizedValueWhenProductListNullShouldReturnZero()
    {
        double max = cut.computeNormalizedValue( null );

        assertEquals( 0.0, max );
    }

    @Test
    void computeNormalizationSalesWHenZeroSalesShouldReturnZero()
    {
        var products = List.of( new ProductDomain( 1L, "A", 0, Map.of() ), new ProductDomain( 2L, "B", 0, Map.of() ) );
        double max = cut.computeNormalizedValue( products );

        assertEquals( 0.0, max );
    }

    @Test
    void computeScoreWhenProductSetShouldReturnNormalizedWeightedScore()
    {
        var prod = new ProductDomain( 1L, "A", 300, Map.of() );
        var maxValue = 500.0;
        double expected = ( prod.sales() / maxValue ) * 0.5;

        double score = cut.computeScore( prod, maxValue );

        assertEquals( expected, score );
    }

    @Test
    void computeScoreWhenProductSalesZeroReturnNormalizedWeightedScore()
    {
        var prod = new ProductDomain( 1L, "A", 0, Map.of() );
        var maxValue = 0.0;
        double expected = 0.0;

        double score = cut.computeScore( prod, maxValue );

        assertEquals( expected, score );
    }

    @Test
    void computeScoreWhenWeightIsNegativeShouldThrowException()
    {
        var prod = new ProductDomain( 1L, "A", 0, Map.of() );
        assertThrows( IllegalArgumentException.class, () -> cut.computeScore( prod, -1 ) );
    }

    @Test
    void computeScoreWhenNormalizedValueIsNegativeShouldThrowException()
    {
        var cut = new ScoringRuleSales( 0.5 );
        var prod = new ProductDomain( 1L, "A", 0, Map.of() );
        assertThrows( IllegalArgumentException.class, () -> cut.computeScore( prod, -1 ) );
    }
}