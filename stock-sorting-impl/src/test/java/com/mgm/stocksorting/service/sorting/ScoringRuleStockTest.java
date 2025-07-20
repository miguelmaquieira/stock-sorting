package com.mgm.stocksorting.service.sorting;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import static org.junit.jupiter.api.Assertions.*;

import com.mgm.stocksorting.domain.ProductDomain;
import com.mgm.stocksorting.domain.StockSizeDomain;

// CSOFF: Javadoc
@TestMethodOrder( MethodOrderer.MethodName.class )
class ScoringRuleStockTest
{
    private static final ProductDomain PRODUCT_1 = new ProductDomain( 1L, "A", 100,
        Map.of( StockSizeDomain.S.name(), 20, StockSizeDomain.M.name(), 10, StockSizeDomain.L.name(), 0 ) );
    private static final ProductDomain PRODUCT_2 = new ProductDomain( 2L, "B", 150,
        Map.of( StockSizeDomain.S.name(), 10, StockSizeDomain.M.name(), 2, StockSizeDomain.L.name(), 30 ) );
    private static final Map<String, Double> STOCK_SIZE_WEIGHTS = Map.of(
        StockSizeDomain.S.name(), 0.2, StockSizeDomain.M.name(), 0.5, StockSizeDomain.L.name(), 0.3 );
    private static final List<ProductDomain> PRODUCTS = List.of( PRODUCT_1, PRODUCT_2 );
    private static final double NORMALIZED_VALUE_WITHOUT_STOCK_SIZE = StockSizeDomain.values().length;
    private static final double NORMALIZED_VALUE_DELTA = 0.0001;

    @Test
    void computeNormalizedValueWhenProductListEmptyShouldReturnZero()
    {
        var cut = new ScoringRuleStock( 0.5, null, false );
        double result = cut.computeNormalizedValue( List.of() );

        assertEquals( 0.0, result );
    }

    @Test
    void computeNormalizedValueWhenProductListIsNullShouldReturnZero()
    {
        var cut = new ScoringRuleStock( 0.5, null, false );
        double result = cut.computeNormalizedValue( null );

        assertEquals( 0.0, result );
    }

    @Test
    void computeNormalizedValueWhenProductListSetAndComputeStockSizeFalseShouldReturnValue()
    {
        var cut = new ScoringRuleStock( 0.5, null, false );
        double result = cut.computeNormalizedValue( PRODUCTS );

        assertEquals( NORMALIZED_VALUE_WITHOUT_STOCK_SIZE, result, NORMALIZED_VALUE_DELTA );
    }

    @Test
    void computeNormalizedValueWhenSingleProductAndComputeStockSizeTrueUsingDefaultValuesShouldReturnValue()
    {
        var cut = new ScoringRuleStock( 0.5, null, true );
        // coverageRatio = 2/3 weightedStock = 20 * 1/3 + 10 * 1/3 + 0 * 1/3 = 10;
        // Given that we only have PRODUCT_1 the max value will PRODUCT_1 normalized value: 2/3 * 10
        var expectedNormalizedValue = ( double ) 2 / 3 * 10;

        double result = cut.computeNormalizedValue( List.of( PRODUCT_1 ) );

        assertEquals( expectedNormalizedValue, result, NORMALIZED_VALUE_DELTA );
    }

    @Test
    void computeNormalizedValueWhenProductListAndComputeStockSizeTrueUsingDefaultValuesShouldReturnValue()
    {
        var cut = new ScoringRuleStock( 0.5, null, true );
        // PRODUCT_1
        // coverageRatio = 2/3, weightedStock = 20 * 1/3 + 10 * 1/3 + 0 * 1/3 = 10;
        // normalizedValue: 2/3 * 10
        // PRODUCT_2
        // coverageRatio = 3/3, weightedStock = 10 * 1/3 + 2 * 1/3 + 30 * 1/3 = 14;
        // normalizedValue: 1 * 14
        // max normalized value of PRODUCT_1 and PRODUCT_2 -> max(10, 14) = 14
        var expectedNormalizedValue = 14.0;

        double result = cut.computeNormalizedValue( PRODUCTS );

        assertEquals( expectedNormalizedValue, result, NORMALIZED_VALUE_DELTA );
    }

    @Test
    void computeNormalizedValueWhenSingleProductAndComputeStockSizeTrueShouldReturnValue()
    {
        var cut = new ScoringRuleStock( 0.5, STOCK_SIZE_WEIGHTS, true );
        // coverageRatio = 2/3 weightedStock = 20 * 0.2 + 10 * 0.5 + 0 * 0.3 = 9;
        // Given that we only have PRODUCT_1 the max value will PRODUCT_1 normalized value: 2/3 * 9
        var expectedNormalizedValue = ( double ) 2 / 3 * 9;

        double result = cut.computeNormalizedValue( List.of( PRODUCT_1 ) );

        assertEquals( expectedNormalizedValue, result, NORMALIZED_VALUE_DELTA );
    }

    @Test
    void computeNormalizedValueWhenProductListAndComputeStockSizeTrueShouldReturnValue()
    {
        var cut = new ScoringRuleStock( 0.5, STOCK_SIZE_WEIGHTS, true );
        // PRODUCT_1
        // coverageRatio = 2/3 weightedStock = 20 * 0.2 + 10 * 0.5 + 0 * 0.3 = 9;
        // Given that we only have PRODUCT_1 the max value will PRODUCT_1 normalized value: 2/3 * 9
        // PRODUCT_2
        // coverageRatio = 3/3, weightedStock = 10 * 0.2 + 2 * 0.5 + 30 * 0.3 = 12;
        // normalizedValue: 1 * 12
        // max normalized value of PRODUCT_1 and PRODUCT_2 -> max(9, 12) = 12
        var expectedNormalizedValue = 12.0;

        double result = cut.computeNormalizedValue( PRODUCTS );

        assertEquals( expectedNormalizedValue, result, NORMALIZED_VALUE_DELTA );
    }

    @Test
    void computeNormalizedValueWhenUnknownStockSizeKeyShouldIgnoreIt()
    {
        var product = new ProductDomain( 3L, "C", 100,
            Map.of( "XL", 10, StockSizeDomain.S.name(), 10 ) ); // "XL" not defined in weights
        var cut = new ScoringRuleStock( 0.5, STOCK_SIZE_WEIGHTS, true );

        // coverageRatio = 2/3 weightedStock = 10 * 0.0 + 10 * 0.2 + 0 * 0.5 + 0 * 0.3 = 2;
        // Normalized value: 2/3 * 2 = 4/3
        var expected = (double ) 4/3;

        double result = cut.computeNormalizedValue(List.of(product));
        assertEquals(expected, result);
    }

    @Test
    void computeScoreWhenNormalizationValueIsZeroShouldReturnZero()
    {
        double weight = 0.5;
        var cut = new ScoringRuleStock( weight, STOCK_SIZE_WEIGHTS, true );
        var prod = new ProductDomain( 1L, "A", 300, Map.of() );

        double expected = 0.0;
        var normalizedValue = 0.0;

        double score = cut.computeScore( prod, normalizedValue );

        assertEquals( expected, score );
    }

    @Test
    void computeScoreWhenWeightIsNegativeShouldThrowException()
    {
        assertThrows( IllegalArgumentException.class, () -> new ScoringRuleStock( -1, null, true ) );
    }

    @Test
    void computeScoreWhenNormalizedValueIsNegativeShouldThrowException()
    {
        double weight = 0.5;
        var cut = new ScoringRuleStock( weight, null, true );
        var prod = new ProductDomain( 1L, "A", 0, Map.of() );
        assertThrows( IllegalArgumentException.class, () -> cut.computeScore( prod, -1 ) );
    }
}