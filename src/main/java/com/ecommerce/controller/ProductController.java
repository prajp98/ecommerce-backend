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
    public ResponseEntity<ApiResponse<ProductResponse>> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return buildResponse(response, HttpStatus.CREATED, "Product created successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> updateProduct(@PathVariable Long id,
                                                                      @Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.updateProduct(id, request);
        return buildResponse(response, HttpStatus.OK, "Product updated successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable Long id) {
        ProductResponse response = productService.getProductById(id);
        return buildResponse(response, HttpStatus.OK, "Product fetched successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponse>>> getAllProducts() {
        List<ProductResponse> response = productService.getAllProducts();
        return buildResponse(response, HttpStatus.OK, "Products fetched successfully");
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getActiveProducts(Pageable pageable) {
        Page<ProductResponse> response = productService.getActiveProducts(pageable);
        return buildResponse(response, HttpStatus.OK, "Active products fetched successfully", response);
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> searchProducts(@RequestParam String keyword,
                                                                             Pageable pageable) {
        Page<ProductResponse> response = productService.searchActiveProducts(keyword, pageable);
        return buildResponse(response, HttpStatus.OK, "Products fetched successfully", response);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getProductsByCategory(@PathVariable Long categoryId,
                                                                                    Pageable pageable) {
        Page<ProductResponse> response = productService.getActiveProductsByCategory(categoryId, pageable);
        return buildResponse(response, HttpStatus.OK, "Products fetched successfully", response);
    }

    @GetMapping("/search/by-price")
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> searchByPriceRange(@RequestParam BigDecimal minPrice,
                                                                                 @RequestParam BigDecimal maxPrice,
                                                                                 Pageable pageable) {
        Page<ProductResponse> response = productService.searchActiveProductsByPriceRange(minPrice, maxPrice, pageable);
        return buildResponse(response, HttpStatus.OK, "Products fetched successfully", response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<ProductResponse>> deactivateProduct(@PathVariable Long id) {
        ProductResponse response = productService.deactivateProduct(id);
        return buildResponse(response, HttpStatus.OK, "Product deactivated successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<ProductResponse>> activateProduct(@PathVariable Long id) {
        ProductResponse response = productService.activateProduct(id);
        return buildResponse(response, HttpStatus.OK, "Product activated successfully");
    }

    private ResponseEntity<ApiResponse<ProductResponse>> buildResponse(ProductResponse data,
                                                                       HttpStatus status,
                                                                       String message) {
        ApiResponse<ProductResponse> apiResponse = new ApiResponse<>();
        apiResponse.setTimestamp(LocalDateTime.now());
        apiResponse.setStatus(status.value());
        apiResponse.setMessage(message);
        apiResponse.setData(data);
        return ResponseEntity.status(status).body(apiResponse);
    }

    private ResponseEntity<ApiResponse<List<ProductResponse>>> buildResponse(List<ProductResponse> data,
                                                                             HttpStatus status,
                                                                             String message) {
        ApiResponse<List<ProductResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setTimestamp(LocalDateTime.now());
        apiResponse.setStatus(status.value());
        apiResponse.setMessage(message);
        apiResponse.setData(data);
        return ResponseEntity.status(status).body(apiResponse);
    }

    private ResponseEntity<ApiResponse<Page<ProductResponse>>> buildResponse(Page<ProductResponse> data,
                                                                             HttpStatus status,
                                                                             String message,
                                                                             Page<ProductResponse> pageData) {
        ApiResponse<Page<ProductResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setTimestamp(LocalDateTime.now());
        apiResponse.setStatus(status.value());
        apiResponse.setMessage(message);
        apiResponse.setData(pageData);
        return ResponseEntity.status(status).body(apiResponse);
    }
}