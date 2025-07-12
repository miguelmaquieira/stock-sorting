package com.mgm.stocksorting.mapper;

import java.util.Map;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.mgm.stocksorting.controller.model.ProductSortQuery;
import com.mgm.stocksorting.controller.model.ProductSortQueryWeights;
import com.mgm.stocksorting.domain.StockSizeDomain;
import com.mgm.stocksorting.domain.SortingCriteriaDomain;

/**
 * Maps the API-level {@link ProductSortQuery} into the domain-level {@link SortingCriteriaDomain}.
 *
 * @author Miguel Maquieira
 */
@Mapper( componentModel = "spring" )
public interface SortingCriteriaMapper
{
    @Mapping( target = "salesWeight", source = "weights.sales" )
    @Mapping( target = "stockWeight", source = "weights.stock" )
    @Mapping( target = "stockSizeWeights", expression = "java(toMap(query.getWeights()))" )
    @Mapping( target = "computeStockSize", expression = "java(computeStockSize)" )
    @Mapping( target = "ascending", expression = "java(query.getOrder() == ProductSortQuery.OrderEnum.ASC)" )
    SortingCriteriaDomain toDomain( ProductSortQuery query, @Context boolean computeStockSize );

    default Map<String, Double> toMap( final ProductSortQueryWeights weights )
    {
        if ( weights.getStockSize() == null )
        {
            return null;
        }
        return Map.of(
            StockSizeDomain.S.name(), weights.getStockSize().getS(),
            StockSizeDomain.M.name(), weights.getStockSize().getM(),
            StockSizeDomain.L.name(), weights.getStockSize().getL() );
    }
}
