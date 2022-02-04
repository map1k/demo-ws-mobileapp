package com.ciklum.demowsmobileapp.model;

import com.ciklum.demowsmobileapp.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.hateoas.RepresentationModel;

public class AddressRestModel extends RepresentationModel<AddressRestModel> {
    private String addressId;
    private String city;
    private String country;
    private String street;
    private String zipcode;
    private String type;
    @JsonBackReference
    private UserDto userDto;

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }
}
