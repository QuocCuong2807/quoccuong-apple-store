package com.springteam.backend.service.impl;

import com.springteam.backend.entity.UserEntity;
import com.springteam.backend.repository.IUserRepository;
import com.springteam.backend.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    private IUserRepository userRepository;

    @Autowired
    public UserServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Boolean isUserNameExisting(String userName) {
        return userRepository.existsByUsername(userName);
    }

    @Override
    public Boolean isEmailExisting(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void addNewUser(UserEntity user) {
        userRepository.save(user);
    }
}
