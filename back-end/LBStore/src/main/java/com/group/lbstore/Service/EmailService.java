package com.group.lbstore.Service;

public interface EmailService {
    /**
     * Gửi email chứa mã OTP đến địa chỉ email chỉ định.
     * @param toEmail địa chỉ người nhận
     * @param otp     mã OTP 6 chữ số
     */
    void sendOtpEmail(String toEmail, String otp);
}
