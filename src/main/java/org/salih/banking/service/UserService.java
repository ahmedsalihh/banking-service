package org.salih.banking.service;

import org.salih.banking.entitiy.User;

import java.util.List;

public interface UserService {
    User addUser(User user);

    List<User> listUsers();
}
