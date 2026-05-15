package com.ecommerce.service.impl;

import com.ecommerce.dto.request.ProductRequest;
import com.ecommerce.dto.response.ProductResponse;
import com.ecommerce.entity.Category;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.DuplicateResourceException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.mapper.ProductMapper;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        String name = request.getName().trim();

        if (productRepository.existsByName(name)) {
            throw new DuplicateResourceException("Product already exists");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + request.getCategoryId()
                ));

        Product product = new Product();
        product.setName(name);
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category);
        product.setActive(true);

        Product savedProduct = productRepository.save(product);
        return buildResponse(savedProduct, "Product created successfully");
    }

    @Override
    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        String newName = request.getName().trim();

        if (!product.getName().equalsIgnoreCase(newName) && productRepository.existsByName(newName)) {
            throw new DuplicateResourceException("Product already exists");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found with id: " + request.getCategoryId()
                ));

        product.setName(newName);
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(category);

        Product updatedProduct = productRepository.save(product);
        return buildResponse(updatedProduct, "Product updated successfully");
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        return buildResponse(product, "Product fetched successfully");
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(product -> buildResponse(product, "Product fetched successfully"))
                .toList();
    }

    @Override
    public List<ProductResponse> getAllActiveProducts() {
        return productRepository.findByActiveTrue()
                .stream()
                .map(product -> buildResponse(product, "Product fetched successfully"))
                .toList();
    }

    @Override
    public List<ProductResponse> getProductsByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(product -> buildResponse(product, "Product fetched successfully"))
                .toList();
    }

    @Override
    @Transactional
    public ProductResponse deactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setActive(false);
        Product savedProduct = productRepository.save(product);

        return buildResponse(savedProduct, "Product deactivated successfully");
    }

    @Override
    @Transactional
    public ProductResponse activateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setActive(true);
        Product savedProduct = productRepository.save(product);

        return buildResponse(savedProduct, "Product activated successfully");
    }

    @Override
    public Page<ProductResponse> getActiveProducts(Pageable pageable) {
        return productRepository.findByActiveTrue(pageable)
                .map(product -> buildResponse(product, "Product fetched successfully"));
    }

    @Override
    public Page<ProductResponse> searchActiveProducts(String keyword, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCaseAndActiveTrue(keyword, pageable)
                .map(product -> buildResponse(product, "Product fetched successfully"));
    }

    @Override
    public Page<ProductResponse> getActiveProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByActiveTrueAndCategoryId(categoryId, pageable)
                .map(product -> buildResponse(product, "Product fetched successfully"));
    }

    @Override
    public Page<ProductResponse> searchActiveProductsByPriceRange(BigDecimal minPrice,
                                                                  BigDecimal maxPrice,
                                                                  Pageable pageable) {
        return productRepository.findByActiveTrueAndPriceBetween(minPrice, maxPrice, pageable)
                .map(product -> buildResponse(product, "Product fetched successfully"));
    }

    private ProductResponse buildResponse(Product product, String message) {
        ProductResponse response = productMapper.toResponse(product);
        response.setMessage(message);
        return response;
    }
}