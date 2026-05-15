package com.ecommerce.controller;

import com.ecommerce.dto.request.AddToCartRequest;
import com.ecommerce.dto.request.UpdateCartItemRequest;
import com.ecommerce.dto.response.ApiResponse;
import com.ecommerce.dto.response.CartItemResponse;
import com.ecommerce.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartItemResponse>> addToCart(@Valid @RequestBody AddToCartRequest request,
                                                                   Authentication authentication) {
        String userEmail = authentication.getName();
        CartItemResponse response = cartService.addToCart(userEmail, request);
        return buildResponse(response, HttpStatus.OK);
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartItemResponse>> updateCartItem(@PathVariable Long cartItemId,
                                                                        @Valid @RequestBody UpdateCartItemRequest request,
                                                                        Authentication authentication) {
        String userEmail = authentication.getName();
        CartItemResponse response = cartService.updateCartItem(userEmail, cartItemId, request);
        return buildResponse(response, HttpStatus.OK);
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartItemResponse>> removeCartItem(@PathVariable Long cartItemId,
                                                                        Authentication authentication) {
        String userEmail = authentication.getName();
        CartItemResponse response = cartService.removeCartItem(userEmail, cartItemId);
        return buildResponse(response, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<CartItemResponse>>> getMyCart(Authentication authentication) {
        String userEmail = authentication.getName();
        List<CartItemResponse> response = cartService.getMyCart(userEmail);
        return buildResponse(response, HttpStatus.OK, "Cart fetched successfully");
    }

    private ResponseEntity<ApiResponse<CartItemResponse>> buildResponse(CartItemResponse data, HttpStatus status) {
        ApiResponse<CartItemResponse> apiResponse = new ApiResponse<>();
        apiResponse.setTimestamp(LocalDateTime.now());
        apiResponse.setStatus(status.value());
        apiResponse.setMessage(data.getMessage());
        apiResponse.setData(data);
        return ResponseEntity.status(status).body(apiResponse);
    }

    private ResponseEntity<ApiResponse<List<CartItemResponse>>> buildResponse(List<CartItemResponse> data,
                                                                              HttpStatus status,
                                                                              String message) {
        ApiResponse<List<CartItemResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setTimestamp(LocalDateTime.now());
        apiResponse.setStatus(status.value());
        apiResponse.setMessage(message);
        apiResponse.setData(data);
        return ResponseEntity.status(status).body(apiResponse);
    }
}