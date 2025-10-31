package stu.tttn.datn.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import stu.tttn.datn.dto.RegisterDto;

public interface UserService extends UserDetailsService {
    // Kế thừa loadUserByUsername(String username) từ UserDetailsService

    void registerUser(RegisterDto registerDto) throws Exception;
}