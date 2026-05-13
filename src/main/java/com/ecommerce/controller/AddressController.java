package com.ecommerce.controller;

import com.ecommerce.dto.request.AddressRequest;
import com.ecommerce.dto.response.AddressResponse;
import com.ecommerce.service.AddressService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<AddressResponse> addAddress(@Valid @RequestBody AddressRequest request,
                                                      Authentication authentication) {
        String userEmail = authentication.getName();
        AddressResponse response = addressService.addAddress(userEmail, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponse> updateAddress(@PathVariable Long addressId,
                                                         @Valid @RequestBody AddressRequest request,
                                                         Authentication authentication) {
        String userEmail = authentication.getName();
        AddressResponse response = addressService.updateAddress(userEmail, addressId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<AddressResponse> deleteAddress(@PathVariable Long addressId,
                                                         Authentication authentication) {
        String userEmail = authentication.getName();
        AddressResponse response = addressService.deleteAddress(userEmail, addressId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<List<AddressResponse>> getMyAddresses(Authentication authentication) {
        String userEmail = authentication.getName();
        List<AddressResponse> response = addressService.getMyAddresses(userEmail);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{addressId}/default")
    public ResponseEntity<AddressResponse> setDefaultAddress(@PathVariable Long addressId,
                                                             Authentication authentication) {
        String userEmail = authentication.getName();
        AddressResponse response = addressService.setDefaultAddress(userEmail, addressId);
        return ResponseEntity.ok(response);
    }
}