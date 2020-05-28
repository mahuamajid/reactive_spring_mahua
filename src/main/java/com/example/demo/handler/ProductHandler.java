package com.example.demo.handler;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

@Component
public class ProductHandler {

    @Autowired
    private ProductRepository productRepository;

    public Mono<ServerResponse> findAllProducts(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(TEXT_EVENT_STREAM)
                .body(productRepository.findAll(), Product.class);
    }

    public Mono<ServerResponse> findProductById(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(TEXT_EVENT_STREAM)
                .body(productRepository.findById(request.pathVariable("id")), Product.class);
    }

    public Mono<ServerResponse> createProduct(ServerRequest request) {
        Mono<Product> productMono = request.bodyToMono(Product.class)
                .flatMap(product -> productRepository.save(product));
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(productMono, Product.class);
    }

    public Mono<ServerResponse> updateProduct(ServerRequest request) {
        Mono<Product> productMono = request.bodyToMono(Product.class)
                .flatMap(product -> {
                    return productRepository.findById(product.getId()).map(e -> {
                        e.setName(product.getName());
                        e.setCategory(product.getCategory());
                        e.setPrice(product.getPrice());
                        return e;
                    }).flatMap(n -> productRepository.save(n));
                });
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(productMono, Product.class);
    }

    public Mono<ServerResponse> deleteProduct(ServerRequest request) {
        return ServerResponse.ok()
                .contentType(APPLICATION_JSON)
                .body(productRepository.deleteById(request.pathVariable("id")), Product.class);
    }
}
