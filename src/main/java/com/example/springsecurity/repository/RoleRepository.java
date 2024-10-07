package com.example.springsecurity.repository;

import com.example.springsecurity.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query(value = "select r from Role r join UserHasRole ur on ur.role.id = r.id where ur.user.id = :userId")
    List<Role> getAllByUserId(Long userId);
}
