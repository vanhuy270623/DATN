package stu.tttn.datn.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import stu.tttn.datn.dto.RegisterDto;
import stu.tttn.datn.service.UserService;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET: Hiển thị trang login
     * Spring Security sẽ tự động xử lý POST /login
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // Trả về file login.html
    }

    /**
     * GET: Hiển thị trang register
     */
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("registerDto", new RegisterDto());
        return "register"; // Trả về file register.html
    }

    /**
     * POST: Xử lý dữ liệu từ form register
     */
    @PostMapping("/register")
    public String processRegistration(@ModelAttribute("registerDto") RegisterDto registerDto,
                                      BindingResult bindingResult,
                                      RedirectAttributes redirectAttributes) {
        // (Tùy chọn) Xử lý validation
        if (bindingResult.hasErrors()) {
            return "register"; // Quay lại trang register nếu có lỗi
        }

        try {
            userService.registerUser(registerDto);
            // Gửi thông báo thành công sang trang login
            redirectAttributes.addFlashAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
            return "redirect:/login"; // Chuyển hướng về trang login
        } catch (Exception e) {
            // Gửi thông báo lỗi quay lại trang register
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/register";
        }
    }
}