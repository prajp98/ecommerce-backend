package com.ecommerce.service.impl;

import com.ecommerce.dto.request.ProductImageRequest;
import com.ecommerce.dto.response.ProductImageResponse;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.ProductImage;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.mapper.ProductImageMapper;
import com.ecommerce.repository.ProductImageRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.ProductImageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductImageMapper productImageMapper;

    public ProductImageServiceImpl(ProductRepository productRepository,
                                   ProductImageRepository productImageRepository,
                                   ProductImageMapper productImageMapper) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.productImageMapper = productImageMapper;
    }

    @Override
    @Transactional
    public ProductImageResponse addImageToProduct(Long productId, ProductImageRequest request) {
        Product product = getProductById(productId);

        ProductImage image = new ProductImage();
        image.setImageUrl(request.getImageUrl().trim());
        image.setPrimaryImage(request.isPrimaryImage());
        image.setProduct(product);

        if (request.isPrimaryImage()) {
            product.getImages()
                    .forEach(existingImage -> existingImage.setPrimaryImage(false));
        }

        ProductImage savedImage = productImageRepository.save(image);
        return buildResponse(savedImage, "Image added successfully");
    }

    @Override
    public List<ProductImageResponse> getImagesByProductId(Long productId) {
        Product product = getProductById(productId);

        return productImageRepository.findByProductId(product.getId())
                .stream()
                .map(image -> buildResponse(image, "Image fetched successfully"))
                .toList();
    }

    @Override
    @Transactional
    public ProductImageResponse deleteImage(Long imageId) {
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + imageId));

        Product product = image.getProduct();
        product.removeImage(image);
        productImageRepository.delete(image);

        return buildResponse(image, "Image deleted successfully");
    }

    private ProductImageResponse buildResponse(ProductImage image, String message) {
        ProductImageResponse response = productImageMapper.toResponse(image);
        response.setMessage(message);
        return response;
    }

    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
    }
}