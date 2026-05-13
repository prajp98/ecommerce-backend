package com.ecommerce.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PlaceOrderRequest {

    @NotNull(message = "Address ID is required")
    private Long addressId;

    @Size(max = 200, message = "Payment method must be at most 200 characters")
    private String paymentMethod;

    public PlaceOrderRequest() {
    }

    public PlaceOrderRequest(Long addressId, String paymentMethod) {
        this.addressId = addressId;
        this.paymentMethod = paymentMethod;
    }

    public Long getAddressId() {
        return addressId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}