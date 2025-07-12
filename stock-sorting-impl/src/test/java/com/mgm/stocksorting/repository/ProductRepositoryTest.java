package com.mgm.stocksorting.repository;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mgm.stocksorting.entity.ProductEntity;

// CSOFF: Javadoc
@DataJpaTest
class ProductRepositoryTest
{

    @Autowired
    private ProductRepository cut;

    @Test
    void findByIdWhenProductExistShouldSaveAndLoadProduct() {
        // Given
        var expectedId = 1L;
        var product = new ProductEntity();
        product.setId( expectedId );
        product.setName( "Test T-Shirt" );
        product.setSales( 100 );
        product.setStock( Map.of( "S", 10, "M", 5, "L", 0 ) );

        // When
        cut.save( product );

        var found = cut.findById( expectedId );

        // Then
        assertNotNull( found );
        assertTrue( found.isPresent() );
        var productItem = found.get();
        assertEquals( "Test T-Shirt" , productItem.getName() );
    }

    @Test
    void findAllWhenThereAreResultsShouldReturnAListOfProducts()
    {
        // Given
        var prod1 = new ProductEntity();
        prod1.setId( 1L );
        prod1.setName( "A" );
        prod1.setSales( 10 );
        prod1.setStock( Map.of( "S", 5, "M", 2, "L", 1 ) );

        var prod2 = new ProductEntity();
        prod2.setId( 2L );
        prod2.setName( "B" );
        prod2.setSales( 20 );
        prod2.setStock( Map.of( "S", 1, "M", 3, "L", 4 ) );

        cut.saveAll( List.of( prod1, prod2 ) );

        // When
        var all = cut.findAll();

        // Then
        assertEquals( 2, all.size() );
    }

    @Test
    @Sql("/sql/test-data.sql")
    void shouldLoadProductsFromSqlScript() {

        var result = cut.findAll();

        assertEquals( 6, result.size(), "Should load 6 products" );

        assertFalse( result.getFirst().getStock().isEmpty(), "First product stock should not be empty" );
        assertFalse( result.getLast().getStock().isEmpty(), "Last product stock should not be empty" );
    }
}