package com.bitlab.mainserviceclean.dto.auth;


import lombok.Data;

import javax.management.relation.Role;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private Role role;
}
