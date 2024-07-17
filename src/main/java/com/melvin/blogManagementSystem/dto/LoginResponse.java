package com.melvin.blogManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class LoginResponse {

    private String username;

    private List<String> roles;

    private String jwtToken;

}
