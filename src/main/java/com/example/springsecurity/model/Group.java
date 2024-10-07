package com.example.springsecurity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tbl_group")
public class Group extends AbstractEntity<Integer> {
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToOne
    private Role role;

    @OneToMany(mappedBy = "group")
    private Set<GroupHasUser> groups = new HashSet<>();

}
