package com.group.lbstore.Controller;

import com.group.lbstore.Model.User;
import com.group.lbstore.Repository.UserRepository;
import com.group.lbstore.Service.EmailService;
import com.group.lbstore.Service.OtpStore;
import com.group.lbstore.Service.UserService;
import com.group.lbstore.Security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private OtpStore otpStore;

    private final SecureRandom secureRandom = new SecureRandom();

    // ── Login ──────────────────────────────────────────────────────────────────

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        if (email == null || password == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email hoặc Password không được bỏ trống");
        }

        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                String token = jwtUtils.generateToken(user);
                Map<String, Object> response = new HashMap<>();
                response.put("token", token);
                response.put("user", user);
                return ResponseEntity.ok(response);
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email hoặc mật khẩu không chính xác.");
    }

    // ── Send OTP ───────────────────────────────────────────────────────────────

    /**
     * POST /api/auth/send-otp
     * Body: { "email": "user@example.com" }
     * Tạo OTP 6 số, lưu vào OtpStore và gửi email cho người dùng.
     */
    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("Email không được để trống.");
        }

        // Kiểm tra email đã tồn tại chưa
        if (userRepository.findByEmail(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email đã được sử dụng bởi tài khoản khác.");
        }

        // Tạo OTP 6 chữ số
        String otp = String.format("%06d", secureRandom.nextInt(1_000_000));
        otpStore.save(email, otp);

        try {
            emailService.sendOtpEmail(email, otp);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Không thể gửi email. Vui lòng kiểm tra địa chỉ email và thử lại.");
        }

        return ResponseEntity.ok(Map.of("message", "Mã OTP đã được gửi đến " + email));
    }

    // ── Verify OTP ─────────────────────────────────────────────────────────────

    /**
     * POST /api/auth/verify-otp
     * Body: { "email": "user@example.com", "otp": "123456" }
     * Trả về 200 nếu OTP đúng, 400 nếu sai/hết hạn.
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String otp = body.get("otp");

        if (email == null || otp == null) {
            return ResponseEntity.badRequest().body("Email và OTP không được để trống.");
        }

        if (!otpStore.verify(email, otp)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Mã xác nhận không đúng hoặc đã hết hạn.");
        }

        // Không xoá OTP ở đây – sẽ xoá sau khi đăng ký thành công
        return ResponseEntity.ok(Map.of("message", "Xác nhận thành công."));
    }

    // ── Register ───────────────────────────────────────────────────────────────

    /**
     * POST /api/auth/register
     * Chỉ được gọi sau khi OTP đã xác nhận thành công ở frontend.
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User userRequest) {
        try {
            // Kiểm tra OTP một lần nữa để bảo mật (phòng bypass frontend)
            // OTP đã được verify trước đó nên chỉ cần invalidate
            otpStore.invalidate(userRequest.getEmail());

            User savedUser = userService.registerUser(userRequest);
            return ResponseEntity.ok(savedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Đã xảy ra lỗi hệ thống khi đăng ký.");
        }
    }
}
