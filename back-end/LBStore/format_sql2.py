import re
import unicodedata

data = """
(2, 'Gigabyte Aero 20 OLED2', 2, 1),
(3, 'Điện thoại iPhone 14 Pro Max (128GB) - Chính hãng VN/A', 2, 1),
(4, 'Acer Chromebook Spin 713', 1, 2),
(5, 'Samsung Galaxy Book Flex 2 Alpha', 3, 2),
(6, 'Điện thoại iPhone 14 Pro Max (128GB) - Chính hãng VN/A', 2, 3),
(8, 'MacBook Air M1 13" (8GB/256GB) - Chính hãng Apple Việt Nam', 3, 1),
(9, 'Điện thoại iPhone 14 Pro Max (128GB) - Chính hãng VN/A', 2, 3),
(10, 'Dell Chromebook 3100', 3, 2),
(11, 'Điện thoại iPhone 15 (256GB) - Chính hãng VN/A', 2, 1),
(12, 'Điện thoại iPhone 14 Pro Max (256GB) - Chính hãng VN/A', 2, 1),
(13, 'Điện thoại ASUS ROG Phone 7 (16GB/512GB) - Chính hãng', 3, 1),
(14, 'Điện thoại iPhone 14 Pro Max (256GB) - Chính hãng VN/A', 3, 1),
(15, 'Điện thoại iPhone 14 Plus (128GB) - Chính hãng VN/A', 1, 1),
(16, 'Điện thoại iPhone 15 Plus (128GB) - Chính hãng VN/A', 1, 1),
(17, 'Điện thoại iPhone 14 Pro Max (128GB) - Chính hãng VN/A', 2, 1),
(18, 'Điện thoại iPhone 15 (512GB) - Chính hãng VN/A', 2, 1),
(19, 'Điện thoại Samsung Galaxy S23 Plus - 8GB/512GB - Chính hãng', 1, 1),
(20, 'Điện thoại NUBIA REDMAGIC 7 PUSAR 16GB 256GB - Chính hãng', 1, 1),
(21, 'Điện thoại NUBIA REDMAGIC 7 PUSAR 16GB 256GB - Chính hãng', 1, 1),
(22, 'Điện thoại iPhone 14 Pro Max (256GB) - Chính hãng VN/A', 2, 1),
(23, 'MacBook Air M1 13" (8GB/256GB) - Chính hãng Apple Việt Nam', 3, 1),
(24, 'Điện thoại iPhone 14 Pro Max (256GB) - Chính hãng VN/A', 1, 1),
(25, 'Điện thoại iPhone 15 (512GB) - Chính hãng VN/A', 1, 1),
(26, 'Đồng hồ thông minh Apple Watch Series 7 GPS, 41mm – Viền nhôm dây cao su - Chính hãng VN/A', 2, 5),
(27, 'Đồng hồ thông minh Apple Watch SE 2023 - GPS + LTE, 44mm - Vỏ Nhôm Dây Cao Su - VN/A', 3, 5),
(28, 'Đồng hồ thông minh Forerunner 255 Music - Chính Hãng', 2, 5),
(29, 'Đồng hồ thông minh Apple Watch SE 2023 - GPS, 44mm - Vỏ Nhôm Dây Cao Su - VN/A', 2, 5),
(30, 'Đồng hồ thông minh Garmin Venu SQ 2 Music - Chính hãng', 2, 5),
(31, 'Đồng hồ thông minh Forerunner 255 Music - Chính Hãng', 3, 5),
(32, 'Đồng hồ thông minh Garmin Venu SQ 2 Music - Chính hãng', 2, 5),
(40, 'Asus ZenBook 14 OLED', 1, 2),
(41, 'Dell XPS 13 Plus', 2, 2),
(42, 'HP Spectre x360 14', 3, 2),
(43, 'Apple MacBook Air M2', 4, 2),
(44, 'Lenovo ThinkPad X1 Carbon Gen 10', 1, 2),
(45, 'Microsoft Surface Laptop Studio', 2, 2),
(46, 'Samsung Galaxy Book Pro 360', 3, 2),
(47, 'Acer Swift X', 4, 2),
(48, 'Razer Blade 14', 1, 2),
(49, 'Gigabyte Aero 15 OLED', 2, 2),
(50, 'MSI Prestige 14', 3, 6),
(51, 'ASUS ROG Zephyrus G14', 4, 6),
(52, 'Acer Nitro 5', 1, 6),
(53, 'Dell G15', 2, 2),
(54, 'HP Pavilion Gaming 15', 3, 2),
(55, 'Lenovo Legion 5 Pro', 4, 2),
(56, 'Asus TUF Dash 15', 1, 2),
(57, 'Acer Swift 3', 2, 2),
(58, 'Dell Inspiron 14', 3, 2),
(60, 'Lenovo IdeaPad 5', 1, 2),
(61, 'Microsoft Surface Laptop 4', 2, 2),
(62, 'Samsung Galaxy Book Flex 2 Alpha', 3, 2),
(63, 'Acer Chromebook Spin 713', 4, 2),
(64, 'Dell Chromebook 3100', 1, 2),
(65, 'HP Chromebook x360 14', 2, 2),
(66, 'Lenovo Chromebook Duet 5', 3, 2),
(67, 'Asus Chromebook Flip CX5', 4, 2),
(68, 'Asus Chromebook Flip CX5', 2, 1),
(69, 'Microsoft Surface Laptop 4', 2, 5),
(70, 'Asus Chromebook Flip CX5', 2, 2),
(71, 'Microsoft Surface Laptop 4', 2, 2),
(72, 'Asus Chromebook Flip CX5', 3, 2),
(73, 'AAAAAAAAAAAAAAAAAAAAa', 2, 3),
(74, 'Màn hình Asus VA24EHF  - Chính hãng', 2, 7),
(75, 'Màn hình LG 24MP60G-B 23.8inch/FHD/IPS/75Hz/1ms/ Đen', 3, 7),
(76, 'Màn hình AOC G2490VX/74 23.8 inch/FHD/VA/144Hz', 2, 7),
(77, 'Màn hình LG 27MP60G-B 27inch/FHD/IPS/75Hz/5ms/250nitsĐen', 3, 7),
(78, 'Màn hình LG 27MK600M-B 27 inch/FHD/ - Chính hãng', 2, 7),
(79, 'Màn hình LG 27GQ50F 27 inch/FHD/VA/165Hz Đen', 2, 7),
(80, 'Màn hình LG 27MP60G-B 27inch/FHD/IPS/75Hz/5ms/Đen', 2, 7),
(81, 'Tivi sony nét căng', 3, 6),
(82, 'TV Chromebook Flip CX5', 2, 6),
(83, 'Laptop acer 360 gaming', 1, 2),
(84, 'Ipad2712 Vip pro max', 2, 2),
(85, 'Asus gaming 1003 VN', 2, 2),
(86, 'Test Product Vip', 5, 3),
(87, 'Màn hình 360 gaming', 2, 7),
(88, 'New product vip pro', 3, 2),
(89, 'New product version 2', 2, 7),
(90, 'New product 11226', 1, 1),
(91, 'Máy tính bảng iPad Pro M2 11" 5G (128GB) - Chính hãng Apple Việt Nam', 2, 3),
(92, 'Máy tính bảng Samsung Galaxy Tab S9 5G 8GB/128GB - Chính hãng', 1, 3),
(93, 'Máy tính bảng Samsung Galaxy Tab S9 FE Plus Wifi 8GB/128GB', 2, 3),
(94, 'Máy tính bảng iPad Gen 9 10.2" Wi-Fi (256GB) - Chính hãng Apple Việt Nam', 2, 3),
(95, 'Máy tính bảng OPPO Pad Air (Màu tím) - 128GB - Chính hãng', 2, 3),
(96, 'Máy tính bảng iPad Gen 9 10.2" Wi-Fi (256GB) - Chính hãng Apple Việt Nam', 1, 1)
"""

def strip_accents(text):
    text = unicodedata.normalize('NFD', text)
    text = text.encode('ascii', 'ignore').decode('utf-8')
    return str(text)

def to_slug(text):
    text = strip_accents(text).lower()
    text = re.sub(r'[^a-z0-9]+', '-', text)
    return text.strip('-')

lines = [l.strip() for l in data.strip().split('\n') if l.strip()]

out_lines = []
seen_names = set()

for line in lines:
    m = re.match(r'\((\d+),\s*\'(.*?)\',\s*(\d+),\s*(\d+)\),?', line)
    if not m:
        m = re.match(r'\((\d+),\s*"(.*?)",\s*(\d+),\s*(\d+)\),?', line)
    if m:
        id_val = m.group(1)
        name_raw = m.group(2)
        if name_raw in seen_names:
            continue
        seen_names.add(name_raw)
        
        name = name_raw.replace('"', '\\"').replace("'", "''")
        brand_id = m.group(3)
        cat_id = m.group(4)
        
        name_lower = name.lower()
        if 'điện thoại' in name_lower or 'iphone' in name_lower or 'rog phone' in name_lower or 'nubia' in name_lower:
            category = 'Điện thoại'
        elif 'macbook' in name_lower or 'laptop' in name_lower or 'chromebook' in name_lower or 'aero' in name_lower or 'book' in name_lower or 'spin' in name_lower or 'zenbook' in name_lower or 'thinkpad' in name_lower or 'swift' in name_lower or 'blade' in name_lower or 'zephyrus' in name_lower or 'nitro' in name_lower or 'ideapad' in name_lower or 'xps' in name_lower or 'spectre' in name_lower or 'prestige' in name_lower or 'tuf' in name_lower or 'inspiron' in name_lower:
            category = 'Laptop'
        elif 'đồng hồ' in name_lower or 'watch' in name_lower or 'forerunner' in name_lower or 'venu' in name_lower:
            category = 'Đồng hồ'
        elif 'màn hình' in name_lower:
            category = 'Màn hình'
        elif 'tivi' in name_lower or 'tv' in name_lower:
            category = 'Tivi'
        elif 'máy tính bảng' in name_lower or 'ipad' in name_lower or 'pad' in name_lower or 'tab' in name_lower:
            category = 'Máy tính bảng'
        else:
            category = 'Khác'
            
        slug = to_slug(name_raw)
        desc = name
        image_url = 'https://via.placeholder.com/500' # Placeholder image
        
        out_lines.append(f"('{name}', '{desc}', {brand_id}, '{category}', '{slug}', '{image_url}', NOW(), NOW())")

with open('output_products.sql', 'w', encoding='utf-8') as f:
    f.write("INSERT INTO products (name, description, brand_id, category, slug, image_url, created_at, updated_at) VALUES\n")
    for i, out in enumerate(out_lines):
        if i == len(out_lines) - 1:
            f.write(out + ';\n')
        else:
            f.write(out + ',\n')
