package com.example.demo.mapper;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;

public class UserMapper {
    public static UserDto toDto(User u) {
        UserDto d = new UserDto();
        d.setId(u.getId());
        d.setName(u.getName());
        d.setEmail(u.getEmail());
        return d;
    }

    public static User toEntity(UserDto d) {
        User u = new User();
        u.setName(d.getName());
        u.setEmail(d.getEmail());
        return u;
    }
}
