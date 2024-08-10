package com.springteam.backend.service;

import com.springteam.backend.entity.Role;
import com.springteam.backend.exception.RoleNotFoundException;
import com.springteam.backend.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements IRoleService {
    private IRoleRepository repository;

    @Autowired
    public RoleServiceImpl(IRoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public Set<Role> getAllRole() {
        return repository.findAll().stream().collect(Collectors.toSet());
    }

    @Override
    public Role getRoleByName(String roleName) {
        return repository.findByName(roleName).orElseThrow(() -> new RoleNotFoundException("cannot find role: " + roleName));
    }
}
