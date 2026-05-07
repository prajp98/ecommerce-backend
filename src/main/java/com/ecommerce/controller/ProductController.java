package com.ecommerce.controller;

import com.ecommerce.dto.request.ProductRequest;
import com.ecommerce.dto.response.ProductResponse;
import com.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id,
                                                         @Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        List<ProductResponse> response = productService.getAllProducts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ProductResponse>> getAllActiveProducts() {
        List<ProductResponse> response = productService.getAllActiveProducts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategoryId(@PathVariable Long categoryId) {
        List<ProductResponse> response = productService.getProductsByCategoryId(categoryId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ProductResponse> deactivateProduct(@PathVariable Long id) {
        ProductResponse response = productService.deactivateProduct(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ProductResponse> activateProduct(@PathVariable Long id) {
        ProductResponse response = productService.activateProduct(id);
        return ResponseEntity.ok(response);
    }
}