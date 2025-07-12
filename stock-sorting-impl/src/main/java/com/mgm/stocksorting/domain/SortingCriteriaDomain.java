package com.mgm.stocksorting.domain;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Domain-level representation of product sorting criteria.
 *
 * @author Miguel Maquieira
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SortingCriteriaDomain
{
    private double salesWeight;
    private double stockWeight;
    private Map<String, Double> stockSizeWeights;
    private boolean computeStockSize;
    private boolean ascending;
}
