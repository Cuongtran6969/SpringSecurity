package com.example.springsecurity.controller;

import com.example.springsecurity.Service.RoleService;
import com.example.springsecurity.model.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final RoleService roleService;

    @GetMapping("/role/{id}")
    public List<Role> getAllRoleByUserId(@PathVariable long id) {
        return roleService.getAllRoleByUserId(id);
    }
}
