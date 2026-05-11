package com.ecommerce.service;

import com.ecommerce.dto.request.AddToCartRequest;
import com.ecommerce.dto.request.UpdateCartItemRequest;
import com.ecommerce.dto.response.CartItemResponse;

import java.util.List;

public interface CartService {

    CartItemResponse addToCart(String userEmail, AddToCartRequest request);

    CartItemResponse updateCartItem(String userEmail, Long cartItemId, UpdateCartItemRequest request);

    CartItemResponse removeCartItem(String userEmail, Long cartItemId);

    List<CartItemResponse> getMyCart(String userEmail);
}