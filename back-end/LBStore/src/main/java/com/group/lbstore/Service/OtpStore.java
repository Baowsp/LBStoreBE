package com.group.lbstore.Service;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OtpStore {

    private record OtpEntry(String otp, LocalDateTime expiresAt) {}

    private final ConcurrentHashMap<String, OtpEntry> store = new ConcurrentHashMap<>();

    /** Lưu OTP cho email, hết hạn sau 5 phút */
    public void save(String email, String otp) {
        store.put(email.toLowerCase(), new OtpEntry(otp, LocalDateTime.now().plusMinutes(5)));
    }

    /** Trả về true nếu OTP đúng và chưa hết hạn */
    public boolean verify(String email, String otp) {
        OtpEntry entry = store.get(email.toLowerCase());
        if (entry == null) return false;
        if (LocalDateTime.now().isAfter(entry.expiresAt())) {
            store.remove(email.toLowerCase());
            return false;
        }
        return entry.otp().equals(otp);
    }

    /** Xoá OTP sau khi đã dùng */
    public void invalidate(String email) {
        store.remove(email.toLowerCase());
    }
}
