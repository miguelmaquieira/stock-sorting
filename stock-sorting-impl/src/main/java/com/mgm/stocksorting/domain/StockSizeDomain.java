package com.mgm.stocksorting.domain;

/**
 * Enumeration of valid product sizes. Used as keys for stock data and size-weighted stock scoring.
 *
 * @author Miguel Maquieira
 */
public enum StockSizeDomain
{
    S,
    M,
    L;

    /**
     * Parses a string into a Size enum value, case-insensitive.
     *
     * @param value the input string (e.g., "S", "M", "L")
     * @return the corresponding Size enum
     * @throws IllegalArgumentException if the value doesn't match any Size
     */
    public static StockSizeDomain fromString( final String value )
    {
        try
        {
            return StockSizeDomain.valueOf( value.toUpperCase() );
        }
        catch ( Exception e )
        {
            throw new IllegalArgumentException( "Invalid size: " + value );
        }
    }
}
