package com.ecommerce.controller;

import com.ecommerce.dto.request.ProductImageRequest;
import com.ecommerce.dto.response.ApiResponse;
import com.ecommerce.dto.response.ProductImageResponse;
import com.ecommerce.service.ProductImageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
    public ResponseEntity<ApiResponse<ProductImageResponse>> addImageToProduct(
            @PathVariable Long productId,
            @Valid @RequestBody ProductImageRequest request) {

        return buildResponse(
                productImageService.addImageToProduct(productId, request),
                HttpStatus.CREATED,
                "Image added successfully"
        );
    }

    @GetMapping("/{productId}/images")
    public ResponseEntity<ApiResponse<List<ProductImageResponse>>> getImagesByProductId(
            @PathVariable Long productId) {

        return buildResponse(
                productImageService.getImagesByProductId(productId),
                HttpStatus.OK,
                "Images fetched successfully"
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<ApiResponse<ProductImageResponse>> deleteImage(
            @PathVariable Long imageId) {

        return buildResponse(
                productImageService.deleteImage(imageId),
                HttpStatus.OK,
                "Image deleted successfully"
        );
    }

    @PatchMapping("/images/{imageId}/primary")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductImageResponse>> setPrimaryImage(@PathVariable Long imageId) {
        ProductImageResponse response = productImageService.setPrimaryImage(imageId);
        return buildResponse(response, HttpStatus.OK, "Primary image updated successfully");
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