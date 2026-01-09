create table blacklisted_tokens
(
    id          bigint auto_increment
        primary key,
    created_at  datetime(6) null,
    expiry_date datetime(6) not null,
    token       longtext    not null,
    updated_at  datetime(6) null,
    user_id     bigint      null
);

create table notifications
(
    id         bigint auto_increment
        primary key,
    body       varchar(255) null,
    created_at datetime(6)  null,
    title      varchar(255) null
);

create table products
(
    id          bigint auto_increment
        primary key,
    category    varchar(255)   null,
    description varchar(255)   null,
    img_url     varchar(255)   null,
    name        varchar(255)   null,
    price       decimal(38, 2) null,
    quantities  int            null,
    size        varchar(255)   null
);

create table roles
(
    id   bigint auto_increment
        primary key,
    name varchar(255) null
);

create table users
(
    id       bigint auto_increment
        primary key,
    address  varchar(255) null,
    email    varchar(255) null,
    password varchar(255) null,
    phone    varchar(255) null,
    constraint UK6dotkott2kjsp8vw4d0m25fb7
        unique (email)
);

create table carts
(
    id      bigint auto_increment
        primary key,
    user_id bigint null,
    constraint UK64t7ox312pqal3p7fg9o503c2
        unique (user_id),
    constraint FKb5o626f86h46m4s7ms6ginnop
        foreign key (user_id) references users (id)
);

create table cart_item
(
    quantity   int    null,
    product_id bigint not null,
    cart_id    bigint not null,
    primary key (cart_id, product_id),
    constraint FKlqwuo55w1gm4779xcu3t4wnrd
        foreign key (cart_id) references carts (id),
    constraint FKqkqmvkmbtiaqn2nfqf25ymfs2
        foreign key (product_id) references products (id)
);

create table orders
(
    id      bigint auto_increment
        primary key,
    status  varchar(255) null,
    user_id bigint       null,
    constraint FK32ql8ubntj5uh44ph9659tiih
        foreign key (user_id) references users (id)
);

create table order_item
(
    id         bigint auto_increment
        primary key,
    created_at datetime(6) null,
    order_id   bigint      null,
    product_id bigint      null,
    constraint FKc5uhmwioq5kscilyuchp4w49o
        foreign key (product_id) references products (id),
    constraint FKt4dc2r9nbvbujrljv3e23iibt
        foreign key (order_id) references orders (id)
);

create table refresh_tokens
(
    id            bigint auto_increment
        primary key,
    created_at    datetime(6) null,
    expiry_date   datetime(6) not null,
    refresh_token text        not null,
    updated_at    datetime(6) null,
    user_id       bigint      null,
    constraint FK1lih5y2npsf8u5o3vhdb9y0os
        foreign key (user_id) references users (id)
);

create table user_notification
(
    id              bigint auto_increment
        primary key,
    is_read         bit    null,
    notification_id bigint null,
    user_id         bigint null,
    constraint FKc2d7aih8weit50jlu4q57cvs
        foreign key (user_id) references users (id),
    constraint FKp137d22f65l9kjbqjgfb37oy
        foreign key (notification_id) references notifications (id)
);

create table user_role
(
    user_id bigint not null,
    role_id bigint not null,
    primary key (role_id, user_id),
    constraint FKj345gk1bovqvfame88rcx7yyx
        foreign key (user_id) references users (id),
    constraint FKt7e7djp752sqn6w22i6ocqy6q
        foreign key (role_id) references roles (id)
);

