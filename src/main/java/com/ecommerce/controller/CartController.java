package com.ecommerce.controller;

import com.ecommerce.dto.request.AddToCartRequest;
import com.ecommerce.dto.request.UpdateCartItemRequest;
import com.ecommerce.dto.response.CartItemResponse;
import com.ecommerce.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/items")
    public ResponseEntity<CartItemResponse> addToCart(@Valid @RequestBody AddToCartRequest request,
                                                      Authentication authentication) {
        String userEmail = authentication.getName();
        CartItemResponse response = cartService.addToCart(userEmail, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartItemResponse> updateCartItem(@PathVariable Long cartItemId,
                                                           @Valid @RequestBody UpdateCartItemRequest request,
                                                           Authentication authentication) {
        String userEmail = authentication.getName();
        CartItemResponse response = cartService.updateCartItem(userEmail, cartItemId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<CartItemResponse> removeCartItem(@PathVariable Long cartItemId,
                                                           Authentication authentication) {
        String userEmail = authentication.getName();
        CartItemResponse response = cartService.removeCartItem(userEmail, cartItemId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<List<CartItemResponse>> getMyCart(Authentication authentication) {
        String userEmail = authentication.getName();
        List<CartItemResponse> response = cartService.getMyCart(userEmail);
        return ResponseEntity.ok(response);
    }
}