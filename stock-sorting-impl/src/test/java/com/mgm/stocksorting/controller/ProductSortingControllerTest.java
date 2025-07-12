package com.mgm.stocksorting.controller;

import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.mgm.stocksorting.controller.model.Product;
import com.mgm.stocksorting.controller.model.ProductSortQuery;
import com.mgm.stocksorting.controller.model.ProductSortQueryWeights;
import com.mgm.stocksorting.controller.model.ProductStock;
import com.mgm.stocksorting.service.ProductSortingService;

// CSOFF: Javadoc
@ExtendWith( MockitoExtension.class )
@TestMethodOrder( MethodOrderer.MethodName.class )
class ProductSortingControllerTest
{
    @Mock
    private ProductSortingService sortingService;
    @InjectMocks
    private ProductSortingController controller;

    @Test
    void shouldReturnSortedProducts()
    {
        // Given
        var query = new ProductSortQuery();
        var weights = new ProductSortQueryWeights( 0.5, 0.5 );
        query.setWeights( weights );

        ProductStock stock = new ProductStock().S( 10 ).M( 5 );
        var expectedProduct = new Product( 1L, "Test Product", 100, stock );
        var expectedProducts = List.of( expectedProduct );

        when( sortingService.sortProducts( query ) ).thenReturn( expectedProducts );

        // When
        ResponseEntity<List<Product>> response = controller.getSortedProducts( query );

        // Then
        assertEquals( HttpStatusCode.valueOf( 200 ), response.getStatusCode() );
        assertEquals( expectedProducts, response.getBody() );
        verify( sortingService ).sortProducts( query );
    }

    @Test
    void shouldReturnEmptyListWhenServiceReturnsNull()
    {
        // Given
        var query = new ProductSortQuery();
        var weights = new ProductSortQueryWeights( 0.5, 0.5 );
        query.setWeights( weights );
        when( sortingService.sortProducts( query ) ).thenReturn( null );

        // When
        var response = controller.getSortedProducts( query );

        // Then
        assertEquals( HttpStatusCode.valueOf( 200 ), response.getStatusCode() );
        assertNotNull( response.getBody() );
        assertTrue( response.getBody().isEmpty() );
        verify( sortingService ).sortProducts( query );
    }

}