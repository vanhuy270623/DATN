package stu.tttn.datn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomSuccessHandler customSuccessHandler;

    public SecurityConfig(CustomSuccessHandler customSuccessHandler) {
        this.customSuccessHandler = customSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Tạm thời tắt CSRF để dễ test
                .authorizeHttpRequests(authorize -> authorize


                        .requestMatchers(
                                "/", "/home", "/login", "/register", "/logout", // Các trang chung
                                "/css/**", "/js/**", "/images/**", "/vendor/**", // Tài nguyên tĩnh
                                "/books", "/books/detail/**", "/search" // Các trang công khai xem sách
                        ).permitAll()

                        // === PHÂN QUYỀN EBOOK STORE ===
                        // Vai trò 'ADMIN' (từ CSDL)
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Vai trò 'USER' (từ CSDL)
                        .requestMatchers(
                                "/cart/**", "/my-profile",
                                "/orders/**", "/reading-progress/**"
                        ).hasRole("USER")

                        // === RULE CUỐI CÙNG ===
                        // Tất cả các request còn lại đều yêu cầu đăng nhập
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login") // Trang login tùy chỉnh của bạn
                        .successHandler(customSuccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout") // Chuyển về trang login với thông báo logout
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}