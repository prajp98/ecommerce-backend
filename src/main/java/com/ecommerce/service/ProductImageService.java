package com.ecommerce.service;

import com.ecommerce.dto.request.ProductImageRequest;
import com.ecommerce.dto.response.ProductImageResponse;

import java.util.List;

public interface ProductImageService {

    ProductImageResponse addImageToProduct(Long productId, ProductImageRequest request);

    List<ProductImageResponse> getImagesByProductId(Long productId);

    ProductImageResponse deleteImage(Long imageId);

    ProductImageResponse setPrimaryImage(Long imageId);
}