package com.ecommerce.service;

import com.ecommerce.dto.request.PlaceOrderRequest;
import com.ecommerce.dto.request.UpdateOrderStatusRequest;
import com.ecommerce.dto.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse placeOrder(String userEmail, PlaceOrderRequest request);

    List<OrderResponse> getMyOrders(String userEmail);

    OrderResponse getOrderById(String userEmail, Long orderId);

    List<OrderResponse> getAllOrders();

    List<OrderResponse> getOrdersByStatus(String status);

    OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request);

    OrderResponse cancelOrder(String userEmail, Long orderId);
}