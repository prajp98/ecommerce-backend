package com.ecommerce.service.impl;

import com.ecommerce.dto.request.PlaceOrderRequest;
import com.ecommerce.dto.request.UpdateOrderStatusRequest;
import com.ecommerce.dto.response.OrderItemResponse;
import com.ecommerce.dto.response.OrderResponse;
import com.ecommerce.entity.*;
import com.ecommerce.exception.EmptyCartException;
import com.ecommerce.exception.ForbiddenOperationException;
import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.repository.*;
import com.ecommerce.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            UserRepository userRepository,
                            CartItemRepository cartItemRepository,
                            ProductRepository productRepository,
                            AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    @Transactional
    public OrderResponse placeOrder(String userEmail, PlaceOrderRequest request) {
        User user = getUserByEmail(userEmail);
        List<CartItem> cartItems = cartItemRepository.findByUserId(user.getId());

        if (cartItems.isEmpty()) {
            throw new EmptyCartException("Cart is empty");
        }

        Address address = addressRepository.findById(request.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Address not found with id: " + request.getAddressId()
                ));

        if (!address.getUser().getId().equals(user.getId())) {
            throw new ForbiddenOperationException("You cannot use another user's address");
        }

        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setUser(user);
        order.setAddress(address);
        order.setStatus(OrderStatus.PENDING);
        order.setPaymentMethod(request.getPaymentMethod() == null ? null : request.getPaymentMethod().trim());

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            if (!product.isActive()) {
                throw new ForbiddenOperationException("Product is not active: " + product.getName());
            }

            if (cartItem.getQuantity() > product.getStock()) {
                throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
            }

            BigDecimal itemTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());
            order.addOrderItem(orderItem);
        }

        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        cartItemRepository.deleteAll(cartItems);

        return toResponse(savedOrder, "Order placed successfully");
    }

    @Override
    public List<OrderResponse> getMyOrders(String userEmail) {
        User user = getUserByEmail(userEmail);
        List<Order> orders = orderRepository.findByUserId(user.getId());

        return orders.stream()
                .map(order -> toResponse(order, "Order fetched successfully"))
                .toList();
    }

    @Override
    public OrderResponse getOrderById(String userEmail, Long orderId) {
        User user = getUserByEmail(userEmail);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new ForbiddenOperationException("You cannot access another user's order");
        }

        return toResponse(order, "Order fetched successfully");
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(order -> toResponse(order, "Order fetched successfully"))
                .toList();
    }

    @Override
    public List<OrderResponse> getOrdersByStatus(String status) {
        OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
        return orderRepository.findByStatus(orderStatus)
                .stream()
                .map(order -> toResponse(order, "Order fetched successfully"))
                .toList();
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long orderId, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        order.setStatus(request.getStatus());
        Order savedOrder = orderRepository.save(order);

        return toResponse(savedOrder, "Order status updated successfully");
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private String generateOrderNumber() {
        String datePart = LocalDate.now().toString().replace("-", "");
        String uniquePart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "ORD-" + datePart + "-" + uniquePart;
    }

    private OrderResponse toResponse(Order order, String message) {
        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(item -> {
                    BigDecimal totalPrice = item.getPriceAtPurchase()
                            .multiply(BigDecimal.valueOf(item.getQuantity()));

                    return new OrderItemResponse(
                            item.getId(),
                            item.getProduct().getId(),
                            item.getProduct().getName(),
                            item.getQuantity(),
                            item.getPriceAtPurchase(),
                            totalPrice
                    );
                })
                .toList();

        Address address = order.getAddress();

        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId());
        response.setOrderNumber(order.getOrderNumber());
        response.setStatus(order.getStatus().name());
        response.setTotalAmount(order.getTotalAmount());
        response.setUserId(order.getUser().getId());
        response.setUserEmail(order.getUser().getEmail());
        response.setAddressId(address.getId());
        response.setAddressLine1(address.getLine1());
        response.setAddressLine2(address.getLine2());
        response.setCity(address.getCity());
        response.setState(address.getState());
        response.setZipCode(address.getZipCode());
        response.setCountry(address.getCountry());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setOrderItems(itemResponses);
        response.setCreatedAt(order.getCreatedAt());
        response.setMessage(message);
        return response;
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(String userEmail, Long orderId) {
        User user = getUserByEmail(userEmail);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new ForbiddenOperationException("You cannot cancel another user's order");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new ForbiddenOperationException("Only pending orders can be cancelled");
        }

        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() + item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(order);

        return toResponse(savedOrder, "Order cancelled successfully");
    }
}