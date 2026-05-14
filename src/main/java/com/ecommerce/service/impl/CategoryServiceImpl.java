package com.ecommerce.service.impl;

import com.ecommerce.dto.request.CategoryRequest;
import com.ecommerce.dto.response.CategoryResponse;
import com.ecommerce.entity.Category;
import com.ecommerce.exception.DuplicateResourceException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.mapper.CategoryMapper;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryMapper categoryMapper,
                               CategoryRepository categoryRepository) {
        this.categoryMapper = categoryMapper;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CategoryRequest request) {
        String name = request.getName().trim();

        boolean exists = categoryRepository.existsByName(name);
        if (exists) {
            throw new DuplicateResourceException("Category already exists");
        }

        Category category = new Category();
        category.setName(name);
        category.setDescription(request.getDescription());
        category.setActive(true);

        Category savedCategory = categoryRepository.save(category);

        CategoryResponse response = categoryMapper.toResponse(savedCategory);
        response.setMessage("Category created successfully");
        return response;
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        String newName = request.getName().trim();

        if (!category.getName().equalsIgnoreCase(newName) && categoryRepository.existsByName(newName)) {
            throw new DuplicateResourceException("Category already exists");
        }

        category.setName(newName);
        category.setDescription(request.getDescription());

        Category updatedCategory = categoryRepository.save(category);
        CategoryResponse response = categoryMapper.toResponse(updatedCategory);
        response.setMessage("Category updated successfully");
        return response;
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        CategoryResponse response = categoryMapper.toResponse(category);
        response.setMessage("Category fetched successfully");
        return response;
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> {
                    CategoryResponse response = categoryMapper.toResponse(category);
                    response.setMessage("Category fetched successfully");
                    return response;
                })
                .toList();
    }

    @Override
    @Transactional
    public CategoryResponse deactivateCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        category.setActive(false);
        Category savedCategory = categoryRepository.save(category);

        CategoryResponse response = categoryMapper.toResponse(savedCategory);
        response.setMessage("Category deactivated successfully");
        return response;
    }

    @Override
    @Transactional
    public CategoryResponse activateCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));

        category.setActive(true);
        Category savedCategory = categoryRepository.save(category);

        CategoryResponse response = categoryMapper.toResponse(savedCategory);
        response.setMessage("Category activated successfully");
        return response;
    }
}