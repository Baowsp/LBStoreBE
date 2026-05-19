package com.group.lbstore.Model;

// Enum định nghĩa các vị trí đặt Banner trên giao diện
public enum BannerPosition {
    HOME_MAIN_SLIDER,   // Slider siêu to khổng lồ ở đầu trang chủ
    HOME_SUB_BANNER,    // Các banner nhỏ nằm cạnh hoặc dưới slider chính
    CATEGORY_HEADER,    // Banner chung nằm ở đầu trang Danh mục
    POPUP_PROMO,        // Banner nhảy lên (Popup) khi khách vừa vào web
    CHECKOUT_PAGE,      // Banner nhỏ ở trang thanh toán

    // Vị trí banner theo danh mục cụ thể
    PHONE,
    LAPTOP,
    HEADPHONE,
    LOUDSPEAKER,
    CAMERA,
    SMARTWATCH,
    BATTERY,
    ACCESSORY
}
