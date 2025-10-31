package stu.tttn.datn.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import stu.tttn.datn.dto.RegisterDto;
import stu.tttn.datn.entity.Role;
import stu.tttn.datn.entity.User;
import stu.tttn.datn.repository.RoleRepository;
import stu.tttn.datn.repository.UserRepository;
import stu.tttn.datn.service.UserService;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Phương thức này được Spring Security gọi khi xử lý LOGIN
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tìm user trong CSDL bằng username
        // Vì User entity đã implement UserDetails, chúng ta có thể trả về nó trực tiếp
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user với username: " + username));
    }

    /**
     * Phương thức này được gọi từ Controller khi xử lý REGISTER
     */
    @Override
    public void registerUser(RegisterDto registerDto) throws Exception {
        // 1. Kiểm tra username đã tồn tại chưa
        if (userRepository.findByUsername(registerDto.getUsername()).isPresent()) {
            throw new Exception("Username đã tồn tại");
        }

        // 2. Kiểm tra email đã tồn tại chưa
        if (userRepository.findByEmail(registerDto.getEmail()).isPresent()) {
            throw new Exception("Email đã được sử dụng");
        }

        // 3. Tìm role "USER" (từ CSDL)
        Role userRole = roleRepository.findByRoleName(Role.RoleName.USER)
                .orElseThrow(() -> new Exception("Không tìm thấy Role 'USER'. Vui lòng thêm role này vào CSDL."));

        // 4. Tạo đối tượng User mới
        User user = new User();
        user.setUserId(UUID.randomUUID().toString()); // Tạo ID ngẫu nhiên (vì cột là VARCHAR)
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPasswordHash(passwordEncoder.encode(registerDto.getPassword())); // Mã hóa mật khẩu
        user.setRole(userRole); // Gán role USER
        user.setCreatedAt(java.time.LocalDateTime.now()); // Set thời gian tạo (nếu CSDL của bạn có cột này)

        // 5. Lưu vào CSDL
        userRepository.save(user);
    }
}