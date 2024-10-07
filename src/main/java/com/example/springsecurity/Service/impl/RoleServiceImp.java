package com.example.springsecurity.Service.impl;

import com.example.springsecurity.Service.RoleService;
import com.example.springsecurity.model.Role;
import com.example.springsecurity.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImp implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoleByUserId(long userId) {
        return roleRepository.getAllByUserId(userId);
    }
}
