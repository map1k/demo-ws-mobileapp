package com.ciklum.demowsmobileapp;

import com.ciklum.demowsmobileapp.dto.UserDto;
import com.ciklum.demowsmobileapp.entity.UserEntity;
import com.ciklum.demowsmobileapp.repository.UserRepository;
import com.ciklum.demowsmobileapp.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    public static final String MARAT_EMAIL_COM = "marat@email.com";
    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    final void testGetUser() {
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setFirstName("Marat");
        entity.setLastName("Kra");
        entity.setUserId("Adfsf213Adf");
        entity.setEncryptedPass("23fAfega42@!f");
        when(userRepository.findByEmail(anyString())).thenReturn(entity);

        UserDto userDto = userService.getUser("email");
        assertNotNull(userDto);
        assertEquals("Marat", userDto.getFirstName());
        assertEquals("Kra", userDto.getLastName());
        assertEquals("Adfsf213Adf", userDto.getUserId());
        assertEquals("23fAfega42@!f", userDto.getEncryptedPass());
    }

    @Test
    final void testGetUser_UserNotFoundException()
    {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class,
                ()-> userService.getUser("marat@email.com")
                );
    }

    @Test
    final void testCreateUser_UserExistException()
    {
        UserEntity entity = new UserEntity();
        when(userRepository.findByEmail(anyString())).thenReturn(entity);

        UserDto userDto = new UserDto();
        userDto.setEmail("marat@email.com");

        assertThrows(RuntimeException.class,
                ()-> userService.createUser(userDto)
        );
    }

    @Test
    final void testCreateUser()
    {
        UserEntity entity = new UserEntity();
        entity.setEmail(MARAT_EMAIL_COM);

        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(userRepository.save(any(UserEntity.class))).thenReturn(entity);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("passSdfa322!!");

        UserDto userDto = new UserDto();
        userDto.setEmail(MARAT_EMAIL_COM);
        userDto.setPassword("asdas");
        userDto.setAddresses(new ArrayList<>());

        UserDto savedUserDto = userService.createUser(userDto);

        assertNotNull(savedUserDto);
        assertEquals(entity.getEmail(), savedUserDto.getEmail());
    }

}
