package com.ecommerce.dto.response;

public class ProductImageResponse {

    private Long id;
    private String imageUrl;
    private boolean primaryImage;
    private Long productId;

    public ProductImageResponse() {
    }

    public ProductImageResponse(Long id, String imageUrl, boolean primaryImage, Long productId) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.primaryImage = primaryImage;
        this.productId = productId;
    }

    public Long getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isPrimaryImage() {
        return primaryImage;
    }

    public Long getProductId() {
        return productId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPrimaryImage(boolean primaryImage) {
        this.primaryImage = primaryImage;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}