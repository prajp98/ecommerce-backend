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
    public ResponseEntity<ApiResponse<AddressResponse>> addAddress(@Valid @RequestBody AddressRequest request,
                                                                   Authentication authentication) {
        String userEmail = authentication.getName();
        AddressResponse response = addressService.addAddress(userEmail, request);
        return buildResponse(response, HttpStatus.CREATED);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponse>> updateAddress(@PathVariable Long addressId,
                                                                      @Valid @RequestBody AddressRequest request,
                                                                      Authentication authentication) {
        String userEmail = authentication.getName();
        AddressResponse response = addressService.updateAddress(userEmail, addressId, request);
        return buildResponse(response, HttpStatus.OK);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<ApiResponse<AddressResponse>> deleteAddress(@PathVariable Long addressId,
                                                                      Authentication authentication) {
        String userEmail = authentication.getName();
        AddressResponse response = addressService.deleteAddress(userEmail, addressId);
        return buildResponse(response, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getMyAddresses(Authentication authentication) {
        String userEmail = authentication.getName();
        List<AddressResponse> response = addressService.getMyAddresses(userEmail);
        return buildResponse(response, HttpStatus.OK, "Addresses fetched successfully");
    }

    @PatchMapping("/{addressId}/default")
    public ResponseEntity<ApiResponse<AddressResponse>> setDefaultAddress(@PathVariable Long addressId,
                                                                          Authentication authentication) {
        String userEmail = authentication.getName();
        AddressResponse response = addressService.setDefaultAddress(userEmail, addressId);
        return buildResponse(response, HttpStatus.OK);
    }

    private ResponseEntity<ApiResponse<AddressResponse>> buildResponse(AddressResponse data, HttpStatus status) {
        ApiResponse<AddressResponse> apiResponse = new ApiResponse<>();
        apiResponse.setTimestamp(LocalDateTime.now());
        apiResponse.setStatus(status.value());
        apiResponse.setMessage(data.getMessage());
        apiResponse.setData(data);
        return ResponseEntity.status(status).body(apiResponse);
    }

    private ResponseEntity<ApiResponse<List<AddressResponse>>> buildResponse(List<AddressResponse> data,
                                                                             HttpStatus status,
                                                                             String message) {
        ApiResponse<List<AddressResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setTimestamp(LocalDateTime.now());
        apiResponse.setStatus(status.value());
        apiResponse.setMessage(message);
        apiResponse.setData(data);
        return ResponseEntity.status(status).body(apiResponse);
    }
}