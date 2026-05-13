package com.ecommerce.service;

import com.ecommerce.dto.request.AddressRequest;
import com.ecommerce.dto.response.AddressResponse;

import java.util.List;

public interface AddressService {

    AddressResponse addAddress(String userEmail, AddressRequest request);

    AddressResponse updateAddress(String userEmail, Long addressId, AddressRequest request);

    AddressResponse deleteAddress(String userEmail, Long addressId);

    List<AddressResponse> getMyAddresses(String userEmail);

    AddressResponse setDefaultAddress(String userEmail, Long addressId);
}