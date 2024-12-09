package com.example.menumaker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class UserDto {
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Mobile number is required")
    @Size(min = 10, max = 15, message = "Mobile number must be between 10 and 15 characters")
    private String mobile;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Shop name is required")
    private String shopName;

    @NotBlank(message = "Shop address is required")
    private String shopAddress;

    // Default Constructor
    public UserDto() {
    }

    // Parameterized Constructor
    public UserDto(String email, String mobile, String password, String shopName, String shopAddress) {
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.shopName = shopName;
        this.shopAddress = shopAddress;
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    // toString Method
    @Override
    public String toString() {
        return "UserDto{" +
                "email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", password='" + password + '\'' +
                ", shopName='" + shopName + '\'' +
                ", shopAddress='" + shopAddress + '\'' +
                '}';
    }

    // equals Method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(email, userDto.email) &&
                Objects.equals(mobile, userDto.mobile) &&
                Objects.equals(password, userDto.password) &&
                Objects.equals(shopName, userDto.shopName) &&
                Objects.equals(shopAddress, userDto.shopAddress);
    }

    // hashCode Method
    @Override
    public int hashCode() {
        return Objects.hash(email, mobile, password, shopName, shopAddress);
    }
}
