package com.ecommerce.dto.response;

public class CategoryResponse {

    private Long id;
    private String name;
    private String description;
    private boolean active;
    private String message;

    public CategoryResponse() {
    }

    public CategoryResponse(Long id, String name, String description, boolean active, String message) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public String getMessage() {
        return message;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}