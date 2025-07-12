package com.mgm.stocksorting.domain;

import java.util.Map;

/**
 * Domain model representing a product used in business logic.
 * Contains product ID, name, sales count, and stock levels by size.
 *
 * @author Miguel Maquieira
 */
public record ProductDomain( long id, String name, int sales, Map<String, Integer> stock )
{
}
