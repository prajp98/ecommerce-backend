package com.ecommerce.mapper;

import com.ecommerce.dto.response.ProductResponse;
import com.ecommerce.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mappings({
            @Mapping(source = "category.id", target = "categoryId"),
            @Mapping(source = "category.name", target = "categoryName")
    })
    ProductResponse toResponse(Product product);
}