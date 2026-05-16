package com.ecommerce.dto.response;

public class AddressResponse {

    private Long id;
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private boolean defaultAddress;
    private Long userId;

    public AddressResponse() {
    }

    public AddressResponse(Long id, String line1, String line2, String city, String state,
                           String zipCode, String country, boolean defaultAddress,
                           Long userId) {
        this.id = id;
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.defaultAddress = defaultAddress;
        this.userId = userId;
    }

    public Long getId() {
        return id;
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

    public Long getUserId() {
        return userId;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}