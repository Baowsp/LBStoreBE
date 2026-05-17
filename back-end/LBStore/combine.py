import re
import unicodedata

data_products = """
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

data_variants = """
(4, 'black', 'https://cdn.hoanghamobile.com/i/productlist/ts/Uploads/2023/03/24/redminote12-0.png', 12000000, 9000000, 2, 64, 2),
(5, 'blue', 'https://cdn.hoanghamobile.com/i/productlist/ts/Uploads/2023/05/11/note12s.png', 23000000, 21000000, 5, 32, 3),
(6, 'black', 'https://cdn.hoanghamobile.com/i/productlist/ts/Uploads/2023/10/11/tab-s9-fe-1.png', 27000000, 26500000, 6, 64, 3),
(7, 'green', 'https://cdn.hoanghamobile.com/i/productlist/ts/Uploads/2023/02/27/thumb-xiaomi-13-lite.png', 32000000, 31000000, 3, 32, 4),
(8, 'blue', 'https://cdn.hoanghamobile.com/i/productlist/ts/Uploads/2023/03/13/c55-1-den.png', 17000000, 0, 1, 16, 4),
(9, 'pink', 'https://cdn.hoanghamobile.com/i/productlist/ts/Uploads/2023/01/11/image-removebg-preview-8.png', 22000000, 0, 2, 32, 5),
(10, 'black', 'https://cdn.hoanghamobile.com/i/productlist/ts/Uploads/2023/12/05/s23-xang.png', 23000000, 22300000, 5, 16, 6),
(11, 'black', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2022/09/08/anh-chup-man-hinh-2022-09-08-luc-01-57-13-removebg-preview.png', 19000000, 18700000, 5, 32, 11),
(12, 'black', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2023/10/19/xueying-product-images-openback-gold-9-rgb-20230725-removebg-preview.png', 21000000, 19900000, 2, 16, 12),
(13, 'red', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2023/10/27/oppo-find-n3.png', 22000000, 21000000, 3, 32, 13),
(14, 'black', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2023/09/13/iphone-15-pro-blue-titanium-pure-back-iphone-15-pro-blue-titanium-pure-front-2up-screen-usen.png', 18000000, 17500000, 2, 16, 14),
(15, 'black', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2023/09/13/iphone-15-pro-natural-titanium-pure-back-iphone-15-pro-natural-titanium-pure-front-2up-screen-usen.png', 15690000, 15000000, 3, 16, 15),
(16, 'black', 'https://cdn.hoanghamobile.com/i/productlist/ts/Uploads/2021/09/15/image-removebg-preview-28.png', 8999000, 7500000, 1, 2, 26),
(17, 'red', 'https://cdn.hoanghamobile.com/i/productlist/ts/Uploads/2020/11/04/venu-sq.png', 6500000, 6000000, 1, 4, 27),
(18, 'blue', 'https://cdn.hoanghamobile.com/i/productlist/ts/Uploads/2023/02/15/ks62w.png', 8000000, 7200000, 2, 4, 28),
(19, 'red', 'https://cdn.hoanghamobile.com/i/productlist/ts/Uploads/2022/03/31/image-removebg-preview.png', 12000000, 11500000, 1, 2, 29),
(20, 'blue', 'https://cdn.hoanghamobile.com/i/productlist/ts/Uploads/2020/11/04/venu-sq.png', 9000000, 8700000, 1, 2, 30),
(21, 'black', 'https://cdn.hoanghamobile.com/i/productlist/ts/Uploads/2022/06/13/vong-deo-tay-thong-minh-huawei-band-7-chinh-hang-12.png', 8500000, 8100000, 1, 2, 31),
(22, 'Black', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2022/10/24/image-removebg-preview-33.png', 10000000, 9000000, 50, 8, 40),
(23, 'Silver', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2022/07/18/image-removebg-preview-1_637937326256886528.png', 12000000, 11000000, 30, 16, 41),
(24, 'Blue', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2022/12/22/image-removebg-preview.png', 8000000, 7500000, 40, 4, 42),
(25, 'Red', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2023/09/26/21dj00cwvn-1.png', 11000000, 10000000, 25, 8, 43),
(26, 'Green', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2022/10/14/image-removebg-preview-39.png', 9000000, 8000000, 35, 16, 44),
(27, 'Gold', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2022/12/22/image-removebg-preview.png', 13000000, 12000000, 20, 4, 45),
(28, 'Gray', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2023/03/06/14zd90q-g-ax32a5-1.png', 9500000, 9000000, 45, 12, 46),
(29, 'White', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2022/12/22/image-removebg-preview.png', 10500000, 9500000, 30, 8, 47),
(30, 'Purple', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2022/10/14/image-removebg-preview-39.png', 8000000, 7500000, 40, 16, 48),
(31, 'Orange', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2022/10/24/image-removebg-preview-33.png', 12000000, 11000000, 25, 4, 49),
(32, 'Yellow', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2023/03/06/14zd90q-g-ax32a5-1.png', 10000000, 9500000, 35, 8, 50),
(33, 'Pink', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2022/10/24/image-removebg-preview-33.png', 11000000, 10000000, 30, 16, 51),
(34, 'Brown', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2022/10/14/image-removebg-preview-39.png', 9000000, 8000000, 40, 12, 52),
(35, 'Cyan', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2023/03/06/14zd90q-g-ax32a5-1.png', 13000000, 12000000, 20, 8, 53),
(36, 'Magenta', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2023/03/06/14zd90q-g-ax32a5-1.png', 9500000, 9000000, 45, 16, 54),
(37, 'Teal', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2023/03/06/14zd90q-g-ax32a5-1.png', 10500000, 9500000, 30, 4, 55),
(38, 'Olive', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2022/10/24/image-removebg-preview-33.png', 8000000, 7500000, 40, 8, 56),
(39, 'Maroon', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2022/12/22/image-removebg-preview.png', 15000000, 14000000, 25, 16, 57),
(40, 'Navy', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2022/10/14/image-removebg-preview-39.png', 9000000, 8500000, 20, 8, 58),
(43, 'Navy', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2022/08/23/image-removebg-preview-16.png', 8000000, 75000000, 5, 1, 74),
(44, 'Orange', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2022/08/18/image-removebg-preview-7.png', 5500000, 0, 2, 1, 75),
(45, 'Orange', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2022/08/23/image-removebg-preview-20.png', 4900000, 0, 5, 1, 76),
(46, 'Whitehttps://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2022/08/18/image-removebg-preview-31.png', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2022/08/18/image-removebg-preview-31.png', 7500000, 5900000, 5, 1, 77),
(47, 'Red', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2022/08/23/image-removebg-preview-15.png', 8880000, 7950000, 5, 1, 78),
(48, 'Black', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2022/10/14/image-removebg-preview-39.png', 8000000, 75000000, 5, 1, 79),
(49, 'Maroon', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2022/08/23/image-removebg-preview-20.png', 7500000, 5900000, 5, 1, 80),
(50, 'Orange', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2022/08/23/image-removebg-preview-20.png', 7500000, 5900000, 5, 1, 81),
(51, 'Red', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2023/03/06/14zd90q-g-ax32a5-1.png', 9000000, 7500000, 25, 1, 82),
(52, 'red', 'https://cdn.hoanghamobile.com/i/productlist/ts/Uploads/2023/06/28/nokia-c32-0.png', 9000000, 62352300, 5, 32, 2),
(53, 'red', 'https://cdn.hoanghamobile.com/i/productlist/ts/Uploads/2023/06/28/nokia-c32-0.png', 9000000, 2, 5, 32, 2),
(55, 'red', 'https://cdn.hoanghamobile.com/i/productlist/ts/Uploads/2023/11/06/thumb-trang.png', 10000000, 9000000, 5, 8, 2),
(56, 'red', 'https://cdn.hoanghamobile.com/i/productlist/dsp/Uploads/2023/10/16/82tsa071vn-1.png', 15000000, 12000000, 1, 8, 89),
(58, 'red', 'https://cdn.hoanghamobile.com/i/productlist/ts/Uploads/2022/10/20/image-removebg-preview-28.png', 10000000, 9000000, 5, 16, 90),
(59, 'red', 'https://cdn.hoanghamobile.com/i/preview/Uploads/2022/10/24/image-removebg-preview_638022082728321716.png', 15000000, 1450000, 5, 8, 91),
(60, 'red', 'https://cdn.hoanghamobile.com/i/preview/Uploads/2023/07/26/samsung-galaxy-tab-s9-5g-13.png', 10000000, 9000000, 5, 16, 92),
(61, 'blue', 'https://cdn.hoanghamobile.com/i/preview/Uploads/2023/10/04/galaxy-tab-s9-fe-plus-gray-product-image-front.png', 12000000, 0, 5, 16, 93),
(62, 'blue', 'https://cdn.hoanghamobile.com/i/preview/Uploads/2021/09/15/image-removebg-preview-26.png', 20000000, 19500000, 5, 32, 94),
(63, 'black', 'https://cdn.hoanghamobile.com/i/preview/Uploads/2023/04/12/oppo-pad-air-tim-1.png', 22000000, 21500000, 5, 16, 95),
(64, 'red', 'https://cdn.hoanghamobile.com/i/preview/Uploads/2021/09/15/image-removebg-preview-27.png', 15000000, 14500000, 5, 16, 96)
"""

def strip_accents(text):
    text = unicodedata.normalize('NFD', text)
    text = text.encode('ascii', 'ignore').decode('utf-8')
    return str(text)

def to_slug(text):
    text = strip_accents(text).lower()
    text = re.sub(r'[^a-z0-9]+', '-', text)
    return text.strip('-')

lines_prod = [l.strip() for l in data_products.strip().split('\n') if l.strip()]

seen_names = {}
old_to_new_id = {}
current_id = 789
out_products = []

for line in lines_prod:
    m = re.match(r'\((\d+),\s*\'(.*?)\',\s*(\d+),\s*(\d+)\),?', line)
    if not m:
        m = re.match(r'\((\d+),\s*"(.*?)",\s*(\d+),\s*(\d+)\),?', line)
    if m:
        old_id = m.group(1)
        name_raw = m.group(2)
        
        if name_raw in seen_names:
            old_to_new_id[old_id] = seen_names[name_raw]
            continue
            
        seen_names[name_raw] = current_id
        old_to_new_id[old_id] = current_id
        
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
        
        out_products.append(f"({current_id}, '{name}', '{desc}', {brand_id}, '{category}', '{slug}', '{image_url}', NOW(), NOW())")
        
        current_id += 1

with open('final_products.sql', 'w', encoding='utf-8') as f:
    f.write("INSERT INTO products (id, name, description, brand_id, category, slug, image_url, created_at, updated_at) VALUES\n")
    for i, out in enumerate(out_products):
        if i == len(out_products) - 1:
            f.write(out + ';\n')
        else:
            f.write(out + ',\n')


# Now variants
lines_var = [l.strip() for l in data_variants.strip().split('\n') if l.strip()]

seen_variants = set()
out_variants = []

for line in lines_var:
    m = re.match(r'\((\d+),\s*\'(.*?)\',\s*\'(.*?)\',\s*(\d+),\s*(\d+),\s*(\d+),\s*(\d+),\s*(\d+)\),?', line)
    if m:
        id_val = m.group(1)
        color = m.group(2)
        
        # fix the 'Whitehttps://...' bug
        if color.startswith('Whitehttps://'):
            color = 'White'
            
        color = color.title().strip()
        
        image = m.group(3)
        price = m.group(4)
        price_sale = m.group(5)
        quantity = m.group(6)
        ram = m.group(7)
        old_product_id = m.group(8)
        
        if old_product_id not in old_to_new_id:
            continue
            
        new_prod_id = old_to_new_id[old_product_id]
        
        key = (new_prod_id, color, ram)
        if key in seen_variants:
            continue
        seen_variants.add(key)
        
        actual_price = price
        storage = f"{ram}GB"
        sku = f"PROD{new_prod_id}-{color[:3].upper()}-{storage}"
        
        out_variants.append(f"({new_prod_id}, '{sku}', '{color}', '{storage}', {actual_price}.00, {quantity}, '{image}', NOW(), NOW())")

with open('final_variants.sql', 'w', encoding='utf-8') as f:
    f.write("INSERT INTO product_variants (product_id, sku, color, storage, price, stock_quantity, image_url, created_at, updated_at) VALUES\n")
    for i, out in enumerate(out_variants):
        if i == len(out_variants) - 1:
            f.write(out + ';\n')
        else:
            f.write(out + ',\n')

