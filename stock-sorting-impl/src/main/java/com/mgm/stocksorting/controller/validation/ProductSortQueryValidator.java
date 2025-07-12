package com.mgm.stocksorting.controller.validation;

import java.util.ArrayList;

import com.mgm.stocksorting.controller.model.ProductSortQuery;
import com.mgm.stocksorting.exception.ApiBadRequestException;
import com.mgm.stocksorting.exception.model.ApiError;
import com.mgm.stocksorting.exception.model.ApiErrorType;

/**
 * This class provides validation logic to ensure that sorting query parameters
 * are present and conform to expected constraints.
 *
 * @author Miguel Maquieira
 */
public class ProductSortQueryValidator
{
    public static void validate( final ProductSortQuery sortQuery )
    {
        if ( sortQuery == null )
        {
            throw new ApiBadRequestException( "Sort query request can not be null", ApiErrorType.VALIDATION );
        }

        var weights = sortQuery.getWeights();
        if ( weights == null )
        {
            throw new ApiBadRequestException( "Weights can not be null",
                ApiErrorType.VALIDATION );
        }

        var errors = new ArrayList<ApiError>();

        var salesWeight = weights.getSales();
        var stockWeight = weights.getStock();
        if ( salesWeight < 0 )
        {
            var error = ApiError.builder()
                .key( "salesWeight" )
                .value( String.valueOf( salesWeight ) )
                .message( "'sales weight' must be greater than or equal to 0" )
                .errorCode( "validation.negative_param" )
                .build();
            errors.add( error );
        }

        if ( stockWeight < 0 )
        {
            var error = ApiError.builder()
                .key( "stockWeight" )
                .value( String.valueOf( stockWeight ) )
                .message( "'stock weight' must be greater than or equal to 0" )
                .errorCode( "validation.negative_param" )
                .build();
            errors.add( error );
        }

        var weightsSum = salesWeight + stockWeight;
        if ( weightsSum != 1 )
        {
            var error = ApiError.builder()
                .key( "weights" )
                .value( String.valueOf( weightsSum ) )
                .message( "sum of weights must be equal to 1" )
                .errorCode( "validation.max_value" )
                .build();
            errors.add( error );
        }

        if ( !errors.isEmpty() )
        {
            throw new ApiBadRequestException( errors );
        }
    }
}
