-- 1. Xóa dữ liệu cũ (Tùy chọn, Cẩn thận nếu đang có dữ liệu thật)
-- SET FOREIGN_KEY_CHECKS = 0;
-- TRUNCATE TABLE product_variants;
-- TRUNCATE TABLE products;
-- TRUNCATE TABLE brands;
-- SET FOREIGN_KEY_CHECKS = 1;

-- 2. Thêm dữ liệu cho bảng brands
INSERT INTO brands (name, description, logo_url, created_at, updated_at) VALUES 
('Apple', 'Công ty công nghệ Apple', 'https://upload.wikimedia.org/wikipedia/commons/f/fa/Apple_logo_black.svg', NOW(), NOW()),
('Samsung', 'Công ty công nghệ điện tử Samsung', 'https://upload.wikimedia.org/wikipedia/commons/2/24/Samsung_Logo.svg', NOW(), NOW()),
('Xiaomi', 'Tập đoàn công nghệ Xiaomi', 'https://upload.wikimedia.org/wikipedia/commons/2/29/Xiaomi_logo.svg', NOW(), NOW());

-- 3. Thêm dữ liệu cho bảng products
-- Giả sử Apple có id = 1, Samsung có id = 2
INSERT INTO products (name, description, brand_id, category, slug, image_url, created_at, updated_at) VALUES 
('iPhone 15 Pro Max', 'Điện thoại cao cấp nhất của Apple năm 2023 với khung Titan', 1, 'Điện thoại', 'iphone-15-pro-max', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2023/09/13/iphone-15-pro-max-natural-titanium-pure-back-iphone-15-pro-max-natural-titanium-pure-front-2up-screen-usen.png', NOW(), NOW()),
('Samsung Galaxy S24 Ultra', 'Điện thoại Android cao cấp nhất của Samsung tích hợp Galaxy AI', 2, 'Điện thoại', 'samsung-galaxy-s24-ultra', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2024/01/18/galaxy-s24-ultra-grey.png', NOW(), NOW());

-- 4. Thêm dữ liệu cho bảng product_variants
-- Giả sử iPhone 15 Pro Max có product_id = 1
INSERT INTO product_variants (product_id, sku, color, storage, price, stock_quantity, image_url, created_at, updated_at) VALUES 
-- iPhone 15 Pro Max - Titan Tự nhiên (3 mức dung lượng)
(1, 'IP15PM-TTN-256', 'Titan Tự Nhiên', '256GB', 34990000.00, 50, 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2023/09/13/iphone-15-pro-max-natural-titanium-pure-back-iphone-15-pro-max-natural-titanium-pure-front-2up-screen-usen.png', NOW(), NOW()),
(1, 'IP15PM-TTN-512', 'Titan Tự Nhiên', '512GB', 41990000.00, 30, 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2023/09/13/iphone-15-pro-max-natural-titanium-pure-back-iphone-15-pro-max-natural-titanium-pure-front-2up-screen-usen.png', NOW(), NOW()),
(1, 'IP15PM-TTN-1TB', 'Titan Tự Nhiên', '1TB', 46990000.00, 10, 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2023/09/13/iphone-15-pro-max-natural-titanium-pure-back-iphone-15-pro-max-natural-titanium-pure-front-2up-screen-usen.png', NOW(), NOW()),
-- iPhone 15 Pro Max - Titan Đen (2 mức dung lượng)
(1, 'IP15PM-TBK-256', 'Titan Đen', '256GB', 34500000.00, 45, 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2023/09/13/iphone-15-pro-max-black-titanium-pure-back-iphone-15-pro-max-black-titanium-pure-front-2up-screen-usen.png', NOW(), NOW()),
(1, 'IP15PM-TBK-512', 'Titan Đen', '512GB', 41500000.00, 25, 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2023/09/13/iphone-15-pro-max-black-titanium-pure-back-iphone-15-pro-max-black-titanium-pure-front-2up-screen-usen.png', NOW(), NOW());

-- Giả sử Samsung Galaxy S24 Ultra có product_id = 2
INSERT INTO product_variants (product_id, sku, color, storage, price, stock_quantity, image_url, created_at, updated_at) VALUES 
-- S24 Ultra - Xám Titan
(2, 'S24U-TGY-256', 'Xám Titan', '256GB', 33990000.00, 40, 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2024/01/18/galaxy-s24-ultra-grey.png', NOW(), NOW()),
(2, 'S24U-TGY-512', 'Xám Titan', '512GB', 37490000.00, 20, 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2024/01/18/galaxy-s24-ultra-grey.png', NOW(), NOW()),
-- S24 Ultra - Tím Titan
(2, 'S24U-TVT-256', 'Tím Titan', '256GB', 33990000.00, 35, 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2024/01/18/galaxy-s24-ultra-violet.png', NOW(), NOW());

-- 5. Thêm dữ liệu cho bảng product_items (Danh sách các máy vật lý cụ thể)
-- Giả sử variant_id = 1 (iPhone 15 Pro Max - Titan Tự nhiên 256GB)
INSERT INTO product_items (variant_id, serial_number, status, created_at, updated_at) VALUES
(1, 'IMEI-IP15PM-TTN-256-001', 'IN_STOCK', NOW(), NOW()),
(1, 'IMEI-IP15PM-TTN-256-002', 'IN_STOCK', NOW(), NOW());

-- Giả sử variant_id = 6 (S24/S24 Ultra - Xám Titan 256GB)
INSERT INTO product_items (variant_id, serial_number, status, created_at, updated_at) VALUES
(6, 'IMEI-S24U-TGY-256-001', 'IN_STOCK', NOW(), NOW()),
(6, 'IMEI-S24U-TGY-256-002', 'IN_STOCK', NOW(), NOW());

-- 6. Cấp sẵn một Admin với password đã lưu mã hoá BCrypt của "123456"
INSERT INTO users (id, email, password, full_name, role, is_active, created_at, updated_at, phone_number) VALUES
(UUID(), 'admin@gmail.com', '$2a$10$nshx8X5O8lJzOfG9ZOn9aOO1bM8Uj9mHhU8S6N5.aY9fP5c5B0a1K', 'Quản Trị Viên', 'ADMIN', true, NOW(), NOW(), '0987654321');
