package com.mgm.stocksorting.exception;

import java.io.Serial;
import java.util.List;

import com.mgm.stocksorting.exception.model.ApiError;
import com.mgm.stocksorting.exception.model.ApiErrorType;

/**
 * Exception representing a 400 Bad Request scenario.
 *
 * <p>
 * Thrown when the request is syntactically valid but cannot be processed
 * due to client-side input issues, such as malformed data, unexpected values,
 * or unsupported content types.
 * </p>
 *
 * @author Miguel Maquieira
 */
public class ApiBadRequestException extends ApiException
{

    @Serial
    private static final long serialVersionUID = 2766537939905849585L;

    public ApiBadRequestException( final List<ApiError> errors )
    {
        super( errors, ApiErrorType.VALIDATION ); // Call super with errors list and specific ErrorType
    }

    public ApiBadRequestException( final String message, final ApiErrorType errorType )
    {
        super( message, errorType );
    }

    public ApiBadRequestException( final List<ApiError> errors, final ApiErrorType apiErrorType )
    {
        // Convert ApiErrorType to ErrorType assuming enum names match
        super( errors, ApiErrorType.valueOf( apiErrorType.name() ) );
    }

    public static ApiBadRequestException fromError( final ApiError error, final ApiErrorType errorType )
    {
        // The BiFunction passed here will resolve to the constructor
        // ApiBadRequestException(List<ApiError> errors, ApiErrorType apiErrorType)
        return ApiException.fromError( error, errorType, ApiBadRequestException::new );
    }
}
