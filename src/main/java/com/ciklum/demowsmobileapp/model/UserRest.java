package com.ciklum.demowsmobileapp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

public class UserRest {
    private String userId;
    private String firstName;
    private String email;
    @JsonManagedReference
    private List<AddressRestModel> addresses;

    public List<AddressRestModel> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<AddressRestModel> addresses) {
        this.addresses = addresses;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
