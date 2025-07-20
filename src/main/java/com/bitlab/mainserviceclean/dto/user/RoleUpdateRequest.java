package com.bitlab.mainserviceclean.dto.user;

import lombok.Data;
import com.bitlab.mainserviceclean.enums.Role;

@Data
public class RoleUpdateRequest {
    private Role role;
}

