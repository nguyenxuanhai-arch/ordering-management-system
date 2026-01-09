# Environment Variables Guide

## Tổng quan

File `.env` chứa tất cả các cấu hình môi trường cho ứng dụng. **KHÔNG commit file này lên Git!**

---

## Database Configuration

### MYSQL_PORT
- **Mô tả**: Port để kết nối MySQL
- **Default**: `3306`
- **Example**: `3306`
- **Lưu ý**: Nếu dùng Docker, không nên expose port này ra ngoài trong production

### MYSQL_ROOT_PASSWORD
- **Mô tả**: Mật khẩu root của MySQL
- **Requirements**:
  - Tối thiểu 8 ký tự
  - Nên có chữ, số, ký tự đặc biệt
  - **KHÔNG sử dụng**: `password`, `123456`, `root`
- **Example**: 
  - Local: `root123456`
  - Production: `P@ssw0rd!SecureRoot#2024`

### MYSQL_DATABASE
- **Mô tả**: Tên database chính
- **Requirements**: Chỉ dùng letters, numbers, underscores
- **Default**: `ordering_system`
- **Example**: `ordering_system`, `oms_db`, `order_management`

### MYSQL_USER
- **Mô tả**: Username để kết nối database
- **Requirements**:
  - Không phải `root`
  - Chỉ letters, numbers, underscores
  - Tối thiểu 3 ký tự
- **Example**: `oms_user`, `appuser`, `ordering_app`

### MYSQL_PASSWORD
- **Mô tả**: Mật khẩu cho MYSQL_USER
- **Requirements**:
  - Tối thiểu 8 ký tự
  - Nên có mix của: chữ hoa, chữ thường, số, ký tự đặc biệt
  - **KHÔNG giống** MYSQL_ROOT_PASSWORD
  - **KHÔNG sử dụng**: `password`, `123456`
- **Example**:
  - Local: `oms123456`
  - Production: `Secure@Pass123#User2024`

---

## Security Configuration

### JWT_SECRET
- **Mô tả**: Secret key để generate/verify JWT tokens
- **Importance**: ⚠️ **CRITICAL** - Nếu expose, tất cả tokens sẽ không an toàn
- **Requirements**:
  - Tối thiểu 32 ký tự
  - Nên dùng: letters, numbers, special chars, dashes, underscores
  - **KHÔNG giống** database passwords
- **Cách generate**:
  ```bash
  # macOS/Linux
  openssl rand -base64 32
  
  # Windows (PowerShell)
  [Convert]::ToBase64String((1..32 | ForEach-Object {[byte](Get-Random -Max 256)}))
  ```
- **Example**:
  - Local: `my_super_secret_jwt_key_at_least_32_characters_long_xxx`
  - Production: Generate with openssl command trên

### JWT_ISSUER
- **Mô tả**: URL của ứng dụng (issuer của JWT token)
- **Requirements**: Phải là URL hợp lệ
- **Example**:
  - Local: `http://localhost:8080`
  - Staging: `https://staging.yourdomain.com`
  - Production: `https://api.yourdomain.com`

---

## Application Configuration

### BACKEND_IMAGE
- **Mô tả**: Docker image name:tag cho backend
- **Format**: `name:tag`
- **Example**: 
  - Development: `oms-backend:latest`
  - Production: `oms-backend:1.0.0`, `registry.example.com/oms:v1.0.0`
- **Lưu ý**: Nếu sử dụng private registry, cần configure Docker login

### APP_PORT
- **Mô tả**: Port để expose ứng dụng ra ngoài
- **Default**: `8080`
- **Requirements**: Port phải available (1024-65535)
- **Example**:
  - Local: `8080`
  - Production: Thường là `80` hoặc `443` (qua reverse proxy)

---

## Environment-Specific Examples

### Development Environment

```env
# Database
MYSQL_PORT=3306
MYSQL_ROOT_PASSWORD=root123456
MYSQL_DATABASE=ordering_system_dev
MYSQL_USER=dev_user
MYSQL_PASSWORD=dev_password_123

# Security
JWT_SECRET=dev_secret_key_at_least_32_characters_for_development_purposes
JWT_ISSUER=http://localhost:8080

# Application
BACKEND_IMAGE=oms-backend:dev
APP_PORT=8080
```

### Staging Environment

```env
# Database
MYSQL_PORT=3306
MYSQL_ROOT_PASSWORD=Stg@SecureRoot#Password2024
MYSQL_DATABASE=ordering_system_staging
MYSQL_USER=staging_oms_user
MYSQL_PASSWORD=Stg@UserPass#Secure2024

# Security
JWT_SECRET=staging_secret_key_generated_from_openssl_or_similar_tools_at_least_32_chars
JWT_ISSUER=https://staging-api.yourdomain.com

# Application
BACKEND_IMAGE=oms-backend:staging
APP_PORT=8080
```

### Production Environment

```env
# Database
MYSQL_PORT=3306
MYSQL_ROOT_PASSWORD=Prod@VerySecureRoot#Password2024!Special
MYSQL_DATABASE=ordering_system_prod
MYSQL_USER=prod_oms_user
MYSQL_PASSWORD=Prod@VerySecure#UserPassword2024!Special

# Security
JWT_SECRET=prod_secret_generated_by_openssl_with_strong_entropy_at_least_32_chars_minimum
JWT_ISSUER=https://api.yourdomain.com

# Application
BACKEND_IMAGE=oms-backend:1.0.0
APP_PORT=8080
```

---

## Optional Advanced Configuration

### Database Connection Pool
Để tăng performance, có thể thêm vào `application.properties`:

```properties
# Connection Pool Settings
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5
spring.datasource.hikari.connection-timeout=30000
spring.datasource.hikari.idle-timeout=600000
spring.datasource.hikari.max-lifetime=1800000
```

### Logging Configuration
```env
LOG_LEVEL=INFO
LOG_FILE=/app/logs/application.log
```

### Email Configuration (nếu cần)
```env
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password
MAIL_FROM=noreply@yourdomain.com
```

### AWS S3 (nếu upload files)
```env
AWS_ACCESS_KEY_ID=your_aws_access_key
AWS_SECRET_ACCESS_KEY=your_aws_secret_key
AWS_S3_BUCKET=your-bucket-name
AWS_REGION=us-east-1
```

---

## Security Best Practices

### ✅ DO's
- ✅ Generate secrets using `openssl` hoặc password manager
- ✅ Use strong passwords (tối thiểu 12 ký tự cho production)
- ✅ Rotate passwords định kỳ (quarterly)
- ✅ Use different passwords cho dev/staging/prod
- ✅ Store `.env` securely (not in Git)
- ✅ Use secrets management tools (AWS Secrets Manager, HashiCorp Vault)
- ✅ Restrict file permissions: `chmod 600 .env`
- ✅ Audit who has access to `.env`

### ❌ DON'Ts
- ❌ Commit `.env` file lên Git
- ❌ Hardcode secrets trong code
- ❌ Share passwords via email/chat
- ❌ Use same password cho multiple envs
- ❌ Use simple passwords like `password123`
- ❌ Log sensitive values
- ❌ Expose `.env` file to web server

---

## Validating Configuration

### Kiểm tra file .env
```bash
# Kiểm tra file tồn tại
ls -la .env

# Kiểm tra permissions
ls -l .env  # Should be: -rw------- (600)

# Fix permissions nếu cần
chmod 600 .env
```

### Test Database Connection
```bash
# Nếu MySQL chạy locally
mysql -u ${MYSQL_USER} -p${MYSQL_PASSWORD} -h localhost -P ${MYSQL_PORT} -D ${MYSQL_DATABASE}

# Nếu MySQL chạy trong Docker
docker exec oms-mysql mysql -u ${MYSQL_USER} -p${MYSQL_PASSWORD} -D ${MYSQL_DATABASE} -e "SELECT 1;"
```

### Test JWT Secret
```bash
# Kiểm tra độ dài (phải >= 32)
echo ${JWT_SECRET} | wc -c
```

### Test Application Startup
```bash
# Run with explicit env vars
docker-compose up -d
docker logs oms-app | grep -i "started"
```

---

## Troubleshooting

### "Access denied for user"
- Kiểm tra `MYSQL_USER` và `MYSQL_PASSWORD`
- Verify user được tạo: `SHOW GRANTS FOR 'user'@'%';`
- Check encoding: Passwords không nên có ký tự đặc biệt quá nhiều

### "Connection refused"
- Kiểm tra `MYSQL_PORT` (default 3306)
- Kiểm tra MySQL container running: `docker ps | grep mysql`
- Kiểm tra network connectivity

### "Invalid JWT secret"
- Kiểm tra độ dài >= 32 ký tự
- Kiểm tra không có whitespace ở đầu/cuối
- Regenerate nếu cần: `openssl rand -base64 32`

### Application won't start
- Xem logs: `docker logs oms-app`
- Kiểm tra tất cả required vars đã set
- Verify syntax trong `.env` (không có quotes không cần thiết)

---

## Migrating Between Environments

### From Dev to Staging
```bash
# 1. Generate new secrets
openssl rand -base64 32 > new_jwt_secret.txt

# 2. Update .env
cp .env-example .env.staging
# Edit với staging values

# 3. Backup database
docker exec oms-mysql mysqldump -u root -p ordering_system > backup.sql

# 4. Deploy
docker-compose down
# Update .env
docker-compose up -d
```

### From Staging to Production
```bash
# Same process nhưng production-grade
# - Stronger passwords
# - HTTPS enabled
# - Proper backup strategy
# - Monitoring enabled
```

---

## Reference

- [Spring Boot Application Properties](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html)
- [MySQL Connection String Format](https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-jdbc-url-format.html)
- [JWT.io](https://jwt.io)
- [OWASP Password Guidelines](https://owasp.org/www-community/authentication/Password_Strength_Controls)

---

**Last Updated**: January 2026
**Version**: 1.0.0

