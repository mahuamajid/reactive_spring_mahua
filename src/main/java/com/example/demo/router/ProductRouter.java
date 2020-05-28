package com.example.demo.router;

import com.example.demo.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Component
public class ProductRouter {
    @Bean
    public RouterFunction route(ProductHandler productHandler) {
        return RouterFunctions
                .route(POST("/v3/products").and(contentType(APPLICATION_JSON)), productHandler::createProduct)
                .andRoute(GET("/v3/products").and(RequestPredicates.accept(APPLICATION_STREAM_JSON)), productHandler::findAllProducts)
                .andRoute(GET("/v3/products/{id}").and(RequestPredicates.accept(APPLICATION_STREAM_JSON)), productHandler::findProductById)
                .andRoute(PUT("/v3/products").and(accept(APPLICATION_JSON)), productHandler::updateProduct)
                .andRoute(DELETE("/v3/products/{id}").and(accept(APPLICATION_JSON)), productHandler::deleteProduct);
    }
}
