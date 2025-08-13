package com.example.demo.service;

import com.example.demo.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    UserDto create(UserDto dto);
    UserDto getById(Long id);
    Page<UserDto> getAll(Pageable pageable);
    UserDto update(Long id, UserDto dto);
    void delete(Long id);
}
