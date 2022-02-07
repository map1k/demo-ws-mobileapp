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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
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
}
