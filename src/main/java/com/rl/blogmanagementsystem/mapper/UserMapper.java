package com.rl.blogmanagementsystem.mapper;

import com.rl.blogmanagementsystem.dto.UserResponse;
import com.rl.blogmanagementsystem.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toDto(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getRole()
        );
    }
}
