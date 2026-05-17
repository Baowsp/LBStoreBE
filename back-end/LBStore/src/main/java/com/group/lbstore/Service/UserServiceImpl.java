package com.group.lbstore.Service;

import com.group.lbstore.Model.User;
import com.group.lbstore.Model.UserRole;
import com.group.lbstore.Repository.UserRepository;
import com.group.lbstore.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final com.group.lbstore.Repository.CustomerRepository customerRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use.");
        }
        user.setRole(UserRole.CUSTOMER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getIsActive() == null) {
            user.setIsActive(true);
        }
        User savedUser = userRepository.save(user);
        
        com.group.lbstore.Model.Customer customer = new com.group.lbstore.Model.Customer();
        customer.setUser(savedUser);
        customerRepository.save(customer);
        
        return savedUser;
    }

    @Override
    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getIsActive() == null) {
            user.setIsActive(true);
        }
        return userRepository.save(user);
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void deactivateUser(UUID id) {
        // HARD DELETE TÀI KHOẢN THEO YÊU CẦU ("xóa luôn")
        try {
            Long customerId = jdbcTemplate.queryForObject("SELECT id FROM customers WHERE user_id = ?", Long.class, id);
            if (customerId != null) {
                // Xóa các liên kết Order và Detail
                jdbcTemplate.update("DELETE FROM online_order_details WHERE online_order_id IN (SELECT id FROM online_orders WHERE customer_id = ?)", customerId);
                jdbcTemplate.update("DELETE FROM online_orders WHERE customer_id = ?", customerId);
                
                // Xóa địa chỉ và customer
                jdbcTemplate.update("DELETE FROM addresses WHERE customer_id = ?", customerId);
                jdbcTemplate.update("DELETE FROM customers WHERE id = ?", customerId);
            }
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            // Không có profile customer (vd: Admin)
        }

        // Xóa bình luận liên quan (Gỡ liên kết hoặc xóa cascade nếu cần)
        // Bọc SELECT bằng 1 subquery ẩn (AS c_temp) để tránh lỗi Error 1093 (MySQL Cannot specify target table for update)
        jdbcTemplate.update("DELETE FROM comments WHERE parent_id IN (SELECT id FROM (SELECT id FROM comments WHERE user_id = ?) AS c_temp)", id);
        
        // Sau đó xóa bình luận do chính người đó tạo
        jdbcTemplate.update("DELETE FROM comments WHERE user_id = ?", id);

        // Cuối cùng xóa User
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);
    }

    @Override
    public User updateUser(UUID id, User userDetails) {
        User user = getUserById(id);
        if (userDetails.getFullName() != null) user.setFullName(userDetails.getFullName());
        if (userDetails.getPhoneNumber() != null) user.setPhoneNumber(userDetails.getPhoneNumber());
        if (userDetails.getRole() != null) user.setRole(userDetails.getRole());
        return userRepository.save(user);
    }

    @Override
    public void updatePassword(UUID id, String oldPassword, String newPassword) {
        User user = getUserById(id);
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không đúng.");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}