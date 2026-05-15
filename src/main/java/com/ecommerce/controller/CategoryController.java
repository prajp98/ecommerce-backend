package com.ecommerce.controller;

import com.ecommerce.dto.request.CategoryRequest;
import com.ecommerce.dto.response.ApiResponse;
import com.ecommerce.dto.response.CategoryResponse;
import com.ecommerce.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        return buildResponse(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategory(@PathVariable Long id,
                                                                        @Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.updateCategory(id, request);
        return buildResponse(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(@PathVariable Long id) {
        CategoryResponse response = categoryService.getCategoryById(id);
        return buildResponse(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> response = categoryService.getAllCategories();
        return buildResponse(response, HttpStatus.OK, "Categories fetched successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ApiResponse<CategoryResponse>> deactivateCategory(@PathVariable Long id) {
        CategoryResponse response = categoryService.deactivateCategory(id);
        return buildResponse(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ApiResponse<CategoryResponse>> activateCategory(@PathVariable Long id) {
        CategoryResponse response = categoryService.activateCategory(id);
        return buildResponse(response, HttpStatus.OK);
    }

    private ResponseEntity<ApiResponse<CategoryResponse>> buildResponse(CategoryResponse data, HttpStatus status) {
        ApiResponse<CategoryResponse> apiResponse = new ApiResponse<>();
        apiResponse.setTimestamp(LocalDateTime.now());
        apiResponse.setStatus(status.value());
        apiResponse.setMessage(data.getMessage());
        apiResponse.setData(data);
        return ResponseEntity.status(status).body(apiResponse);
    }

    private ResponseEntity<ApiResponse<List<CategoryResponse>>> buildResponse(List<CategoryResponse> data,
                                                                              HttpStatus status,
                                                                              String message) {
        ApiResponse<List<CategoryResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setTimestamp(LocalDateTime.now());
        apiResponse.setStatus(status.value());
        apiResponse.setMessage(message);
        apiResponse.setData(data);
        return ResponseEntity.status(status).body(apiResponse);
    }
}