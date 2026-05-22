package com.ecommerce.service;

import com.ecommerce.dto.response.ProductImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductImageService {

    ProductImageResponse addImageToProduct(Long productId, MultipartFile file, boolean primaryImage);

    List<ProductImageResponse> getImagesByProductId(Long productId);

    ProductImageResponse deleteImage(Long imageId);

    ProductImageResponse setPrimaryImage(Long imageId);
}