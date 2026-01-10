-- ============================================================================
-- PERFORMANCE OPTIMIZATION: Database Indexes & Migrations
-- ============================================================================
-- These indexes are recommended to improve query performance after code fixes
-- Run these migrations in production database

-- ============================================================================
-- 1. COVERING INDEX FOR PAGINATION
-- ============================================================================
-- Allows MySQL to answer pagination queries without accessing main table
-- Used for: SELECT o FROM Order o ORDER BY created_at DESC LIMIT 10

ALTER TABLE orders
ADD INDEX idx_order_pagination (created_at DESC, id);

-- ============================================================================
-- 2. INDEX FOR ORDER ITEMS WITH PRODUCT
-- ============================================================================
-- Improves: SELECT oi FROM OrderItem oi WHERE oi.order_id IN (...)
-- Used when batch loading items for a set of orders

ALTER TABLE order_item
ADD INDEX idx_order_item_batch_load (order_id, product_id);

-- ============================================================================
-- 3. INDEX FOR PRODUCT NAME FILTERING
-- ============================================================================
-- Used for: WHERE product.name LIKE '%...'
-- Already exists in Product.java, but ensure it's created

ALTER TABLE products
ADD INDEX idx_product_name_search (name);

-- ============================================================================
-- 4. INDEX FOR USER FILTERING (email, name, phone)
-- ============================================================================
-- Used for keyword filtering on user fields
-- Email already has unique index, but add for name/phone

ALTER TABLE users
ADD INDEX idx_user_search (name, email, phone);

-- ============================================================================
-- 5. COMPOSITE INDEX FOR STATUS FILTERING BY USER
-- ============================================================================
-- Improves: WHERE user_id = ? AND status = ?
-- Already exists as idx_order_user_status, but verify it's created

ALTER TABLE orders
ADD INDEX idx_order_status_lookup (status, user_id);

-- ============================================================================
-- VERIFICATION QUERIES
-- ============================================================================
-- Run these to verify indexes are created correctly

-- Show all indexes on orders table
SHOW INDEXES FROM orders;

-- Show all indexes on order_item table
SHOW INDEXES FROM order_item;

-- Show all indexes on users table
SHOW INDEXES FROM users;

-- Show all indexes on products table
SHOW INDEXES FROM products;

-- ============================================================================
-- QUERY EXECUTION PLANS (for verification)
-- ============================================================================
-- Run these BEFORE and AFTER applying indexes to see improvement

-- Pagination query (should use idx_order_pagination)
EXPLAIN SELECT o.*
FROM orders o
LEFT JOIN users u ON o.user_id = u.id
ORDER BY o.created_at DESC
LIMIT 10;

-- Batch item loading (should use idx_order_item_batch_load)
EXPLAIN SELECT oi.*
FROM order_item oi
WHERE oi.order_id IN (1, 2, 3, 4, 5);

-- Product name filtering (should use idx_product_name_search)
EXPLAIN SELECT o.*
FROM orders o
INNER JOIN order_item oi ON o.id = oi.order_id
INNER JOIN products p ON oi.product_id = p.id
WHERE p.name LIKE '%laptop%';

-- User search (should use idx_user_search)
EXPLAIN SELECT o.*
FROM orders o
INNER JOIN users u ON o.user_id = u.id
WHERE u.name LIKE '%john%' OR u.email LIKE '%john%';

-- ============================================================================
-- PERFORMANCE MONITORING
-- ============================================================================
-- After applying indexes, monitor query performance

-- Find slow queries in MySQL (adjust timeout as needed)
SET GLOBAL slow_query_log = 'ON';
SET GLOBAL long_query_time = 2; -- Log queries slower than 2 seconds

-- Check slow query log
-- SHOW VARIABLES LIKE 'slow_query%';

-- ============================================================================
-- ROLLBACK (if needed)
-- ============================================================================
-- If performance doesn't improve, remove indexes with:

-- ALTER TABLE orders DROP INDEX idx_order_pagination;
-- ALTER TABLE order_item DROP INDEX idx_order_item_batch_load;
-- ALTER TABLE products DROP INDEX idx_product_name_search;
-- ALTER TABLE users DROP INDEX idx_user_search;
-- ALTER TABLE orders DROP INDEX idx_order_status_lookup;

-- ============================================================================
-- MONITORING AFTER FIXES
-- ============================================================================
-- Use these queries to monitor index usage and performance

-- Find unused indexes (adapt for your database)
-- SELECT * FROM performance_schema.table_io_waits_summary_by_index_usage;

-- Monitor query execution time per index
-- SELECT * FROM performance_schema.events_statements_summary_by_digest
-- ORDER BY SUM_TIMER_WAIT DESC
-- LIMIT 20;

-- Get index statistics
-- SELECT * FROM information_schema.STATISTICS
-- WHERE TABLE_SCHEMA = 'demodb'
-- AND TABLE_NAME IN ('orders', 'order_item', 'users', 'products')
-- ORDER BY TABLE_NAME, SEQ_IN_INDEX;

-- ============================================================================
-- ADVANCED OPTIMIZATION (Optional)
-- ============================================================================

-- Partition large tables for even better performance (if 100M+ rows)
-- Recommended: Partition orders by created_at (monthly)
-- ALTER TABLE orders PARTITION BY RANGE (YEAR(created_at) * 12 + MONTH(created_at)) (
--   PARTITION p202301 VALUES LESS THAN (202302),
--   PARTITION p202302 VALUES LESS THAN (202303),
--   ...
-- );

-- Archive old orders to separate table
-- CREATE TABLE orders_archive LIKE orders;
-- INSERT INTO orders_archive
-- SELECT * FROM orders WHERE created_at < DATE_SUB(NOW(), INTERVAL 1 YEAR);
-- DELETE FROM orders WHERE created_at < DATE_SUB(NOW(), INTERVAL 1 YEAR);


