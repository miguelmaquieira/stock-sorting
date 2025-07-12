package com.mgm.stocksorting.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.mgm.stocksorting.controller.api.ProductApiDelegate;
import com.mgm.stocksorting.controller.model.Product;
import com.mgm.stocksorting.controller.model.ProductSortQuery;
import com.mgm.stocksorting.controller.validation.ProductSortQueryValidator;
import com.mgm.stocksorting.service.ProductSortingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller that implements product sorting API delegate.
 * Delegates sorting logic to the service layer.
 *
 * @author Miguel Maquieira
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class ProductSortingController implements ProductApiDelegate
{

    private final ProductSortingService productSortingService;

    /**
     * Handles POST /products/sorted by applying sorting criteria.
     *
     * @param query sorting criteria and weights
     * @return sorted product list
     */
    @Operation( summary = "Sort products based on sales and stock weights",
                description = "Returns a list of products sorted by computed scores derived from weighted sales and stock values." )
    @ApiResponses( value = {
        @ApiResponse( responseCode = "200", description = "Sorted product list returned",
                                           content = @Content( mediaType = "application/json",
                                                               schema = @Schema( implementation = Product.class ) ) ),
        @ApiResponse( responseCode = "400", description = "Validation error",
                      content = @Content( mediaType = "application/json" ) ),
        @ApiResponse( responseCode = "500", description = "Internal server error", content = @Content ) } )
    @Override
    public ResponseEntity<List<Product>> getSortedProducts( final ProductSortQuery query )
    {
        ProductSortQueryValidator.validate( query );
        log.debug( "Getting sorted products with the following criteria: {}", query );

        var sortedProducts = productSortingService.sortProducts( query );
        var result = ( sortedProducts != null ) ? sortedProducts : List.<Product>of();

        log.debug( "{} elements sorted.", result.size() );

        return ResponseEntity.ok( result );
    }
}