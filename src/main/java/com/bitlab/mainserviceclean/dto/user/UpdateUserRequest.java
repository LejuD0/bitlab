package com.bitlab.mainserviceclean.dto.user;

import lombok.Data;

@Data
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}

