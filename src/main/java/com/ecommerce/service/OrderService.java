package com.ecommerce.service;

import com.ecommerce.dto.request.PlaceOrderRequest;
import com.ecommerce.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse placeOrder(String userEmail, PlaceOrderRequest request);

    List<OrderResponse> getMyOrders(String userEmail);

    OrderResponse getOrderById(String userEmail, Long orderId);
}