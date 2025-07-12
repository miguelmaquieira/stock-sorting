package com.mgm.stocksorting.entity;

import java.util.HashMap;
import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Entity representing a product in the catalog.
 * Contains product ID, name, sales count, and stock per size.
 *
 * @author Miguel Maquieira
 */
@Entity
@Table( name = "products" )
@Data
public class ProductEntity
{
    /**
     * The unique ID of the product.
     */
    @Id
    private Long id;

    /**
     * The name of the product.
     */
    @Column( nullable = false )
    private String name;

    /**
     * The number of units sold for this product.
     */
    @Column( nullable = false )
    private Integer sales;

    /**
     * Map of stock levels by size (S, M, L).
     * Stored in a separate table as an element collection.
     */
    @ElementCollection
    @CollectionTable( name = "product_stock", joinColumns = @JoinColumn( name = "product_id" ) )
    @MapKeyColumn( name = "size" )
    @Column( name = "quantity" )
    private Map<String, Integer> stock = new HashMap<>();
}
