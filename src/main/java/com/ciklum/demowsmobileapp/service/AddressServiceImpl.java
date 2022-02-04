package com.ciklum.demowsmobileapp.service;

import com.ciklum.demowsmobileapp.dto.AddressDto;
import com.ciklum.demowsmobileapp.entity.AddressEntity;
import com.ciklum.demowsmobileapp.entity.UserEntity;
import com.ciklum.demowsmobileapp.model.ErrorMessages;
import com.ciklum.demowsmobileapp.repository.AddressRepository;
import com.ciklum.demowsmobileapp.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    UserRepository userRepository;


    @Autowired
    AddressRepository addressRepository;

    @Override
    public List<AddressEntity> getUserAddresses(String userId) {

        UserEntity userEntity = userRepository.findByUserId(userId);
        List<AddressEntity> addressDtos = addressRepository.findAllByUserId(userEntity.getId());
        if (userEntity == null) throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        return addressDtos;
    }

    @Override
    public AddressDto getUserAddressByAddressId(String addressId) {
        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
        if (addressEntity == null) throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(addressEntity, AddressDto.class);
    }
}
