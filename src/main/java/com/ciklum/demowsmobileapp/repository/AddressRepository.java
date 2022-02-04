package com.ciklum.demowsmobileapp.repository;

import com.ciklum.demowsmobileapp.entity.AddressEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
    List<AddressEntity> findAllByUserId(Long userId);
    AddressEntity findByAddressId(String addressId);
}
