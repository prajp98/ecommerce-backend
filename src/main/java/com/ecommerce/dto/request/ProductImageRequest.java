package com.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ProductImageRequest {

    @NotBlank(message = "Image URL is required")
    @Size(max = 500, message = "Image URL must be at most 500 characters")
    private String imageUrl;

    private boolean primaryImage;

    public ProductImageRequest() {
    }

    public ProductImageRequest(String imageUrl, boolean primaryImage) {
        this.imageUrl = imageUrl;
        this.primaryImage = primaryImage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isPrimaryImage() {
        return primaryImage;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPrimaryImage(boolean primaryImage) {
        this.primaryImage = primaryImage;
    }
}