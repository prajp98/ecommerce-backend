package com.ecommerce.mapper;

import com.ecommerce.dto.response.AddressResponse;
import com.ecommerce.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(source = "user.id", target = "userId")
    AddressResponse toResponse(Address address);
}