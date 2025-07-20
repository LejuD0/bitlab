package com.bitlab.mainserviceclean.controller;

import com.bitlab.mainserviceclean.dto.auth.AuthRequest;
import com.bitlab.mainserviceclean.dto.auth.AuthResponse;
import com.bitlab.mainserviceclean.dto.auth.RegisterRequest;
import com.bitlab.mainserviceclean.dto.user.RoleUpdateRequest;
import com.bitlab.mainserviceclean.dto.user.UpdateUserRequest;
import com.bitlab.mainserviceclean.service.KeycloakAuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class AuthController {

    private final KeycloakAuthService authService;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return authService.authenticate(request);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest dto) {
        authService.createUser(dto);
        return ResponseEntity.ok("User created");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(
            @RequestBody UpdateUserRequest dto,
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = authHeader.replace("Bearer ", "");
        authService.updateCurrentUser(token, dto);
        return ResponseEntity.ok("User updated successfully");
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/update-role/{username}")
    public ResponseEntity<String> updateUserRole(
            @PathVariable String username,
            @RequestBody RoleUpdateRequest request
    ) {
        authService.updateUserRole(username, request.getRole().name());
        return ResponseEntity.ok("Role updated for user: " + username);
    }
}




