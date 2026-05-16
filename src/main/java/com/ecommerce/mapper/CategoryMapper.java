package com.ecommerce.mapper;

import com.ecommerce.dto.request.CategoryRequest;
import com.ecommerce.dto.response.CategoryResponse;
import com.ecommerce.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "active", ignore = true)
    })
    Category toEntity(CategoryRequest request);

    CategoryResponse toResponse(Category category);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "active", ignore = true)
    })
    void updateEntityFromRequest(CategoryRequest request, @MappingTarget Category category);
}