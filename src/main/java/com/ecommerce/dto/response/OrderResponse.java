package com.ecommerce.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {

    private Long orderId;
    private String orderNumber;
    private String status;
    private BigDecimal totalAmount;
    private Long userId;
    private String userEmail;

    private Long addressId;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String zipCode;
    private String country;

    private String paymentMethod;
    private List<OrderItemResponse> orderItems;
    private LocalDateTime createdAt;

    public OrderResponse() {
    }

    public OrderResponse(Long orderId, String orderNumber, String status, BigDecimal totalAmount,
                         Long userId, String userEmail,
                         Long addressId, String addressLine1, String addressLine2, String city,
                         String state, String zipCode, String country,
                         String paymentMethod, List<OrderItemResponse> orderItems,
                         LocalDateTime createdAt) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.status = status;
        this.totalAmount = totalAmount;
        this.userId = userId;
        this.userEmail = userEmail;
        this.addressId = addressId;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.paymentMethod = paymentMethod;
        this.orderItems = orderItems;
        this.createdAt = createdAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Long getAddressId() {
        return addressId;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCountry() {
        return country;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public List<OrderItemResponse> getOrderItems() {
        return orderItems;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setOrderItems(List<OrderItemResponse> orderItems) {
        this.orderItems = orderItems;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}