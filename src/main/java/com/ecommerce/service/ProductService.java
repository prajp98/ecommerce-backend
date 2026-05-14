package com.ecommerce.service;

import com.ecommerce.dto.request.ProductRequest;
import com.ecommerce.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
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

    Page<ProductResponse> getActiveProducts(Pageable pageable);

    Page<ProductResponse> searchActiveProducts(String keyword, Pageable pageable);

    Page<ProductResponse> getActiveProductsByCategory(Long categoryId, Pageable pageable);

    Page<ProductResponse> searchActiveProductsByPriceRange(
            BigDecimal minPrice,
            BigDecimal maxPrice,
            Pageable pageable
    );
}