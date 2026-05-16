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
    public ResponseEntity<ApiResponse<CartItemResponse>> addToCart(
            @Valid @RequestBody AddToCartRequest request,
            Authentication authentication) {

        return buildResponse(
                cartService.addToCart(authentication.getName(), request),
                HttpStatus.OK,
                "Product added to cart successfully"
        );
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartItemResponse>> updateCartItem(
            @PathVariable Long cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request,
            Authentication authentication) {

        return buildResponse(
                cartService.updateCartItem(authentication.getName(), cartItemId, request),
                HttpStatus.OK,
                "Cart item updated successfully"
        );
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<ApiResponse<CartItemResponse>> removeCartItem(
            @PathVariable Long cartItemId,
            Authentication authentication) {

        return buildResponse(
                cartService.removeCartItem(authentication.getName(), cartItemId),
                HttpStatus.OK,
                "Cart item removed successfully"
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<CartItemResponse>>> getMyCart(
            Authentication authentication) {

        return buildResponse(
                cartService.getMyCart(authentication.getName()),
                HttpStatus.OK,
                "Cart fetched successfully"
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