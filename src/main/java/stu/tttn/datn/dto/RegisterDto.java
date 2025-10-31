package stu.tttn.datn.dto;

import lombok.Getter;
import lombok.Setter;
// Thêm các dependency validation nếu bạn cần (ví dụ: jakarta.validation.constraints.*)
// import jakarta.validation.constraints.Email;
// import jakarta.validation.constraints.NotEmpty;
// import jakarta.validation.constraints.Size;

@Getter
@Setter
public class RegisterDto {

    // @NotEmpty(message = "Username không được để trống")
    private String username;

    // @Email(message = "Email không hợp lệ")
    // @NotEmpty(message = "Email không được để trống")
    private String email;

    // @NotEmpty(message = "Mật khẩu không được để trống")
    // @Size(min = 6, message = "Mật khẩu phải có ít nhất 6 ký tự")
    private String password;
}