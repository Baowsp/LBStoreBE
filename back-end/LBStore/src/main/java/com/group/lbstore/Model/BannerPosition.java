package com.group.lbstore.Model;

// Enum định nghĩa các vị trí đặt Banner trên giao diện
public enum BannerPosition {
    HOME_MAIN_SLIDER,   // Slider siêu to khổng lồ ở đầu trang chủ
    HOME_SUB_BANNER,    // Các banner nhỏ nằm cạnh hoặc dưới slider chính
    CATEGORY_HEADER,    // Banner nằm ở đầu trang Danh mục (VD: Bấm vào Laptop thì hiện banner Laptop)
    POPUP_PROMO,        // Banner nhảy lên (Popup) khi khách vừa vào web
    CHECKOUT_PAGE       // Banner nhỏ ở trang thanh toán (VD: Nhắc nhở nhập mã VNPAY)
}
