package com.springteam.backend.service;

import com.springteam.backend.entity.UserEntity;

public interface IUserService {
    Boolean isUserNameExisting(String userName);
    Boolean isEmailExisting(String email);
    void addNewUser(UserEntity user);
}
