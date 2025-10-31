package stu.tttn.datn.config; // Đã đổi package cho phù hợp dự án

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        // Lấy danh sách các quyền (authorities) của người dùng
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectUrl = "/"; // Trang mặc định

        /* * Lấy quyền cao nhất.
         * Giả sử ADMIN là quyền cao nhất, kiểm tra ADMIN trước.
         * Spring Security thường tự động thêm tiền tố 'ROLE_'.
         */
        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            if (role.equals("ROLE_ADMIN")) {
                redirectUrl = "/admin/dashboard"; // Chuyển hướng ADMIN đến trang dashboard
                break; // Tìm thấy quyền ADMIN, dừng lại
            } else if (role.equals("ROLE_USER")) {
                redirectUrl = "/home"; // Chuyển hướng USER về trang chủ
            }
        }

        response.sendRedirect(redirectUrl);
    }
}