package com.ecommerce.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AddressRequest {

    @NotBlank(message = "Line 1 is required")
    @Size(max = 200, message = "Line 1 must be at most 200 characters")
    private String line1;

    @Size(max = 200, message = "Line 2 must be at most 200 characters")
    private String line2;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City must be at most 100 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 100, message = "State must be at most 100 characters")
    private String state;

    @NotBlank(message = "Zip code is required")
    @Size(max = 20, message = "Zip code must be at most 20 characters")
    private String zipCode;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country must be at most 100 characters")
    private String country;

    private boolean defaultAddress;

    public AddressRequest() {
    }

    public AddressRequest(String line1, String line2, String city, String state, String zipCode,
                          String country, boolean defaultAddress) {
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.defaultAddress = defaultAddress;
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
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

    public boolean isDefaultAddress() {
        return defaultAddress;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
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

    public void setDefaultAddress(boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
    }
}