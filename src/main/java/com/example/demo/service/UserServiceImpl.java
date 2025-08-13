package com.example.demo.service;

import com.example.demo.dto.UserDto;
import com.example.demo.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.repository.UserRepository;
import com.example.demo.exception.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository repo;
    public UserServiceImpl(UserRepository repo) {
        this.repo = repo;
    }
    @Override
    public UserDto create(UserDto dto) {
        if(repo.findByEmail(dto.getEmail()).isPresent()) {
            throw new BadRequestException("Email already exists");
        }
        User saved = repo.save(UserMapper.toEntity(dto));
        return UserMapper.toDto(saved);
    }

    @Override
    public UserDto getById(Long id) {
        User u = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserMapper.toDto(u);
    }

    @Override
    public Page<UserDto> getAll(Pageable pageable) {
        return repo.findAll(pageable).map(UserMapper::toDto);
    }

    @Override
    public UserDto update(Long id, UserDto dto) {
        User u = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        u.setName(dto.getName());
        u.setEmail(dto.getEmail());
        return UserMapper.toDto(repo.save(u));
    }

    @Override
    public void delete(Long id) {
        repo.deleteById(id);
    }
}
