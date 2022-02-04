package com.ciklum.demowsmobileapp.service;

import com.ciklum.demowsmobileapp.dto.UserDto;
import com.ciklum.demowsmobileapp.entity.UserEntity;
import com.ciklum.demowsmobileapp.model.ErrorMessages;
import com.ciklum.demowsmobileapp.repository.UserRepository;
import org.hibernate.procedure.ParameterMisuseException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {
        ModelMapper modelMapper = new ModelMapper();
        userDto.getAddresses().forEach(address -> {
            address.setUser(userDto);
            address.setAddressId(UUID.randomUUID().toString());
        });
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);

        userEntity.setEncryptedPass(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userEntity.setUserId(UUID.randomUUID().toString());

        UserEntity old = userRepository.findByEmail(userEntity.getEmail());

        if (old != null) {
            throw new ParameterMisuseException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage());
        }
        UserEntity stored = userRepository.save(userEntity);

        return modelMapper.map(stored, UserDto.class);
    }

    @Override
    public UserDto getUser(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEntity, userDto);
        return userDto;
    }

    @Override
    public UserDto getUserByUserId(String id) {
        UserEntity userEntity = userRepository.findByUserId(id);
        if (userEntity == null) throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEntity, userDto);
        return userDto;
    }

    @Override
    public UserDto updateUser(String id, UserDto userDto) {
        UserEntity userEntity = userRepository.findByUserId(id);
        if (userEntity == null) throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());

        UserEntity stored = userRepository.save(userEntity);
        UserDto storedDto = new UserDto();
        BeanUtils.copyProperties(stored, storedDto);

        return storedDto;
    }

    @Override
    public UserDto deleteUserByUserId(String id) {
        UserEntity userEntity = userRepository.findByUserId(id);
        if (userEntity == null) throw new UsernameNotFoundException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        userRepository.deleteById(userEntity.getId());
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEntity, userDto);
        return userDto;
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        List<UserDto> userDtoList = new ArrayList<>();
        if (page > 0) page-= 1;
        Pageable pageReq = PageRequest.of(page, limit);
        Page<UserEntity> pageColl = userRepository.findAll(pageReq);
        List<UserEntity> userEntities = pageColl.getContent();

        for (UserEntity entity : userEntities) {
            UserDto dto = new UserDto();
            BeanUtils.copyProperties(entity, dto);
            userDtoList.add(dto);
        }
        return userDtoList;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(username);
        if (userEntity == null) throw new UsernameNotFoundException(ErrorMessages.EMAIL_ADDRESS_NOT_VERIFIED.getErrorMessage());
        return new User(userEntity.getEmail(), userEntity.getEncryptedPass(), new ArrayList<>());
    }
}
