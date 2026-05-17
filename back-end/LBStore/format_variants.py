import re

data = """
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

lines = [l.strip() for l in data.strip().split('\n') if l.strip()]

out_lines = []
seen = set()

for line in lines:
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
        product_id = m.group(8)
        
        # Determine actual price to insert (if price_sale > 0 we can use it, else price; but test-data uses price as the main value, let's keep original price to be safe)
        actual_price = price
        
        key = (product_id, color, ram)
        if key in seen:
            continue
        seen.add(key)
        
        storage = f"{ram}GB"
        
        sku = f"PROD{product_id}-{color[:3].upper()}-{storage}"
        
        out_lines.append(f"({product_id}, '{sku}', '{color}', '{storage}', {actual_price}.00, {quantity}, '{image}', NOW(), NOW())")

with open('output_variants.sql', 'w', encoding='utf-8') as f:
    f.write("INSERT INTO product_variants (product_id, sku, color, storage, price, stock_quantity, image_url, created_at, updated_at) VALUES\n")
    for i, out in enumerate(out_lines):
        if i == len(out_lines) - 1:
            f.write(out + ';\n')
        else:
            f.write(out + ',\n')
