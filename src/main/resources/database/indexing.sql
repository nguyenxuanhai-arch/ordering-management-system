create index idx_order_user
    on orders (user_id);

create index idx_order_user_status
    on orders (user_id, status);

create index idx_cart_item_cart
    on cart_item (cart_id);

create index idx_cart_item_product
    on cart_item (product_id);

create index idx_notification_created
    on notifications (created_at);

create index idx_order_item_order
    on order_item (order_id);

create index idx_product_category
    on products (category);

create index idx_product_name
    on products (name);

create index idx_user_email
    on users (email);

create index idx_user_notification_user
    on user_notification (user_id);

create index idx_user_notification_user_read
    on user_notification(user_id, is_read);
