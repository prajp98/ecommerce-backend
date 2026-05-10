package com.ecommerce.controller;

import com.ecommerce.dto.request.ProductImageRequest;
import com.ecommerce.dto.response.ProductImageResponse;
import com.ecommerce.service.ProductImageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductImageController {

    private final ProductImageService productImageService;

    public ProductImageController(ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{productId}/images")
    public ResponseEntity<ProductImageResponse> addImageToProduct(@PathVariable Long productId,
                                                                  @Valid @RequestBody ProductImageRequest request) {
        ProductImageResponse response = productImageService.addImageToProduct(productId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{productId}/images")
    public ResponseEntity<List<ProductImageResponse>> getImagesByProductId(@PathVariable Long productId) {
        List<ProductImageResponse> response = productImageService.getImagesByProductId(productId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<ProductImageResponse> deleteImage(@PathVariable Long imageId) {
        ProductImageResponse response = productImageService.deleteImage(imageId);
        return ResponseEntity.ok(response);
    }
}