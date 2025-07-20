package com.mgm.stocksorting.service.sorting;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        var sortingStrategy =  new SortingStrategyScore();
        cut = new ProductSortingAlgorithmImpl( sortingStrategy );
    }

    @ParameterizedTest( name = "{index} => {0}" )
    @MethodSource( "testCasesProvider" )
    void shouldSortProductsPerTestCase( final ProductSortingAlgorithmImplTest.TestCase testCase )
    {
        var rules = convertToScoringRules( testCase.criteria.rules );
        var result = cut.sort( testCase.input,rules,  testCase.criteria.ascending );
        var resultIds = result.stream().map( ProductDomain::id ).toList();

        assertEquals( testCase.expected, resultIds, "Failed test: " + testCase.name );
    }

    private Set<ScoringRule> convertToScoringRules( final Set<TestRawScoringRuleDTO> rawRules )
    {
        return rawRules.stream().map( raw -> switch ( raw.getType() )
        {
            case "sales" -> new ScoringRuleSales( raw.getWeight() );
            case "stock" -> {
                var stockRule = new ScoringRuleStock( raw.getWeight(), null, false );;
            }
            default -> throw new IllegalArgumentException( "Unknown rule type: " + raw.getType() );
        } ).collect( Collectors.toSet() );
    }

    static List<TestCase> testCasesProvider() throws Exception
    {
        try ( InputStream input = ProductSortingAlgorithmImplTest.class.getResourceAsStream(
            "/sorting/sorting_test_cases.json" ) )
        {
            return MAPPER.readValue( input, new TypeReference<>()
            {
            } );
        }
    }

    @Test
    void sortWhenProductListIsEmptyShouldReturnAnEmptyList()
    {
        var result = cut.sort( List.of(),Set.of(), false );
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
        private Set<TestRawScoringRuleDTO> rules;
        private boolean ascending;
    }

    @Data
    @NoArgsConstructor
    private static class TestRawScoringRuleDTO
    {
        private String type;
        private double weight;
    }


}