package stu.tttn.datn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import stu.tttn.datn.entity.Role;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    // Tìm role theo tên (ví dụ: "USER")
    Optional<Role> findByRoleName(Role.RoleName roleName);
}