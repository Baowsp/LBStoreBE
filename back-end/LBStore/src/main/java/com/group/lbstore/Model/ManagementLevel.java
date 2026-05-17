package com.group.lbstore.Model;

// Enum định nghĩa các cấp bậc quản lý trong hệ thống bán lẻ
public enum ManagementLevel {
    TEAM_LEADER,        // Trưởng nhóm (VD: Trưởng ca bán hàng)
    STORE_MANAGER,      // Cửa hàng trưởng (Quản lý toàn bộ 1 showroom)
    AREA_MANAGER,       // Giám đốc khu vực (Quản lý cụm 5-10 cửa hàng)
    DEPARTMENT_HEAD,    // Trưởng phòng (VD: Trưởng phòng Marketing, Trưởng phòng Kỹ thuật)
    DIRECTOR            // Giám đốc khối / Ban giám đốc
}
