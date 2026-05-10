package com.ecommerce.dto.response;

public class ProductImageResponse {

    private Long id;
    private String imageUrl;
    private boolean primaryImage;
    private Long productId;
    private String message;

    public ProductImageResponse() {
    }

    public ProductImageResponse(Long id, String imageUrl, boolean primaryImage, Long productId, String message) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.primaryImage = primaryImage;
        this.productId = productId;
        this.message = message;
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

    public String getMessage() {
        return message;
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

    public void setMessage(String message) {
        this.message = message;
    }
}