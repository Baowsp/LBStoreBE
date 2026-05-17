package com.group.lbstore.Model;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Quan hệ One-To-One: Mỗi hồ sơ khách hàng gắn liền với đúng 1 tài khoản User
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, unique = true)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private User user;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 20)
    private Gender gender;

    // Điểm thưởng tích lũy khi mua laptop/điện thoại
    @Column(name = "reward_points", nullable = false)
    @Builder.Default
    private Integer rewardPoints = 0;

    // Hạng thành viên để tính chiết khấu
    @Enumerated(EnumType.STRING)
    @Column(name = "customer_grade", nullable = false)
    @Builder.Default
    private CustomerGrade grade = CustomerGrade.MEMBER;

    // Địa chỉ giao hàng mặc định (Có thể lưu dạng chuỗi hoặc tách thành Model Address riêng)
    // Thêm vào file Customer.java
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<Address> addresses = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

// Enum định nghĩa giới tính
enum Gender {
    MALE,
    FEMALE,
    OTHER
}

// Enum định nghĩa các hạng thẻ khách hàng thân thiết
enum CustomerGrade {
    MEMBER,   // Thành viên mới
    SILVER,   // Khách hàng Bạc
    GOLD,     // Khách hàng Vàng
    DIAMOND   // Khách hàng Kim Cương (VIP)
}