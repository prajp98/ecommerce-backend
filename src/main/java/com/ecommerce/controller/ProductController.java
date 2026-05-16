package com.ecommerce.controller;

import com.ecommerce.dto.request.ProductRequest;
import com.ecommerce.dto.response.ApiResponse;
import com.ecommerce.dto.response.ProductResponse;
import com.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(
            @Valid @RequestBody ProductRequest request) {

        return buildResponse(
                productService.createProduct(request),
                HttpStatus.CREATED,
                "Product created successfully"
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {

        return buildResponse(
                productService.updateProduct(id, request),
                HttpStatus.OK,
                "Product updated successfully"
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(
            @PathVariable Long id) {

        return buildResponse(
                productService.getProductById(id),
                HttpStatus.OK,
                "Product fetched successfully"
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {

        return buildResponse(
                productService.getAllProducts(),
                HttpStatus.OK,
                "Products fetched successfully"
        );
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getActiveProducts(
            Pageable pageable) {

        return buildResponse(
                productService.getActiveProducts(pageable),
                HttpStatus.OK,
                "Active products fetched successfully"
        );
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> searchProducts(
            @RequestParam String keyword,
            Pageable pageable) {

        return buildResponse(
                productService.searchActiveProducts(keyword, pageable),
                HttpStatus.OK,
                "Products fetched successfully"
        );
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProductsByCategory(
            @PathVariable Long categoryId,
            Pageable pageable) {

        return buildResponse(
                productService.getActiveProductsByCategory(categoryId, pageable),
                HttpStatus.OK,
                "Products fetched successfully"
        );
    }

    @GetMapping("/search/by-price")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> searchByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice,
            Pageable pageable) {

        return buildResponse(
                productService.searchActiveProductsByPriceRange(minPrice, maxPrice, pageable),
                HttpStatus.OK,
                "Products fetched successfully"
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<ProductResponse>> deactivateProduct(
            @PathVariable Long id) {

        return buildResponse(
                productService.deactivateProduct(id),
                HttpStatus.OK,
                "Product deactivated successfully"
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<ProductResponse>> activateProduct(
            @PathVariable Long id) {

        return buildResponse(
                productService.activateProduct(id),
                HttpStatus.OK,
                "Product activated successfully"
        );
    }

    private <T> ResponseEntity<ApiResponse<T>> buildResponse(
            T data,
            HttpStatus status,
            String message) {

        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setTimestamp(LocalDateTime.now());
        apiResponse.setStatus(status.value());
        apiResponse.setMessage(message);
        apiResponse.setData(data);

        return ResponseEntity.status(status).body(apiResponse);
    }
}