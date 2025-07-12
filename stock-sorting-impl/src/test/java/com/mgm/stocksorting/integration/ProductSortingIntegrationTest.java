package com.mgm.stocksorting.integration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgm.stocksorting.controller.model.Product;
import com.mgm.stocksorting.controller.model.ProductSortQuery;
import com.mgm.stocksorting.controller.model.ProductSortQueryWeights;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// CSOFF: Javadoc
@SpringBootTest
@AutoConfigureMockMvc
@Sql( scripts = "/sql/test-data.sql" )
public class ProductSortingIntegrationTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void shouldReturnSortedProducts() throws Exception
    {
        var query = new ProductSortQuery();
        var weights = new ProductSortQueryWeights( 0.5, 0.5 );
        query.setWeights( weights );

        var response = mockMvc.perform( post( "/api/v1/products/sorted" )
            .contentType( MediaType.APPLICATION_JSON )
            .content( mapper.writeValueAsString( query ) ) )
            .andExpect( status().isOk() )
            .andReturn();

        var resultJson = response.getResponse().getContentAsString();
        var products = mapper.readerForListOf( Product.class ).readValue( resultJson );

        Assertions.assertNotNull( products );
    }
}
