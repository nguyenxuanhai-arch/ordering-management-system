SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE blacklisted_tokens;
TRUNCATE TABLE refresh_tokens;
TRUNCATE TABLE user_notification;
TRUNCATE TABLE order_item;
TRUNCATE TABLE orders;
TRUNCATE TABLE cart_item;
TRUNCATE TABLE carts;
TRUNCATE TABLE user_role;
TRUNCATE TABLE notifications;
TRUNCATE TABLE products;
TRUNCATE TABLE users;
TRUNCATE TABLE roles;

SET FOREIGN_KEY_CHECKS = 1;
INSERT INTO roles (name)
VALUES ('ROLE_USER'), ('ROLE_ADMIN');

INSERT INTO users (email, password, phone, address)
SELECT
    CONCAT(
            SUBSTRING(MD5(RAND()), 1, 12),
            '@',
            SUBSTRING(MD5(RAND()), 1, 6),
            '.com'
    ) AS email,

    '$2a$12$K8Jz6k5Yz9qzN1mZ4RkK3O7F6m9zXkZxRz6M1ZkF0wYJZpW9nQk6a' AS password,

    CONCAT(
            '0',
            FLOOR(100000000 + RAND() * 899999999)
    ) AS phone,

    CONCAT(
            'Addr-',
            SUBSTRING(MD5(RAND()), 1, 12)
    ) AS address
FROM (
         SELECT 1
         FROM
             (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
              UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a,
             (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
              UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b,
             (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
              UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) c,
             (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
              UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) d,
             (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
              UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) e
             LIMIT 100000
     ) t;


INSERT INTO user_role (user_id, role_id)
SELECT u.id, r.id
FROM users u
         JOIN roles r ON r.name = 'ROLE_USER';

INSERT INTO products
(name, price, quantities, category, size, img_url, description)
SELECT
    -- name: random, không theo thứ tự
    CONCAT(
            ELT(FLOOR(1 + RAND()*6),
                'Ultra','Super','Pro','Max','Eco','Prime'
            ),
            ' ',
            ELT(FLOOR(1 + RAND()*6),
                'Sneaker','TShirt','Jacket','Bag','Watch','Hat'
            ),
            ' ',
            SUBSTRING(MD5(RAND()), 1, 4)
    ) AS name,

    -- price: 10k → 5tr
    ROUND(10000 + RAND() * 4990000, 2) AS price,

    -- quantities: 0 → 1000
    FLOOR(RAND() * 1000) AS quantities,

    -- category: CAT_A → CAT_J
    ELT(FLOOR(1 + RAND()*10),
        'CAT_A','CAT_B','CAT_C','CAT_D','CAT_E',
        'CAT_F','CAT_G','CAT_H','CAT_I','CAT_J'
    ) AS category,

    -- size
    ELT(FLOOR(1 + RAND()*5), 'XS','S','M','L','XL') AS size,

    -- img_url random (nhưng vẫn hợp lệ)
    CONCAT(
        'https://img.test/product_',
        FLOOR(RAND()*1000),
        '.png'
    ) AS img_url,

    -- description random
    CONCAT(
        'Product ',
        SUBSTRING(MD5(RAND()), 1, 16),
        ' description'
    ) AS description
FROM (
    SELECT 1
    FROM
    (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a,
    (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b,
    (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) c,
    (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) d,
    (SELECT 0 UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) e
    LIMIT 20000
    ) t;


INSERT INTO carts (user_id)
SELECT id FROM users;

INSERT INTO cart_item (cart_id, product_id, quantity)
SELECT
    c.id,
    p.id,
    FLOOR(RAND() * 3) + 1
FROM carts c
         JOIN products p
WHERE p.id % 7 = 0;

INSERT INTO orders (user_id, status)
SELECT
    u.id,
    ELT(FLOOR(1 + RAND()*4), 'PENDING','PAID','SHIPPED','CANCELLED')
FROM users u
         JOIN (SELECT 1 UNION ALL SELECT 2) t;

INSERT INTO order_item (order_id, product_id)
SELECT
    o.id,
    p.id,
    NOW()
FROM orders o
         JOIN products p
WHERE p.id % 9 = 0;

INSERT INTO notification (title, body, created_at)
SELECT
    CONCAT('Notify ', n),
    'System notification',
    NOW()
FROM (
         SELECT a.n + b.n*10 + c.n*100 + 1 AS n
         FROM
             (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
              UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) a,
             (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
              UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) b,
             (SELECT 0 n UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
              UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9) c
     ) x
WHERE n <= 1000;

INSERT INTO user_notification (user_id, notification_id, is_read)
SELECT
    u.id,
    n.id,
    b'0'
FROM users u
         JOIN notifications n ON n.id = 1
LIMIT 10000;

UPDATE users
SET name = CONCAT(
        ELT(FLOOR(1 + RAND() * 15),
            'Nguyễn', 'Trần', 'Lê', 'Phạm', 'Hoàng',
            'Huỳnh', 'Phan', 'Vũ', 'Võ', 'Đặng',
            'Bùi', 'Đỗ', 'Hồ', 'Ngô', 'Dương'
        ),
        ' ',
        ELT(FLOOR(1 + RAND() * 8),
            'Văn', 'Thị', 'Hữu', 'Đức',
            'Quang', 'Gia', 'Minh', 'Thanh'
        ),
        ' ',
        ELT(FLOOR(1 + RAND() * 20),
            'An', 'Anh', 'Bình', 'Cường', 'Dũng',
            'Hải', 'Hiếu', 'Hùng', 'Khánh', 'Khoa',
            'Long', 'Minh', 'Nam', 'Phong', 'Quân',
            'Sơn', 'Thắng', 'Toàn', 'Trung', 'Vinh'
        )
           )
WHERE name IS NULL
    LIMIT 10000;
