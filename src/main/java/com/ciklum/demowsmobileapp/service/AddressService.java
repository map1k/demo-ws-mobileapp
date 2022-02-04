package com.ciklum.demowsmobileapp.service;

import com.ciklum.demowsmobileapp.dto.AddressDto;
import com.ciklum.demowsmobileapp.entity.AddressEntity;

import java.util.List;

public interface AddressService {
    List<AddressEntity> getUserAddresses(String userId);
    AddressDto getUserAddressByAddressId(String addressId);
}
