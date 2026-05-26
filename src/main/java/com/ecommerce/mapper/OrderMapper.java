package com.ecommerce.mapper;

import com.ecommerce.dto.response.OrderResponse;
import com.ecommerce.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.email", target = "userEmail")
    @Mapping(source = "address.id", target = "addressId")
    @Mapping(source = "address.line1", target = "addressLine1")
    @Mapping(source = "address.line2", target = "addressLine2")
    @Mapping(source = "address.city", target = "city")
    @Mapping(source = "address.state", target = "state")
    @Mapping(source = "address.zipCode", target = "zipCode")
    @Mapping(source = "address.country", target = "country")
    OrderResponse toResponse(Order order);
}