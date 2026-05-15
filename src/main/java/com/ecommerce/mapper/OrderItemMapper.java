package com.ecommerce.mapper;

import com.ecommerce.dto.response.OrderItemResponse;
import com.ecommerce.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    @Mapping(source = "id", target = "orderItemId")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(target = "totalPrice", ignore = true)
    OrderItemResponse toResponse(OrderItem orderItem);
}