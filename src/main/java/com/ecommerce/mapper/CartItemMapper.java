package com.ecommerce.mapper;

import com.ecommerce.dto.response.CartItemResponse;
import com.ecommerce.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(source = "id", target = "cartItemId")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price", target = "price")
    @Mapping(target = "totalPrice", ignore = true)
    CartItemResponse toResponse(CartItem cartItem);
}