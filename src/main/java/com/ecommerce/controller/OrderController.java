package com.ecommerce.controller;

import com.ecommerce.dto.request.PlaceOrderRequest;
import com.ecommerce.dto.request.UpdateOrderStatusRequest;
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

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody PlaceOrderRequest request,
                                                    Authentication authentication) {
        String userEmail = authentication.getName();
        OrderResponse response = orderService.placeOrder(userEmail, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<List<OrderResponse>> getMyOrders(Authentication authentication) {
        String userEmail = authentication.getName();
        List<OrderResponse> response = orderService.getMyOrders(userEmail);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long orderId,
                                                      Authentication authentication) {
        String userEmail = authentication.getName();
        OrderResponse response = orderService.getOrderById(userEmail, orderId);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> response = orderService.getAllOrders();
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(@PathVariable String status) {
        List<OrderResponse> response = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(@PathVariable Long orderId,
                                                           @Valid @RequestBody UpdateOrderStatusRequest request) {
        OrderResponse response = orderService.updateOrderStatus(orderId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable Long orderId,
                                                     Authentication authentication) {
        String userEmail = authentication.getName();
        OrderResponse response = orderService.cancelOrder(userEmail, orderId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/page")
    public ResponseEntity<Page<OrderResponse>> getMyOrdersPaged(Authentication authentication,
                                                                Pageable pageable) {
        String userEmail = authentication.getName();
        Page<OrderResponse> response = orderService.getMyOrders(userEmail, pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me/status/{status}")
    public ResponseEntity<Page<OrderResponse>> getMyOrdersByStatus(Authentication authentication,
                                                                   @PathVariable String status,
                                                                   Pageable pageable) {
        String userEmail = authentication.getName();
        Page<OrderResponse> response = orderService.getMyOrdersByStatus(userEmail, status, pageable);
        return ResponseEntity.ok(response);
    }
}