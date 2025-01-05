package org.salih.banking.controller;

import jakarta.validation.Valid;
import org.salih.banking.entitiy.User;
import org.salih.banking.model.UserRequest;
import org.salih.banking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/list")
    public ResponseEntity<List<User>> listUsers(){
        return ResponseEntity.ok(userService.listUsers());
    }

    @PostMapping("/add")
    public ResponseEntity<User> addUser(@Valid @RequestBody UserRequest user){
        return ResponseEntity.ok(userService.addUser(user));
    }
}
