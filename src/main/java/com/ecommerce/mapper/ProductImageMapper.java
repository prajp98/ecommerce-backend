package com.ecommerce.mapper;

import com.ecommerce.dto.response.ProductImageResponse;
import com.ecommerce.entity.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductImageMapper {

    @Mapping(source = "product.id", target = "productId")
    ProductImageResponse toResponse(ProductImage productImage);
}