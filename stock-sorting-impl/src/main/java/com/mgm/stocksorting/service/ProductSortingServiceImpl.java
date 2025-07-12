package com.mgm.stocksorting.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mgm.stocksorting.controller.model.Product;
import com.mgm.stocksorting.controller.model.ProductSortQuery;
import com.mgm.stocksorting.mapper.ProductMapper;
import com.mgm.stocksorting.mapper.SortingCriteriaMapper;
import com.mgm.stocksorting.repository.ProductRepository;
import com.mgm.stocksorting.service.sorting.ProductSortingAlgorithm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service implementation that retrieves products, applies sorting, and maps to API models.
 * Delegates sorting logic to {@link ProductSortingAlgorithm}.
 *
 * @author Miguel Maquieira
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductSortingServiceImpl implements ProductSortingService
{
    private final ProductRepository repo;
    private final ProductMapper productMapper;
    private final SortingCriteriaMapper criteriaMapper;
    private final ProductSortingAlgorithm sortingAlgo;

    @Value( "${product.sorting.stock.computeStockSize: false}" )
    private boolean computeStockSize;

    @Override
    public List<Product> sortProducts( final ProductSortQuery sortingQuery )
    {
        if ( sortingQuery == null )
        {
            throw new IllegalArgumentException( "Sorting query must not be null" );
        }
        log.debug( "Sorting products with the following criteria: {}", sortingQuery );

        var products = repo.findAll();
        if ( products.isEmpty() )
        {
            log.warn( "No products found to be sorted" );
            return List.of();
        }
        log.debug( "Found {} products to be sorted", products.size() );

        var allProducts = products.stream().map( productMapper::entityToDomain ).toList();

        var sortingCriteria = criteriaMapper.toDomain( sortingQuery, computeStockSize );
        var sortedProducts = sortingAlgo.sort( allProducts, sortingCriteria );
        if ( log.isDebugEnabled() )
        {
            log.debug( "Sorted product list: \n{}", sortedProducts.stream()
                .map( p -> String.format( " - [%d] %s", p.id(), p.name() ) )
                .collect( Collectors.joining( "\n" ) ) );
        }

        return sortedProducts.stream().map( productMapper::domainToApi ).toList();
    }
}
