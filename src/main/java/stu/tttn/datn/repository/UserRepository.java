package stu.tttn.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stu.tttn.datn.entity.User;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // Dùng cho UserDetailsService (Login)
    Optional<User> findByUsername(String username);

    // Dùng để kiểm tra khi đăng ký
    Optional<User> findByEmail(String email);

}