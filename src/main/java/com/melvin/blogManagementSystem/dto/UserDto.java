package com.melvin.blogManagementSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {

    private String firstname;
    private String lastname;
    private String email;
    private String password;

}
