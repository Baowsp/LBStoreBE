package com.group.lbstore.Service;

import com.group.lbstore.Model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User registerUser(User user); // For customers
    User createUser(User user); // For internal use (creating employees/admins)
    User getUserById(UUID id);
    User getUserByEmail(String email);
    User updateUser(UUID id,User user);
    Page<User> getAllUsers(Pageable pageable);
    void deactivateUser(UUID id);
    void updatePassword(UUID id, String oldPassword, String newPassword);
}