package org.salih.banking.service;

import org.salih.banking.entitiy.User;
import org.salih.banking.model.UserRequest;

import java.util.List;

public interface UserService {
    User addUser(UserRequest user);

    List<User> listUsers();
}
