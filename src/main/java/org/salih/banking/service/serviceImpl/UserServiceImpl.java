package org.salih.banking.service.serviceImpl;

import org.salih.banking.entitiy.User;
import org.salih.banking.model.UserRequest;
import org.salih.banking.service.UserService;
import org.salih.banking.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User addUser(UserRequest userRequest) {
        User user = new User();
        user.setFirstname(userRequest.getFirstname());
        user.setLastname(userRequest.getLastname());
        user.setCreditLimit(userRequest.getCreditLimit());
        user.setUsedCreditLimit(userRequest.getUsedCreditLimit());
        return userRepository.save(user);
    }

    @Override
    public List<User> listUsers() {
        return userRepository.findAll();
    }
}
