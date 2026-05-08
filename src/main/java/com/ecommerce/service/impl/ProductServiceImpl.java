package com.ecommerce.service.impl;

import com.ecommerce.dto.request.ProductRequest;
import com.ecommerce.dto.response.ProductResponse;
import com.ecommerce.entity.Category;
import com.ecommerce.entity.Product;
import com.ecommerce.exception.DuplicateResourceException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        String name = request.getName().trim();

        boolean exists = productRepository.existsByName(name);
        if (exists) {
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
        return toResponse(savedProduct, "Product created successfully");
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
        return toResponse(updatedProduct, "Product updated successfully");
    }

    @Override
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        return toResponse(product, "Product fetched successfully");
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> toResponse(product, "Product fetched successfully"))
                .toList();
    }

    @Override
    public List<ProductResponse> getAllActiveProducts() {
        List<Product> products = productRepository.findByActiveTrue();
        return products.stream()
                .map(product -> toResponse(product, "Product fetched successfully"))
                .toList();
    }

    @Override
    public List<ProductResponse> getProductsByCategoryId(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        return products.stream()
                .map(product -> toResponse(product, "Product fetched successfully"))
                .toList();
    }

    @Override
    @Transactional
    public ProductResponse deactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setActive(false);
        Product savedProduct = productRepository.save(product);
        return toResponse(savedProduct, "Product deactivated successfully");
    }

    @Override
    @Transactional
    public ProductResponse activateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        product.setActive(true);
        Product savedProduct = productRepository.save(product);
        return toResponse(savedProduct, "Product activated successfully");
    }

    private ProductResponse toResponse(Product product, String message) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setCategoryId(product.getCategory().getId());
        response.setCategoryName(product.getCategory().getName());
        response.setActive(product.isActive());
        response.setMessage(message);
        return response;
    }

    @Override
    public Page<ProductResponse> getActiveProducts(Pageable pageable) {
        Page<Product> productsPage = productRepository.findByActiveTrue(pageable);
        return productsPage.map(product -> toResponse(product, "Product fetched successfully"));
    }

    @Override
    public Page<ProductResponse> searchActiveProducts(String keyword, Pageable pageable) {
        Page<Product> productsPage =
                productRepository.findByNameContainingIgnoreCaseAndActiveTrue(keyword, pageable);

        return productsPage.map(product -> toResponse(product, "Product fetched successfully"));
    }

    @Override
    public Page<ProductResponse> getActiveProductsByCategory(Long categoryId, Pageable pageable) {
        Page<Product> productsPage = productRepository.findByCategoryIdAndActiveTrue(categoryId, pageable);
        return productsPage.map(product -> toResponse(product, "Product fetched successfully"));
    }
}