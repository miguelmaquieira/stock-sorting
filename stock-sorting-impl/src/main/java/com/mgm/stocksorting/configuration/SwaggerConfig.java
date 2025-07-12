package com.mgm.stocksorting.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

/**
 * Swagger/OpenAPI configuration enabled for non-production environments.
 *
 * @author Miguel Maquieira
 */
@Configuration
@Profile( "!prod" )
public class SwaggerConfig
{
    @Bean
    public OpenAPI customOpenAPI()
    {
        return new OpenAPI().info( new Info().title( "Product Sorting API" )
            .version( "v1" )
            .description( "API documentation for the product sorting service" ) );
    }
}
