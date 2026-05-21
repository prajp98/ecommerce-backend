package com.ecommerce.mapper;

import com.ecommerce.dto.response.ProductResponse;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.ProductImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mappings({
            @Mapping(source = "category.id", target = "categoryId"),
            @Mapping(source = "category.name", target = "categoryName"),
            @Mapping(source = "images", target = "primaryImageUrl")
    })
    ProductResponse toResponse(Product product);

    default String map(List<ProductImage> images) {
        if (images == null || images.isEmpty()) {
            return null;
        }

        return images.stream()
                .filter(ProductImage::isPrimaryImage)
                .findFirst()
                .orElse(images.get(0))
                .getImageUrl();
    }
}