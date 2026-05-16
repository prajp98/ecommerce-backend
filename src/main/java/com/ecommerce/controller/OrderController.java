package com.ecommerce.controller;

import com.ecommerce.dto.request.PlaceOrderRequest;
import com.ecommerce.dto.request.UpdateOrderStatusRequest;
import com.ecommerce.dto.response.ApiResponse;
import com.ecommerce.dto.response.OrderResponse;
import com.ecommerce.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(
            @Valid @RequestBody PlaceOrderRequest request,
            Authentication authentication) {

        return buildResponse(
                orderService.placeOrder(authentication.getName(), request),
                HttpStatus.CREATED,
                "Order placed successfully"
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getMyOrders(
            Authentication authentication) {

        return buildResponse(
                orderService.getMyOrders(authentication.getName()),
                HttpStatus.OK,
                "Orders fetched successfully"
        );
    }

    @GetMapping("/me/page")
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getMyOrdersPaged(
            Authentication authentication,
            Pageable pageable) {

        return buildResponse(
                orderService.getMyOrders(authentication.getName(), pageable),
                HttpStatus.OK,
                "Orders fetched successfully"
        );
    }

    @GetMapping("/me/status/{status}")
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getMyOrdersByStatus(
            Authentication authentication,
            @PathVariable String status,
            Pageable pageable) {

        return buildResponse(
                orderService.getMyOrdersByStatus(authentication.getName(), status, pageable),
                HttpStatus.OK,
                "Orders fetched successfully"
        );
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(
            @PathVariable Long orderId,
            Authentication authentication) {

        return buildResponse(
                orderService.getOrderById(authentication.getName(), orderId),
                HttpStatus.OK,
                "Order fetched successfully"
        );
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @PathVariable Long orderId,
            Authentication authentication) {

        return buildResponse(
                orderService.cancelOrder(authentication.getName(), orderId),
                HttpStatus.OK,
                "Order cancelled successfully"
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {

        return buildResponse(
                orderService.getAllOrders(),
                HttpStatus.OK,
                "Orders fetched successfully"
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByStatus(
            @PathVariable String status) {

        return buildResponse(
                orderService.getOrdersByStatus(status),
                HttpStatus.OK,
                "Orders fetched successfully"
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request) {

        return buildResponse(
                orderService.updateOrderStatus(orderId, request),
                HttpStatus.OK,
                "Order status updated successfully"
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