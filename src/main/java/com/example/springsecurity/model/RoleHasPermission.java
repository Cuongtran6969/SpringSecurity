package com.example.springsecurity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_role_has_permission")
public class RoleHasPermission extends AbstractEntity<Integer> {

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "permission_id")
    private Permission permission;
}
