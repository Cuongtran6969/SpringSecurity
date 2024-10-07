package com.example.springsecurity.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tbl_token")
public class Token extends AbstractEntity<Integer>{
    @Column(name = "username")
    private String username;

    @Column(name = "access_token")
    private String access_token;

    @Column(name = "refresh_token")
    private String refresh_token;

}
