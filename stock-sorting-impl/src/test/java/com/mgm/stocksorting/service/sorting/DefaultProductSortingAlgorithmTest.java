package com.mgm.stocksorting.service.sorting;

import java.io.InputStream;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgm.stocksorting.domain.ProductDomain;
import com.mgm.stocksorting.domain.SortingCriteriaDomain;

import lombok.Data;
import lombok.NoArgsConstructor;

// CSOFF: Javadoc
@TestMethodOrder( MethodOrderer.MethodName.class )
class DefaultProductSortingAlgorithmTest
{
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final ProductSortingAlgorithm cut = new DefaultProductSortingAlgorithm();

    @ParameterizedTest( name = "{index} => {0}" )
    @MethodSource( "testCasesProvider" )
    void shouldSortProductsPerTestCase( final DefaultProductSortingAlgorithmTest.TestCase testCase )
    {
        var result = cut.sort( testCase.input, testCase.criteria );
        var resultIds = result.stream().map( ProductDomain::id ).toList();

        assertEquals( testCase.expected, resultIds, "Failed test: " + testCase.name );
    }

    static List<TestCase> testCasesProvider() throws Exception
    {
        try ( InputStream input = DefaultProductSortingAlgorithmTest.class.getResourceAsStream(
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
        var result = cut.sort( List.of(), null );
        assertNotNull( result );
        assertTrue( result.isEmpty() );
    }

    @Test
    void sortWhenProductListIsNullShouldReturnAnEmptyList()
    {
        var result = cut.sort( null, null );
        assertNotNull( result );
        assertTrue( result.isEmpty() );
    }

    @Data
    @NoArgsConstructor
    private static class TestCase
    {
        private String name;
        private List<ProductDomain> input;
        private SortingCriteriaDomain criteria;
        private List<Long> expected;
    }
}