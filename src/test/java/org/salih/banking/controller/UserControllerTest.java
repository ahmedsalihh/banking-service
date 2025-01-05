package org.salih.banking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.salih.banking.entitiy.User;
import org.salih.banking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void listUsers() throws Exception {
        List<User> users = new ArrayList<>();
        User u = new User();
        u.setFirstname("test");
        u.setLastname("test");
        users.add(u);

        when(userService.listUsers()).thenReturn(users);

        mockMvc.perform(get("/users/list").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void addUser() throws Exception {
        User u = new User();
        u.setFirstname("test");
        u.setLastname("test");
        u.setUsedCreditLimit(BigDecimal.valueOf(10000));
        u.setUsedCreditLimit(BigDecimal.ZERO);

        when(userService.addUser(Mockito.any())).thenReturn(u);

        mockMvc.perform(post("/users/add").content(asJsonString(u)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}