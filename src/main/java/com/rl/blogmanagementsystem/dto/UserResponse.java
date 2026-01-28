package com.rl.blogmanagementsystem.dto;

import com.rl.blogmanagementsystem.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String email;
    private Role role;
}
