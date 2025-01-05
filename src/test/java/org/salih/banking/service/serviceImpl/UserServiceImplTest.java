package org.salih.banking.service.serviceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.salih.banking.entitiy.User;
import org.salih.banking.repositories.UserRepository;
import org.salih.banking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
    }

    @Test
    void addUser() {
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(Mockito.mock(User.class));

        userService.addUser(new User());

        verify(userRepository, times(1)).save(new User());
    }

    @Test
    void listUsers() {
        Mockito.when(userRepository.findAll()).thenReturn(Arrays.asList(new User(), new User()));

        List<User> users =  userService.listUsers();

        assertEquals(users.size(), 2);
    }
}