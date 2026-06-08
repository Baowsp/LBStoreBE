package com.group.lbstore.Model;

public enum ChatRoomStatus {
    WAITING, // Khách hàng tạo chat nhưng chưa có nhân viên nào phản hồi
    ACTIVE,  // Nhân viên đã join và đang chat
    CLOSED   // Phiên chat đã kết thúc
}
