package com.group.lbstore.Model;

// Enum định nghĩa vòng đời của một ca bảo hành
public enum WarrantyStatus {
    RECEIVED,           // Vừa tiếp nhận, đang chờ kỹ thuật kiểm tra
    IN_PROGRESS,        // Đang tiến hành sửa chữa / thay linh kiện
    WAITING_FOR_PARTS,  // Đang tạm dừng chờ linh kiện từ hãng gửi về
    SENT_TO_VENDOR,     // Lỗi nặng, phải gửi máy trả về trung tâm bảo hành của hãng (VD: Apple Care)
    COMPLETED,          // Đã sửa xong, đang gọi khách đến lấy
    RETURNED_TO_CUSTOMER, // Đã trả máy cho khách thành công
    REJECTED            // Từ chối bảo hành (VD: Khách tự ý tháo máy, rách tem)
}
