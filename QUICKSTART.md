# Quick Start Guide - Ordering Management System

## ⚡ Chạy nhanh nhất (5 phút)

### Yêu cầu tối thiểu
- Java 21+ đã cài
- MySQL đã chạy
- Git đã cài

### Bước 1: Clone & Setup
```bash
git clone <repository-url>
cd ordering-management-system
cp .env-example .env
```

### Bước 2: Cấu hình .env
```env
MYSQL_PORT=3306
MYSQL_ROOT_PASSWORD=root123
MYSQL_DATABASE=ordering_system
MYSQL_USER=oms_user
MYSQL_PASSWORD=oms123
JWT_SECRET=my_super_secret_key_at_least_32_characters_long_xxx
JWT_ISSUER=http://localhost:8080
BACKEND_IMAGE=oms-backend:latest
APP_PORT=8080
```

### Bước 3: Chạy với Docker Compose
```bash
docker-compose up -d
```

### Bước 4: Kiểm tra
```bash
curl http://localhost:8080/actuator/health
```

✅ Done! API ready at `http://localhost:8080`

---

## Nếu không có Docker

### Cách 1: Maven
```bash
mvn clean package -DskipTests
mvn spring-boot:run
```

### Cách 2: Run JAR
```bash
mvn clean package -DskipTests
java -jar target/ordering-management-system-0.0.1-SNAPSHOT.jar
```

---

## Các lệnh hữu ích

```bash
# Xem logs
docker-compose logs -f oms-app

# Vào MySQL
docker-compose exec mysql mysql -u root -p

# Stop
docker-compose down

# Restart
docker-compose restart

# Reset database
docker-compose down -v
docker-compose up -d
```

---

## Troubleshoot nhanh

| Problem | Solution |
|---------|----------|
| Port 8080 in use | `docker-compose restart` hoặc `kill process on port 8080` |
| MySQL won't start | `docker-compose down -v && docker-compose up -d` |
| Can't connect to DB | Check `.env`, kiểm tra MySQL credentials |
| Logs không hiển thị | Xem: `docker logs oms-app` |

---

## Default Endpoints

- Health: `GET http://localhost:8080/actuator/health`
- Metrics: `GET http://localhost:8080/actuator/metrics`

---

For detailed guide, see: [DEPLOYMENT.md](README.md)

