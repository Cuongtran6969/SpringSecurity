package com.example.springsecurity.Service;

import com.example.springsecurity.model.Role;

import java.util.List;

public interface RoleService {

    List<Role> getAllRoleByUserId(long userId);
}
