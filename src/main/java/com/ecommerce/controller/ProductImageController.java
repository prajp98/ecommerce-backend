package com.ecommerce.controller;

import com.ecommerce.dto.response.ApiResponse;
import com.ecommerce.dto.response.ProductImageResponse;
import com.ecommerce.service.ProductImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    @PostMapping(value = "/{productId}/images/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductImageResponse>> uploadImageToProduct(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "primaryImage", defaultValue = "false") boolean primaryImage) {

        ProductImageResponse response =
                productImageService.addImageToProduct(productId, file, primaryImage);

        return buildResponse(response, HttpStatus.CREATED, "Image uploaded successfully");
    }

    @GetMapping("/{productId}/images")
    public ResponseEntity<ApiResponse<List<ProductImageResponse>>> getImagesByProductId(
            @PathVariable Long productId) {

        List<ProductImageResponse> response = productImageService.getImagesByProductId(productId);
        return buildResponse(response, HttpStatus.OK, "Images fetched successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<ApiResponse<ProductImageResponse>> deleteImage(
            @PathVariable Long imageId) {

        ProductImageResponse response = productImageService.deleteImage(imageId);
        return buildResponse(response, HttpStatus.OK, "Image deleted successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/images/{imageId}/primary")
    public ResponseEntity<ApiResponse<ProductImageResponse>> setPrimaryImage(
            @PathVariable Long imageId) {

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