# Hướng Dẫn Deploy Ordering Management System

## 📋 Mục lục
1. [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
2. [Chuẩn bị môi trường](#chuẩn-bị-môi-trường)
3. [Cấu hình dự án](#cấu-hình-dự-án)
4. [Deploy cục bộ (Local)](#deploy-cục-bộ-local)
5. [Deploy với Docker](#deploy-với-docker)
6. [Deploy lên server](#deploy-lên-server)
7. [Kiểm tra & Troubleshooting](#kiểm-tra--troubleshooting)
8. [Bảo trì & Giám sát](#bảo-trì--giám-sát)

---

## Yêu cầu hệ thống

### Tối thiểu
- **Java**: JDK 21 trở lên
- **Maven**: 3.9.0 trở lên
- **MySQL**: 8.0 trở lên
- **RAM**: 2GB
- **Disk**: 1GB (tùy theo dữ liệu)

### Khuyên dùng
- **Java**: JDK 21 LTS
- **Maven**: 3.9.6 trở lên
- **MySQL**: 8.0.35 trở lên
- **Docker**: 20.10+ (nếu sử dụng containerization)
- **Docker Compose**: 2.0+ (nếu sử dụng)
- **RAM**: 4GB
- **Disk**: 10GB

### Công cụ hỗ trợ
- Git
- Postman/cURL (để test API)
- IDE: IntelliJ IDEA / VS Code / Eclipse

---

## Chuẩn bị môi trường

### 1. Clone Repository
```bash
git clone <repository-url>
cd ordering-management-system
```

### 2. Cài đặt Java
**Windows:**
```bash
# Kiểm tra Java đã cài chưa
java -version

# Nếu chưa, tải từ https://www.oracle.com/java/technologies/downloads/#java21
# Hoặc sử dụng package manager
```

**Linux/Mac:**
```bash
# Ubuntu/Debian
sudo apt-get install openjdk-21-jdk

# macOS (với Homebrew)
brew install openjdk@21

# Kiểm tra
java -version
```

### 3. Cài đặt MySQL
**Windows:**
```bash
# Sử dụng installer từ https://dev.mysql.com/downloads/mysql/
# Hoặc dùng Chocolatey
choco install mysql

# Khởi động MySQL
mysql -u root -p
```

**Linux (Ubuntu/Debian):**
```bash
sudo apt-get update
sudo apt-get install mysql-server

# Khởi động
sudo systemctl start mysql
sudo systemctl enable mysql

# Kiểm tra
mysql --version
```

**macOS:**
```bash
brew install mysql

# Khởi động
brew services start mysql

# Kiểm tra
mysql --version
```

### 4. Cài đặt Maven (nếu cần)
```bash
# Windows (Chocolatey)
choco install maven

# Linux/Mac (Homebrew)
brew install maven

# Kiểm tra
mvn --version
```

---

## Cấu hình dự án

### 1. Tạo file `.env` từ `.env-example`
```bash
cp .env-example .env
```

### 2. Chỉnh sửa file `.env` với các giá trị thực tế

**Ví dụ cho môi trường Local:**
```env
# MySQL Configuration
MYSQL_PORT=3306
MYSQL_ROOT_PASSWORD=root_password_123
MYSQL_DATABASE=ordering_system
MYSQL_USER=oms_user
MYSQL_PASSWORD=oms_password_123

# JWT Configuration
JWT_SECRET=your_long_secret_key_min_32_chars_for_security_xxxxxxxx
JWT_ISSUER=http://localhost:8080

# Backend Configuration
BACKEND_IMAGE=oms-backend:latest
APP_PORT=8080
```

**Ví dụ cho môi trường Production:**
```env
# MySQL Configuration
MYSQL_PORT=3306
MYSQL_ROOT_PASSWORD=strong_root_password_with_special_chars_!@#$%
MYSQL_DATABASE=ordering_system_prod
MYSQL_USER=oms_prod_user
MYSQL_PASSWORD=strong_user_password_with_special_chars_!@#$%

# JWT Configuration
JWT_SECRET=generate_with_openssl_rand_-base64_32
JWT_ISSUER=https://yourdomain.com

# Backend Configuration
BACKEND_IMAGE=oms-backend:1.0.0
APP_PORT=8080
```

### 3. Cấu hình Database

**Tạo database và user (MySQL):**
```sql
-- Đăng nhập MySQL
mysql -u root -p

-- Tạo database
CREATE DATABASE ordering_system CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Tạo user
CREATE USER 'oms_user'@'localhost' IDENTIFIED BY 'oms_password_123';

-- Cấp quyền
GRANT ALL PRIVILEGES ON ordering_system.* TO 'oms_user'@'localhost';
FLUSH PRIVILEGES;

-- Kiểm tra
SHOW DATABASES;
SELECT User FROM mysql.user;
```

### 4. Cấu hình Spring Boot (application.properties)

Mở file `src/main/resources/application.properties`:

**Cho môi trường Local:**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ordering_system?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=oms_user
spring.datasource.password=oms_password_123
spring.jpa.hibernate.ddl-auto=update

jwt.secret=your_long_secret_key_min_32_chars_for_security_xxxxxxxx
jwt.issuer=http://localhost:8080
```

**Cho môi trường Production:**
```properties
spring.datasource.url=jdbc:mysql://db-server:3306/ordering_system_prod?useSSL=true&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=validate

jwt.secret=${JWT_SECRET}
jwt.issuer=${JWT_ISSUER}

# Bảo mật
server.ssl.enabled=true
server.ssl.key-store=${KEYSTORE_PATH}
server.ssl.key-store-password=${KEYSTORE_PASSWORD}
```

---

## Deploy cục bộ (Local)

### Cách 1: Chạy trực tiếp với Maven

**Bước 1: Build dự án**
```bash
mvn clean install
```

**Bước 2: Chạy ứng dụng**
```bash
mvn spring-boot:run
```

Ứng dụng sẽ chạy tại `http://localhost:8080`

**Bước 3: Kiểm tra health check**
```bash
curl http://localhost:8080/actuator/health
```

### Cách 2: Chạy JAR file

**Bước 1: Build JAR**
```bash
mvn clean package
```

**Bước 2: Chạy JAR**
```bash
java -jar target/ordering-management-system-0.0.1-SNAPSHOT.jar
```

**Bước 3: Chạy JAR với tùy chỉnh cấu hình**
```bash
java -jar target/ordering-management-system-0.0.1-SNAPSHOT.jar \
  --spring.datasource.url=jdbc:mysql://localhost:3306/ordering_system \
  --spring.datasource.username=oms_user \
  --spring.datasource.password=oms_password_123 \
  --jwt.secret=your_secret_key \
  --jwt.issuer=http://localhost:8080
```

**Bước 4: Kiểm tra logs**
```bash
tail -f logs/application.log
```

---

## Deploy với Docker

### Yêu cầu
- Docker 20.10+ đã cài đặt
- Docker Compose 2.0+ đã cài đặt

### Cách 1: Chạy với Docker Compose (Khuyên dùng)

**Bước 1: Chuẩn bị file `.env`**
```bash
cp .env-example .env
# Chỉnh sửa .env với giá trị thực tế
```

**Bước 2: Build image**
```bash
docker-compose build
```

**Bước 3: Khởi động container**
```bash
# Chạy ở foreground (để xem logs)
docker-compose up

# Hoặc chạy ở background
docker-compose up -d
```

**Bước 4: Kiểm tra trạng thái**
```bash
docker-compose ps
docker-compose logs -f oms-app
```

**Bước 5: Dừng container**
```bash
docker-compose down
```

**Bước 6: Xóa volume (nếu cần reset database)**
```bash
docker-compose down -v
```

### Cách 2: Chạy với Docker thủ công

**Bước 1: Build image**
```bash
docker build -t oms-backend:latest .
```

**Bước 2: Tạo network**
```bash
docker network create oms-network
```

**Bước 3: Chạy MySQL container**
```bash
docker run -d \
  --name oms-mysql \
  --network oms-network \
  -e MYSQL_ROOT_PASSWORD=root_password_123 \
  -e MYSQL_DATABASE=ordering_system \
  -e MYSQL_USER=oms_user \
  -e MYSQL_PASSWORD=oms_password_123 \
  -v mysql_data:/var/lib/mysql \
  -p 3306:3306 \
  mysql:8.0
```

**Bước 4: Chạy Application container**
```bash
docker run -d \
  --name oms-backend \
  --network oms-network \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://oms-mysql:3306/ordering_system?useSSL=false \
  -e SPRING_DATASOURCE_USERNAME=oms_user \
  -e SPRING_DATASOURCE_PASSWORD=oms_password_123 \
  -e JWT_SECRET=your_secret_key \
  -e JWT_ISSUER=http://localhost:8080 \
  -p 8080:8080 \
  oms-backend:latest
```

**Bước 5: Kiểm tra logs**
```bash
docker logs -f oms-backend
```

### Container Commands Hữu ích

```bash
# Xem container đang chạy
docker ps

# Xem logs
docker logs -f oms-backend

# Vào container shell
docker exec -it oms-backend /bin/bash

# Dừng container
docker stop oms-backend

# Khởi động lại
docker restart oms-backend

# Xóa container
docker rm oms-backend

# Xem resource usage
docker stats
```

---

## Deploy lên server

### A. Chuẩn bị Server

#### 1. SSH vào server
```bash
ssh user@server_ip
```

#### 2. Cập nhật hệ thống
```bash
# Linux (Ubuntu/Debian)
sudo apt-get update
sudo apt-get upgrade -y

# CentOS/RHEL
sudo yum update -y
```

#### 3. Cài đặt dependencies
```bash
# Java 21
sudo apt-get install openjdk-21-jdk -y

# MySQL (nếu chưa có)
sudo apt-get install mysql-server -y

# Docker (nếu sử dụng Docker)
sudo apt-get install docker.io docker-compose -y

# Git
sudo apt-get install git -y

# Kiểm tra
java -version
mysql --version
docker --version
```

#### 4. Tạo user cho application
```bash
sudo useradd -m -s /bin/bash app_user
sudo usermod -aG docker app_user  # Nếu sử dụng Docker
```

### B. Deploy với Docker Compose trên Server

#### 1. Clone repository
```bash
cd /opt
sudo git clone <repository-url> ordering-management-system
sudo chown -R app_user:app_user ordering-management-system
```

#### 2. Cấu hình môi trường
```bash
cd /opt/ordering-management-system
cp .env-example .env
# Chỉnh sửa với giá trị production
sudo nano .env
```

#### 3. Khởi động ứng dụng
```bash
docker-compose up -d
docker-compose logs -f
```

#### 4. Cấu hình Reverse Proxy (Nginx)
```bash
sudo apt-get install nginx -y
sudo nano /etc/nginx/sites-available/oms
```

**Nội dung file nginx config:**
```nginx
upstream oms_backend {
    server localhost:8080;
}

server {
    listen 80;
    server_name yourdomain.com;

    # Redirect HTTP to HTTPS
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl http2;
    server_name yourdomain.com;

    # SSL Certificate (sử dụng Let's Encrypt)
    ssl_certificate /etc/letsencrypt/live/yourdomain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/yourdomain.com/privkey.pem;

    # SSL Configuration
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;

    # Proxy settings
    client_max_body_size 100M;

    location / {
        proxy_pass http://oms_backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_read_timeout 90;
        proxy_connect_timeout 90;
    }

    # Static files caching
    location ~* \.(jpg|jpeg|png|gif|ico|css|js)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }
}
```

**Enable site:**
```bash
sudo ln -s /etc/nginx/sites-available/oms /etc/nginx/sites-enabled/
sudo systemctl restart nginx
```

#### 5. Cấu hình SSL với Let's Encrypt
```bash
sudo apt-get install certbot python3-certbot-nginx -y
sudo certbot certonly --nginx -d yourdomain.com
```

### C. Deploy thủ công (Jar file)

#### 1. Build JAR file trên local
```bash
mvn clean package -DskipTests
```

#### 2. Transfer JAR lên server
```bash
scp target/ordering-management-system-0.0.1-SNAPSHOT.jar user@server_ip:/opt/oms/
```

#### 3. Tạo Systemd service
```bash
sudo nano /etc/systemd/system/oms.service
```

**Nội dung:**
```ini
[Unit]
Description=Ordering Management System
After=network.target
StartLimitIntervalSec=0

[Service]
Type=simple
Restart=always
RestartSec=10
User=app_user
WorkingDirectory=/opt/oms
EnvironmentFile=/opt/oms/.env
ExecStart=/usr/bin/java -jar /opt/oms/ordering-management-system-0.0.1-SNAPSHOT.jar \
  --spring.datasource.url=${SPRING_DATASOURCE_URL} \
  --spring.datasource.username=${SPRING_DATASOURCE_USERNAME} \
  --spring.datasource.password=${SPRING_DATASOURCE_PASSWORD} \
  --jwt.secret=${JWT_SECRET} \
  --jwt.issuer=${JWT_ISSUER}

# Log configuration
StandardOutput=journal
StandardError=journal
SyslogIdentifier=oms

[Install]
WantedBy=multi-user.target
```

#### 4. Kích hoạt và chạy service
```bash
sudo systemctl daemon-reload
sudo systemctl enable oms
sudo systemctl start oms

# Kiểm tra status
sudo systemctl status oms

# Xem logs
sudo journalctl -u oms -f
```

---

## Kiểm tra & Troubleshooting

### Health Check

```bash
# API Health
curl -X GET http://localhost:8080/actuator/health

# Database connection
curl -X GET http://localhost:8080/actuator/db

# Xem tất cả endpoints
curl -X GET http://localhost:8080/actuator
```

### Logs & Debugging

```bash
# Xem logs (Local with Maven)
tail -f logs/application.log

# Docker logs
docker logs -f oms-app

# Systemd service logs
sudo journalctl -u oms -f

# Xem logs từ ngày cụ thể
sudo journalctl -u oms --since "2024-01-09" --until "2024-01-10"
```

### Common Issues

#### 1. Port Already in Use
```bash
# Tìm process dùng port 8080
lsof -i :8080  # Linux/Mac
netstat -ano | findstr :8080  # Windows

# Kill process
kill -9 <PID>  # Linux/Mac
taskkill /PID <PID> /F  # Windows
```

#### 2. MySQL Connection Failed
```bash
# Kiểm tra MySQL status
sudo systemctl status mysql

# Kiểm tra MySQL đang listen
sudo netstat -tlnp | grep 3306

# Test connection
mysql -u oms_user -p -h localhost -D ordering_system
```

#### 3. Docker Network Issues
```bash
# Kiểm tra networks
docker network ls

# Kiểm tra containers trên network
docker network inspect oms-network

# Restart containers
docker-compose restart
```

#### 4. Out of Memory
```bash
# Tăng heap memory (Local)
export JAVA_OPTS="-Xms512m -Xmx2g"
java -jar application.jar

# Trong Docker (docker-compose.yml)
environment:
  JAVA_OPTS: "-Xms512m -Xmx2g"
```

#### 5. Application won't start
```bash
# Check logs cho errors
docker logs oms-app
docker logs oms-mysql

# Kiểm tra database migration
docker exec oms-mysql mysql -u oms_user -p ordering_system -e "SHOW TABLES;"

# Reset database
docker-compose down -v
docker-compose up -d
```

### Performance Monitoring

```bash
# CPU & Memory usage
docker stats

# Database queries
docker exec oms-mysql mysql -u root -p -e "SHOW PROCESSLIST;"

# Application metrics
curl http://localhost:8080/actuator/metrics

# JVM memory
curl http://localhost:8080/actuator/metrics/jvm.memory.usage
```

---

## Bảo trì & Giám sát

### Backup Strategy

#### 1. Database Backup
```bash
# Tạo backup
docker exec oms-mysql mysqldump -u root -p ordering_system > backup_$(date +%Y%m%d_%H%M%S).sql

# Backup tự động (cron job)
0 2 * * * /usr/local/bin/backup-db.sh
```

**Tạo backup script (`/usr/local/bin/backup-db.sh`):**
```bash
#!/bin/bash

BACKUP_DIR="/backups/oms"
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="$BACKUP_DIR/oms_backup_$DATE.sql"

mkdir -p $BACKUP_DIR

docker exec oms-mysql mysqldump -u root -p$MYSQL_ROOT_PASSWORD ordering_system > $BACKUP_FILE

# Keep only last 30 days
find $BACKUP_DIR -name "*.sql" -mtime +30 -delete

echo "Backup completed: $BACKUP_FILE"
```

#### 2. Application Logs Backup
```bash
# Lưu logs vào file
docker logs oms-app > app_logs_$(date +%Y%m%d).log

# Tự động archive logs cũ (logrotate)
sudo nano /etc/logrotate.d/oms
```

### Monitoring & Alerts

#### 1. Systemd Status Check
```bash
# Monitor service
sudo systemctl status oms

# Auto-restart on failure (đã cấu hình trong service file)
Restart=always
RestartSec=10
```

#### 2. Docker Health Checks
```bash
# Cấu hình health check trong docker-compose.yml
healthcheck:
  test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
  interval: 30s
  timeout: 10s
  retries: 3
  start_period: 40s
```

#### 3. Log Aggregation
```bash
# Gửi logs đến file centralized
docker logs oms-app | tee -a /var/log/oms/combined.log
```

### Updates & Patches

#### 1. Update Application
```bash
# Tải code mới
cd /opt/ordering-management-system
git pull origin main

# Rebuild Docker image
docker-compose build

# Restart services
docker-compose down
docker-compose up -d
```

#### 2. Update Dependencies
```bash
# Update Maven dependencies
mvn dependency:update-snapshots
mvn clean install

# Check for security vulnerabilities
mvn org.owasp:dependency-check-maven:check
```

#### 3. Database Migration
```bash
# Backup before migration
docker exec oms-mysql mysqldump -u root -p ordering_system > backup_before_migration.sql

# Run migration scripts nếu có
docker exec oms-mysql mysql -u root -p ordering_system < migration_script.sql

# Verify
docker exec oms-mysql mysql -u root -p ordering_system -e "SHOW TABLES;"
```

### Security Best Practices

1. **Change Default Passwords**
   - MySQL root password
   - Database user password
   - JWT secret key

2. **Use Environment Variables**
   - Không commit `.env` file
   - Sử dụng secrets management (AWS Secrets Manager, HashiCorp Vault, etc.)

3. **Enable HTTPS**
   - Sử dụng SSL/TLS certificates
   - Setup Nginx reverse proxy

4. **Regular Updates**
   - Update Java version
   - Update MySQL version
   - Update Docker images

5. **Access Control**
   - Sử dụng firewall
   - Limit database access
   - Use SSH key authentication

6. **Database Security**
   - Regular backups
   - Disable remote root login
   - Use strong passwords

### Useful Commands Reference

```bash
# Start/Stop/Restart
docker-compose up -d          # Start containers
docker-compose down           # Stop containers
docker-compose restart        # Restart containers
docker-compose logs -f        # View logs

# Database operations
docker exec -it oms-mysql mysql -u root -p
SHOW DATABASES;
USE ordering_system;
SHOW TABLES;
SELECT * FROM users;

# Application operations
docker exec oms-app ps aux
docker exec oms-app kill -9 <PID>

# System monitoring
docker stats
docker ps
docker images
docker network ls

# Cleanup
docker system prune
docker volume prune
docker network prune
```

---

## Checklist Deploy Production

- [ ] Java 21 JDK đã cài đặt
- [ ] MySQL 8.0 đã cài đặt
- [ ] Git repository đã clone
- [ ] `.env` file đã tạo và cấu hình
- [ ] Database đã tạo, user đã tạo
- [ ] Application test thành công locally
- [ ] Docker image đã build
- [ ] Docker containers đã chạy
- [ ] Health check endpoints responsive
- [ ] Database migrations đã chạy
- [ ] Reverse proxy (Nginx) đã cấu hình
- [ ] SSL certificate đã cài đặt
- [ ] Firewall đã cấu hình
- [ ] Backup strategy đã setup
- [ ] Monitoring đã enable
- [ ] Logs đã configured
- [ ] Security configs đã review

---

## Support & Troubleshooting

Nếu gặp vấn đề:

1. Kiểm tra logs: `docker logs -f oms-app`
2. Kiểm tra health: `curl http://localhost:8080/actuator/health`
3. Kiểm tra database: `docker exec oms-mysql mysql -u root -p`
4. Kiểm tra network: `docker network inspect oms-network`
5. Restart services: `docker-compose restart`

---

**Last Updated**: January 2026
**Version**: 1.0.0

