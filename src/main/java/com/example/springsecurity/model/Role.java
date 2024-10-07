package com.example.springsecurity.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tbl_role")
public class Role extends AbstractEntity<Integer>{
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private Set<RoleHasPermission> permissions = new HashSet<>();

    @OneToMany(mappedBy = "role")
    @JsonIgnore
    private Set<UserHasRole> roles = new HashSet<>();
}
