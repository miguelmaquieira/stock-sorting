package com.mgm.stocksorting.service.sorting;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgm.stocksorting.domain.ProductDomain;

import lombok.Data;
import lombok.NoArgsConstructor;

// CSOFF: Javadoc
@TestMethodOrder( MethodOrderer.MethodName.class )
class ProductSortingAlgorithmImplTest
{
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private ProductSortingAlgorithm cut;

    @BeforeEach
    void setUp()
    {
        cut = new ProductSortingAlgorithmImpl( new SortingStrategyScore() );
    }

    @ParameterizedTest( name = "{index} - {0}" )
    @MethodSource( "testCasesProvider" )
    void testSortingFromJson( final TestCase testCase )
    {
        // Arrange
        List<ProductDomain> products =
            testCase.input.stream().map( p -> new ProductDomain( p.id(), p.name(), p.sales(), p.stock() ) ).toList();

        Set<ScoringRule> rules = testCase.criteria.rules.stream().map( r -> switch ( r.type.toUpperCase() )
        {
            case "SALES" -> new ScoringRuleSales( r.weight );
            case "STOCK" -> new ScoringRuleStock( r.weight, r.sizeWeights != null ? r.sizeWeights : Map.of(),
                r.computeStockSize != null && r.computeStockSize );
            default -> throw new IllegalArgumentException( "Unsupported rule type: " + r.type );
        } ).collect( Collectors.toSet() );

        // Act
        List<ProductDomain> sorted = cut.sort( products, rules, testCase.criteria.ascending );

        // Assert
        var actualOrder = sorted.stream().map( ProductDomain::id ).toList();
        assertEquals( testCase.expected, actualOrder, testCase.name );
    }

    static Stream<TestCase> testCasesProvider() throws Exception
    {
        InputStream is =
            ProductSortingAlgorithmImplTest.class.getResourceAsStream( "/sorting/sorting_test_cases.json" );
        List<TestCase> cases = MAPPER.readValue( is, new TypeReference<>()
        {
        } );
        return cases.stream();
    }

    @Test
    void sortWhenProductListIsEmptyShouldReturnAnEmptyList()
    {
        var result = cut.sort( List.of(), Set.of(), false );
        assertNotNull( result );
        assertTrue( result.isEmpty() );
    }

    @Test
    void sortWhenProductListIsNullShouldReturnAnEmptyList()
    {
        var result = cut.sort( null, Set.of(), false );
        assertNotNull( result );
        assertTrue( result.isEmpty() );
    }

    @Test
    void sortWhenRuleListIsEmptyShouldThrowException()
    {
        var products = List.of( new ProductDomain( 1L, "A", 100, Map.of() ) );
        assertThrows( IllegalArgumentException.class, () -> cut.sort( products, Set.of(), false ) );
    }

    @Test
    void sortWhenRuleListListIsNullShouldThrowException()
    {
        var products = List.of( new ProductDomain( 1L, "A", 100, Map.of() ) );
        assertThrows( IllegalArgumentException.class, () -> cut.sort( products, null, false ) );
    }

    @Data
    @NoArgsConstructor
    private static class TestCase
    {
        private String name;
        private List<ProductDomain> input;
        private TestSortingCriteria criteria;
        private List<Long> expected;
    }

    @Data
    @NoArgsConstructor
    private static class TestSortingCriteria
    {
        private Set<TestScoringRule> rules;
        private boolean ascending;
    }

    @Data
    @NoArgsConstructor
    private static class TestScoringRule
    {
        public String type;
        public double weight;
        public Map<String, Double> sizeWeights;
        public Boolean computeStockSize;
    }
}