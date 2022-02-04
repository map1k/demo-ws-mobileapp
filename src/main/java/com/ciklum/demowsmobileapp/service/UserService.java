package com.ciklum.demowsmobileapp.service;

import com.ciklum.demowsmobileapp.dto.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);
    UserDto getUser(String email);
    UserDto getUserByUserId(String email);
    UserDto updateUser(String id, UserDto userDto);
    UserDto deleteUserByUserId(String id);
    List<UserDto> getUsers(int page, int limit);
}
