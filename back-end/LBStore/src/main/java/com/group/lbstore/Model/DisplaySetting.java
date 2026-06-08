package com.group.lbstore.Model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Bảng key-value lưu cài đặt hiển thị (số cột, số hàng, v.v.)
 * Mỗi row = một setting key.
 */
@Entity
@Table(name = "display_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisplaySetting {

    @Id
    @Column(name = "setting_key", length = 100)
    private String settingKey;

    @Column(name = "setting_value", nullable = false)
    private Integer settingValue;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onSave() {
        this.updatedAt = LocalDateTime.now();
    }
}
