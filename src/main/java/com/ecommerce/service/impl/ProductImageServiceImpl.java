package com.ecommerce.service.impl;

import com.ecommerce.dto.request.ProductImageRequest;
import com.ecommerce.dto.response.ProductImageResponse;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.ProductImage;
import com.ecommerce.exception.ResourceNotFoundException;
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

    public ProductImageServiceImpl(ProductRepository productRepository,
                                   ProductImageRepository productImageRepository) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
    }

    @Override
    @Transactional
    public ProductImageResponse addImageToProduct(Long productId, ProductImageRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        ProductImage image = new ProductImage();
        image.setImageUrl(request.getImageUrl().trim());
        image.setPrimaryImage(request.isPrimaryImage());
        image.setProduct(product);

        if (request.isPrimaryImage()) {
            for (ProductImage existingImage : product.getImages()) {
                existingImage.setPrimaryImage(false);
            }
        }

        ProductImage savedImage = productImageRepository.save(image);

        ProductImageResponse response = new ProductImageResponse(
                savedImage.getId(),
                savedImage.getImageUrl(),
                savedImage.isPrimaryImage(),
                product.getId(),
                "Image added successfully"
        );

        return response;
    }

    @Override
    public List<ProductImageResponse> getImagesByProductId(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        List<ProductImage> images = productImageRepository.findByProductId(product.getId());

        return images.stream()
                .map(image -> new ProductImageResponse(
                        image.getId(),
                        image.getImageUrl(),
                        image.isPrimaryImage(),
                        product.getId(),
                        "Image fetched successfully"
                ))
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

        ProductImageResponse response = new ProductImageResponse(
                image.getId(),
                image.getImageUrl(),
                image.isPrimaryImage(),
                product.getId(),
                "Image deleted successfully"
        );

        return response;
    }
}