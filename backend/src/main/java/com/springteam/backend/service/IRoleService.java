package com.springteam.backend.service;

import com.springteam.backend.entity.Role;

import java.util.Set;

public interface IRoleService {
    Set<Role> getAllRole();
    Role getRoleByName(String roleName);
}
