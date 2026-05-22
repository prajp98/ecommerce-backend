package com.ecommerce.service.impl;

import com.ecommerce.dto.response.ProductImageResponse;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.ProductImage;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.mapper.ProductImageMapper;
import com.ecommerce.repository.ProductImageRepository;
import com.ecommerce.repository.ProductRepository;
import com.ecommerce.service.ProductImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductImageMapper productImageMapper;
    private final Path uploadRoot;

    public ProductImageServiceImpl(ProductRepository productRepository,
                                   ProductImageRepository productImageRepository,
                                   ProductImageMapper productImageMapper,
                                   @Value("${app.upload-dir:uploads}") String uploadDir) {
        this.productRepository = productRepository;
        this.productImageRepository = productImageRepository;
        this.productImageMapper = productImageMapper;
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @Override
    @Transactional
    public ProductImageResponse addImageToProduct(Long productId, MultipartFile file, boolean primaryImage) {
        Product product = getProductById(productId);

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file is required");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }

        String imageUrl = storeFile(productId, file);

        if (primaryImage) {
            productImageRepository.findByProductId(productId)
                    .forEach(existingImage -> existingImage.setPrimaryImage(false));
        }

        ProductImage image = new ProductImage();
        image.setImageUrl(imageUrl);
        image.setPrimaryImage(primaryImage);
        image.setProduct(product);

        ProductImage savedImage = productImageRepository.save(image);
        return productImageMapper.toResponse(savedImage);
    }

    @Override
    public List<ProductImageResponse> getImagesByProductId(Long productId) {
        Product product = getProductById(productId);

        return productImageRepository.findByProductId(product.getId())
                .stream()
                .map(productImageMapper::toResponse)
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

        return productImageMapper.toResponse(image);
    }

    @Override
    @Transactional
    public ProductImageResponse setPrimaryImage(Long imageId) {
        ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with id: " + imageId));

        Product product = image.getProduct();

        productImageRepository.findByProductId(product.getId())
                .forEach(existingImage -> existingImage.setPrimaryImage(false));

        image.setPrimaryImage(true);
        ProductImage savedImage = productImageRepository.save(image);

        return productImageMapper.toResponse(savedImage);
    }

    private Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
    }

    private String storeFile(Long productId, MultipartFile file) {
        try {
            Files.createDirectories(uploadRoot);

            Path productDir = uploadRoot.resolve("product-images").resolve(String.valueOf(productId));
            Files.createDirectories(productDir);

            String originalName = StringUtils.cleanPath(
                    Objects.requireNonNullElse(file.getOriginalFilename(), "image")
            );

            String extension = "";
            int lastDot = originalName.lastIndexOf('.');
            if (lastDot > -1 && lastDot < originalName.length() - 1) {
                extension = originalName.substring(lastDot);
            }

            String fileName = UUID.randomUUID() + extension;
            Path destination = productDir.resolve(fileName).normalize();

            Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

            return "/uploads/product-images/" + productId + "/" + fileName;
        } catch (IOException ex) {
            throw new RuntimeException("Failed to store image file", ex);
        }
    }
}