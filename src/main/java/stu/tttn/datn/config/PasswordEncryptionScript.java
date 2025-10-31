package stu.tttn.datn.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Script này dùng để chuyển đổi mật khẩu dạng plain text (thô) trong CSDL
 * sang dạng băm (hashed) bằng BCrypt.
 * LƯU Ý: Chỉ chạy script này MỘT LẦN trên CSDL chứa mật khẩu thô.
 */
public class PasswordEncryptionScript {
    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        try {
            // 1. Kết nối với cơ sở dữ liệu 'ebook_store'
            String url = "jdbc:mysql://localhost:3306/ebook_store";
            String username = "root";
            String password = "";

            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Kết nối CSDL thành công!");

            // 2. Lấy tất cả user và mật khẩu (giả sử đang là plain text)
            // Lấy từ bảng 'users' và cột 'password_hash'
            String selectQuery = "SELECT user_id, password_hash FROM users";
            PreparedStatement selectStmt = connection.prepareStatement(selectQuery);

            ResultSet rs = selectStmt.executeQuery();

            while (rs.next()) {
                String userId = rs.getString("user_id"); // Đổi sang String
                String plainTextPassword = rs.getString("password_hash"); // Lấy mật khẩu từ cột password_hash

                // 3. Mã hóa mật khẩu
                String encodedPassword = passwordEncoder.encode(plainTextPassword);

                // 4. Cập nhật mật khẩu mã hóa vào cơ sở dữ liệu
                // Cập nhật bảng 'users', cột 'password_hash' dựa trên 'user_id'
                String updateQuery = "UPDATE users SET password_hash = ? WHERE user_id = ?";
                PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
                updateStmt.setString(1, encodedPassword);
                updateStmt.setString(2, userId); // Đổi sang setString

                updateStmt.executeUpdate();
                System.out.println("Đã cập nhật mật khẩu cho user ID: " + userId);
            }

            System.out.println("Hoàn tất quá trình mã hóa mật khẩu.");
            rs.close();
            selectStmt.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}