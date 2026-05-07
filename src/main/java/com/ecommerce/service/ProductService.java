package com.ecommerce.service;

import com.ecommerce.dto.request.ProductRequest;
import com.ecommerce.dto.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse createProduct(ProductRequest request);

    ProductResponse updateProduct(Long id, ProductRequest request);

    ProductResponse getProductById(Long id);

    List<ProductResponse> getAllProducts();

    List<ProductResponse> getAllActiveProducts();

    List<ProductResponse> getProductsByCategoryId(Long categoryId);

    ProductResponse deactivateProduct(Long id);

    ProductResponse activateProduct(Long id);
}