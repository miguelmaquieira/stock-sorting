package com.mgm.stocksorting.mapper;

import org.mapstruct.Mapper;

import com.mgm.stocksorting.controller.model.Product;
import com.mgm.stocksorting.domain.ProductDomain;
import com.mgm.stocksorting.entity.ProductEntity;

/**
 * Maps between different representations of a product across the application layers.
 *
 * @author Miguel Maquieira
 */
@Mapper( componentModel = "spring")
public interface ProductMapper
{

    // Entity → Domain
    ProductDomain entityToDomain( ProductEntity entity );

    // Domain → API
    Product domainToApi( ProductDomain domain );

    // Domain → Entity
    ProductEntity domainToEntity( ProductDomain domain );
}
