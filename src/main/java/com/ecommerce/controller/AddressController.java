package com.ecommerce.controller;

import com.ecommerce.dto.request.AddressRequest;
import com.ecommerce.dto.response.AddressResponse;
import com.ecommerce.dto.response.ApiResponse;
import com.ecommerce.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponse>> addAddress(
            @Valid @RequestBody AddressRequest request,
            Authentication authentication) {

        return buildResponse(
                addressService.addAddress(authentication.getName(), request),
                HttpStatus.CREATED,
                "Address added successfully"
        );
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(
            @PathVariable Long addressId,
            @Valid @RequestBody AddressRequest request,
            Authentication authentication) {

        return buildResponse(
                addressService.updateAddress(authentication.getName(), addressId, request),
                HttpStatus.OK,
                "Address updated successfully"
        );
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponse>> deleteAddress(
            @PathVariable Long addressId,
            Authentication authentication) {

        return buildResponse(
                addressService.deleteAddress(authentication.getName(), addressId),
                HttpStatus.OK,
                "Address deleted successfully"
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getMyAddresses(
            Authentication authentication) {

        return buildResponse(
                addressService.getMyAddresses(authentication.getName()),
                HttpStatus.OK,
                "Addresses fetched successfully"
        );
    }

    @PatchMapping("/{addressId}/default")
    public ResponseEntity<ApiResponse<AddressResponse>> setDefaultAddress(
            @PathVariable Long addressId,
            Authentication authentication) {

        return buildResponse(
                addressService.setDefaultAddress(authentication.getName(), addressId),
                HttpStatus.OK,
                "Default address updated successfully"
        );
    }

    private <T> ResponseEntity<ApiResponse<T>> buildResponse(
            T data,
            HttpStatus status,
            String message) {

        ApiResponse<T> apiResponse = new ApiResponse<>();
        apiResponse.setTimestamp(LocalDateTime.now());
        apiResponse.setStatus(status.value());
        apiResponse.setMessage(message);
        apiResponse.setData(data);

        return ResponseEntity.status(status).body(apiResponse);
    }
}