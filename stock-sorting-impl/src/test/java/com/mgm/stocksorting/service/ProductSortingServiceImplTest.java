package com.mgm.stocksorting.service;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.mgm.stocksorting.controller.model.Product;
import com.mgm.stocksorting.controller.model.ProductSortQuery;
import com.mgm.stocksorting.controller.model.ProductSortQueryWeights;
import com.mgm.stocksorting.controller.model.ProductStock;
import com.mgm.stocksorting.domain.ProductDomain;
import com.mgm.stocksorting.domain.SortingCriteriaDomain;
import com.mgm.stocksorting.entity.ProductEntity;
import com.mgm.stocksorting.mapper.ProductMapper;
import com.mgm.stocksorting.mapper.SortingCriteriaMapper;
import com.mgm.stocksorting.repository.ProductRepository;
import com.mgm.stocksorting.service.sorting.ProductSortingAlgorithm;

// CSOFF: Javadoc
@ExtendWith( MockitoExtension.class )
class ProductSortingServiceImplTest
{
    @Mock
    private ProductRepository repo;
    @Mock
    private ProductSortingAlgorithm sortingAlgo;
    @InjectMocks
    private ProductSortingServiceImpl service;

    @BeforeEach
    void setUp()
    {
        var productMapper = Mappers.getMapper( ProductMapper.class );
        var criteriaMapper = Mappers.getMapper( SortingCriteriaMapper.class );
        ReflectionTestUtils.setField( service, "productMapper", productMapper );
        ReflectionTestUtils.setField( service, "criteriaMapper", criteriaMapper );
        ReflectionTestUtils.setField( service, "computeStockSize", false );
    }

    @Test
    void sortProductsWhenProductsExistShouldReturnAListOfSortedProducts()
    {
        // Given
        var prodDescription = "Test Product";
        var prodId = 1L;
        var sales = 100;
        var stock = new ProductStock();
        stock.S( 10 ).M( 5 );
        var entity = new ProductEntity();
        entity.setId( prodId );
        entity.setName( prodDescription );
        entity.setSales( sales );
        entity.setStock(  Map.of( "S", 10, "M", 5 ) );

        var domain = new ProductDomain( prodId, prodDescription, sales, Map.of( "S", 10, "M", 5 ) );
        var sortedDomain = new ProductDomain( prodId, prodDescription, sales, Map.of( "S", 10, "M", 5 ) );

        var apiProduct = new Product( prodId, prodDescription, sales, stock );

        when( repo.findAll() ).thenReturn( List.of( entity ) );
        when( sortingAlgo.sort( eq( List.of( domain ) ), any() ) ).thenReturn( List.of( sortedDomain ) );

        var query = new ProductSortQuery();
        query.setOrder( ProductSortQuery.OrderEnum.ASC );
        var salesWeight = 0.4;
        var stockWeight = 0.6;
        query.setWeights( new ProductSortQueryWeights( salesWeight, stockWeight ) );

        // When
        List<Product> result = service.sortProducts( query );

        // Then
        assertNotNull( result );
        assertEquals( 1, result.size() );
        assertEquals( apiProduct, result.getFirst() );

        verify( repo ).findAll();
        var criteriaCaptor = ArgumentCaptor.forClass( SortingCriteriaDomain.class );
        verify( sortingAlgo ).sort( anyList(), criteriaCaptor.capture() );
        var criteria = criteriaCaptor.getValue();
        assertNotNull(  criteria );
        assertTrue( criteria.isAscending() );
        assertEquals( salesWeight, criteria.getSalesWeight() );
        assertEquals( stockWeight, criteria.getStockWeight() );
    }

    @Test
    void sortProductsWhenNoProductsFoundShouldReturnEmptyListWhenNoProductsFound()
    {
        // Given
        when( repo.findAll() ).thenReturn( List.of() );

        var query = new ProductSortQuery();

        // When
        List<Product> result = service.sortProducts( query );

        // Then
        assertNotNull( result );
        assertTrue( result.isEmpty() );

        verify( repo ).findAll();
        verifyNoInteractions( sortingAlgo );
    }
}