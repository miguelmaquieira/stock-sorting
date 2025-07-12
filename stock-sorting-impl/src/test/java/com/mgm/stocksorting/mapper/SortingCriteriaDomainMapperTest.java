package com.mgm.stocksorting.mapper;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import static org.junit.jupiter.api.Assertions.*;

import com.mgm.stocksorting.controller.model.ProductSortQuery;
import com.mgm.stocksorting.controller.model.ProductSortQueryWeights;
import com.mgm.stocksorting.controller.model.ProductSortQueryWeightsStockSize;
import com.mgm.stocksorting.domain.StockSizeDomain;

// CSOFF: Javadoc
class SortingCriteriaDomainMapperTest
{
    private final SortingCriteriaMapper mapper = Mappers.getMapper( SortingCriteriaMapper.class );

    @Test
    void toDomainWhenWithoutStockSizeWeightsShouldReturnDomain() {
        var weights = new ProductSortQueryWeights();
        var salesWeight = 0.7;
        var stockWeight = 0.3;
        weights.setSales( salesWeight );
        weights.setStock( stockWeight );
        weights.setStockSize( null );

        var query = new ProductSortQuery();
        query.setWeights( weights );
        query.setOrder( ProductSortQuery.OrderEnum.DESC );

        var criteria = mapper.toDomain(query, true );

        assertNotNull( criteria );
        assertEquals( salesWeight, criteria.getSalesWeight() );
        assertEquals( stockWeight, criteria.getStockWeight() );
        assertFalse( criteria.isAscending() );
        assertTrue( criteria.isComputeStockSize() );
        assertNull( criteria.getStockSizeWeights() );
    }

    @Test
    void toDomainWhenWithStockSizeWeightsShouldReturnDomain() {
        var weights = new ProductSortQueryWeights();
        var salesWeight = 0.4;
        var stockWeight = 0.6;
        weights.setSales( salesWeight );
        weights.setStock( stockWeight );
        var stockWeights = new ProductSortQueryWeightsStockSize();
        var stockSizeSWeight = 0.2;
        var stockSizeMWeight = 0.5;
        var stockSizeLWeight = 0.3;
        stockWeights.setS( stockSizeSWeight );
        stockWeights.setM( stockSizeMWeight );
        stockWeights.setL( stockSizeLWeight );
        weights.setStockSize( stockWeights );

        var query = new ProductSortQuery();
        query.setWeights( weights );
        query.setOrder( ProductSortQuery.OrderEnum.ASC );

        var criteria = mapper.toDomain(query, false );

        assertNotNull( criteria );
        assertEquals( salesWeight, criteria.getSalesWeight() );
        assertEquals( stockWeight, criteria.getStockWeight() );
        assertTrue( criteria.isAscending() );
        assertFalse( criteria.isComputeStockSize() );
        assertNotNull( criteria.getStockSizeWeights() );
        assertEquals(
            Map.of(
                StockSizeDomain.S.name(), stockSizeSWeight,
                StockSizeDomain.M.name(), stockSizeMWeight,
                StockSizeDomain.L.name(), stockSizeLWeight ),
            criteria.getStockSizeWeights() );
    }
}