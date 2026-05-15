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
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(@Valid @RequestBody PlaceOrderRequest request,
                                                                 Authentication authentication) {
        String userEmail = authentication.getName();
        OrderResponse response = orderService.placeOrder(userEmail, request);
        return buildResponse(response, HttpStatus.CREATED);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getMyOrders(Authentication authentication) {
        String userEmail = authentication.getName();
        List<OrderResponse> response = orderService.getMyOrders(userEmail);
        return buildResponse(response, HttpStatus.OK, "Orders fetched successfully");
    }

    @GetMapping("/me/page")
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getMyOrdersPaged(Authentication authentication,
                                                                             Pageable pageable) {
        String userEmail = authentication.getName();
        Page<OrderResponse> response = orderService.getMyOrders(userEmail, pageable);
        return buildResponse(response, HttpStatus.OK, "Orders fetched successfully", response);
    }

    @GetMapping("/me/status/{status}")
    public ResponseEntity<ApiResponse<Page<OrderResponse>>> getMyOrdersByStatus(Authentication authentication,
                                                                                @PathVariable String status,
                                                                                Pageable pageable) {
        String userEmail = authentication.getName();
        Page<OrderResponse> response = orderService.getMyOrdersByStatus(userEmail, status, pageable);
        return buildResponse(response, HttpStatus.OK, "Orders fetched successfully", response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Long orderId,
                                                                   Authentication authentication) {
        String userEmail = authentication.getName();
        OrderResponse response = orderService.getOrderById(userEmail, orderId);
        return buildResponse(response, HttpStatus.OK);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(@PathVariable Long orderId,
                                                                  Authentication authentication) {
        String userEmail = authentication.getName();
        OrderResponse response = orderService.cancelOrder(userEmail, orderId);
        return buildResponse(response, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {
        List<OrderResponse> response = orderService.getAllOrders();
        return buildResponse(response, HttpStatus.OK, "Orders fetched successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersByStatus(@PathVariable String status) {
        List<OrderResponse> response = orderService.getOrdersByStatus(status);
        return buildResponse(response, HttpStatus.OK, "Orders fetched successfully");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(@PathVariable Long orderId,
                                                                        @Valid @RequestBody UpdateOrderStatusRequest request) {
        OrderResponse response = orderService.updateOrderStatus(orderId, request);
        return buildResponse(response, HttpStatus.OK);
    }

    private ResponseEntity<ApiResponse<OrderResponse>> buildResponse(OrderResponse data, HttpStatus status) {
        ApiResponse<OrderResponse> apiResponse = new ApiResponse<>();
        apiResponse.setTimestamp(LocalDateTime.now());
        apiResponse.setStatus(status.value());
        apiResponse.setMessage(data.getMessage());
        apiResponse.setData(data);
        return ResponseEntity.status(status).body(apiResponse);
    }

    private ResponseEntity<ApiResponse<List<OrderResponse>>> buildResponse(List<OrderResponse> data,
                                                                           HttpStatus status,
                                                                           String message) {
        ApiResponse<List<OrderResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setTimestamp(LocalDateTime.now());
        apiResponse.setStatus(status.value());
        apiResponse.setMessage(message);
        apiResponse.setData(data);
        return ResponseEntity.status(status).body(apiResponse);
    }

    private ResponseEntity<ApiResponse<Page<OrderResponse>>> buildResponse(Page<OrderResponse> data,
                                                                           HttpStatus status,
                                                                           String message,
                                                                           Page<OrderResponse> pageData) {
        ApiResponse<Page<OrderResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setTimestamp(LocalDateTime.now());
        apiResponse.setStatus(status.value());
        apiResponse.setMessage(message);
        apiResponse.setData(pageData);
        return ResponseEntity.status(status).body(apiResponse);
    }
}