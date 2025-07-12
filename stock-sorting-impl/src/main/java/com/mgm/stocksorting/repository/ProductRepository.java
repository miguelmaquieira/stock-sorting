package com.mgm.stocksorting.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mgm.stocksorting.entity.ProductEntity;

/**
 * Repository for accessing product data from the database.
 * Provides CRUD operations and query methods for ProductEntity.
 *
 * @author Miguel Maquieira
 */
public interface ProductRepository extends JpaRepository<ProductEntity, Long>
{
}
