# Docker Deployment Guide

## 📦 Overview

Hướng dẫn chi tiết về cách deploy Ordering Management System sử dụng Docker.

---

## Yêu cầu

- Docker 20.10+
- Docker Compose 2.0+
- Git
- Ít nhất 2GB RAM
- Ít nhất 1GB disk space

### Kiểm tra cài đặt
```bash
docker --version
docker-compose --version
```

---

## Cấu trúc Docker

### Dockerfile
- **Base Image**: `maven:3.9-eclipse-temurin-21` (build stage)
- **Runtime Image**: `eclipse-temurin:21-jre` (production stage)
- **Multi-stage build**: Giảm kích thước image cuối cùng
- **Non-root user**: Security best practice

### docker-compose.yml
- **MySQL Service**: Database container
- **Backend Service**: Spring Boot application container
- **Network**: oms-network (inter-container communication)
- **Volumes**: Data persistence

---

## Quick Start with Docker Compose

### Step 1: Prepare
```bash
# Clone repository
git clone <repository-url>
cd ordering-management-system

# Create .env from template
cp .env-example .env

# Edit .env with your values
nano .env  # or use your editor
```

### Step 2: Configuration
```env
MYSQL_PORT=3306
MYSQL_ROOT_PASSWORD=root_password_123
MYSQL_DATABASE=ordering_system
MYSQL_USER=oms_user
MYSQL_PASSWORD=oms_password_123
JWT_SECRET=generate_random_32_chars_key
JWT_ISSUER=http://localhost:8080
BACKEND_IMAGE=oms-backend:latest
APP_PORT=8080
```

### Step 3: Build
```bash
# Build Docker images
docker-compose build

# (Optional) View image
docker images | grep oms
```

### Step 4: Deploy
```bash
# Start services in background
docker-compose up -d

# Or start in foreground to see logs
docker-compose up
```

### Step 5: Verify
```bash
# Check containers
docker-compose ps

# Check logs
docker-compose logs -f oms-app

# Test API
curl http://localhost:8080/actuator/health
```

---

## Common Docker Compose Commands

### Viewing Logs
```bash
# View all logs
docker-compose logs

# View specific service logs
docker-compose logs oms-app
docker-compose logs mysql

# Follow logs in real-time
docker-compose logs -f
docker-compose logs -f oms-app

# View last 100 lines
docker-compose logs --tail=100

# View logs with timestamps
docker-compose logs -f --timestamps
```

### Managing Containers
```bash
# Start services
docker-compose up
docker-compose up -d  # background

# Stop services
docker-compose stop

# Start services (if stopped)
docker-compose start

# Restart services
docker-compose restart
docker-compose restart oms-app  # specific service

# Remove containers
docker-compose down

# Remove containers and volumes (WARNING: deletes data)
docker-compose down -v

# Remove containers, volumes, and images
docker-compose down -v --rmi all
```

### Inspecting Services
```bash
# View running containers
docker-compose ps

# View container stats
docker stats

# View container details
docker-compose config

# View service logs with exit code
docker-compose up --abort-on-container-exit
```

### Database Operations
```bash
# Access MySQL shell
docker-compose exec mysql mysql -u root -p

# Or without password in compose (if configured)
docker-compose exec mysql mysql -u oms_user -p -D ordering_system

# Execute SQL command
docker-compose exec mysql mysql -u root -p -e "SHOW DATABASES;"

# Backup database
docker-compose exec mysql mysqldump -u root -p ordering_system > backup.sql

# Restore database
docker-compose exec -T mysql mysql -u root -p ordering_system < backup.sql
```

### Container Access
```bash
# Enter container shell
docker-compose exec oms-app /bin/bash
docker-compose exec mysql /bin/bash

# Execute command in container
docker-compose exec oms-app ls -la
docker-compose exec oms-app cat /app/app.jar

# View environment variables
docker-compose exec oms-app env | grep SPRING
docker-compose exec oms-app env | grep JWT
```

---

## Advanced Docker Compose Usage

### Custom Configuration Override

Create `docker-compose.override.yml` untuk development:

```yaml
version: "3.8"

services:
  oms-app:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    environment:
      SPRING_JPA_HIBERNATE_DDL_AUTO: create-drop  # Reset DB setiap startup
      SPRING_JPA_SHOW_SQL: "true"
      SPRING_JPA_PROPERTIES_HIBERNATE_FORMAT_SQL: "true"
    volumes:
      - .:/workspace  # Code mount untuk development
      - /workspace/target  # Exclude target folder
```

### Health Checks

Cấu hình health checks dalam docker-compose.yml:

```yaml
services:
  mysql:
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      interval: 10s
      timeout: 5s
      retries: 5
      start_period: 40s

  oms-app:
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 40s
```

### Environment-Specific Compose Files

```bash
# Development
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up

# Staging
docker-compose -f docker-compose.yml -f docker-compose.staging.yml up

# Production
docker-compose -f docker-compose.yml -f docker-compose.prod.yml up
```

---

## Building Custom Docker Image

### Build locally
```bash
docker build -t oms-backend:1.0.0 .
docker tag oms-backend:1.0.0 oms-backend:latest
```

### View image info
```bash
docker inspect oms-backend:latest
docker history oms-backend:latest  # View layers
```

### Push to Registry
```bash
# Tag untuk registry
docker tag oms-backend:latest myregistry.azurecr.io/oms-backend:latest

# Login ke registry
docker login myregistry.azurecr.io

# Push
docker push myregistry.azurecr.io/oms-backend:latest
```

### Optimize Image Size
Current image size: ~400-500MB (Java runtime + dependencies)

Untuk mengurangi:
```dockerfile
# Use alpine-jre (smaller but less tested)
FROM eclipse-temurin:21-jre-alpine

# Remove unnecessary files
RUN apt-get autoremove && apt-get clean
```

---

## Manual Docker Container Management

Jika tidak sử dụng docker-compose:

### Create Network
```bash
docker network create oms-network
```

### Run MySQL
```bash
docker run -d \
  --name oms-mysql \
  --network oms-network \
  -e MYSQL_ROOT_PASSWORD=root_password \
  -e MYSQL_DATABASE=ordering_system \
  -e MYSQL_USER=oms_user \
  -e MYSQL_PASSWORD=oms_password \
  -v mysql_data:/var/lib/mysql \
  -p 3306:3306 \
  mysql:8.0
```

### Run Application
```bash
docker run -d \
  --name oms-backend \
  --network oms-network \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://oms-mysql:3306/ordering_system?useSSL=false \
  -e SPRING_DATASOURCE_USERNAME=oms_user \
  -e SPRING_DATASOURCE_PASSWORD=oms_password \
  -e JWT_SECRET=your_secret_key \
  -e JWT_ISSUER=http://localhost:8080 \
  -p 8080:8080 \
  oms-backend:latest
```

### View Container Logs
```bash
docker logs oms-backend
docker logs -f oms-backend  # Follow
```

### Access Container
```bash
docker exec -it oms-backend /bin/bash
docker exec oms-backend ls -la /app
```

### Stop & Remove
```bash
docker stop oms-backend
docker rm oms-backend
docker volume rm mysql_data
```

---

## Troubleshooting Docker Issues

### "Bind for 0.0.0.0:8080 failed: port is already allocated"
```bash
# Find process using port
lsof -i :8080  # Linux/Mac
netstat -ano | findstr :8080  # Windows

# Kill process or use different port
docker-compose down
# Or change port in .env
```

### "Cannot connect to MySQL"
```bash
# Check MySQL container
docker-compose ps mysql

# Check logs
docker-compose logs mysql

# Check health
docker-compose exec mysql mysql -u root -p -e "SELECT 1;"

# Restart
docker-compose restart mysql
```

### "Application starts but can't connect to DB"
```bash
# Verify network
docker network inspect oms-network

# Check service names resolve
docker-compose exec oms-app ping mysql

# Check connection string
docker-compose exec oms-app env | grep DATASOURCE
```

### "Out of memory"
```bash
# Check Docker stats
docker stats

# Increase memory limit in docker-compose.yml
services:
  oms-app:
    environment:
      JAVA_OPTS: "-Xms512m -Xmx2g"
```

### "Cannot execute shell scripts"
```bash
# Check permissions
docker-compose exec oms-app ls -la /app/wait-and-run.sh

# Make executable
chmod +x wait-and-run.sh  # Before building

# Or in Dockerfile
RUN chmod +x /app/wait-and-run.sh
```

### "Disk space issues"
```bash
# Clean up Docker resources
docker system prune -a

# Remove unused volumes
docker volume prune

# Check disk usage
du -sh /var/lib/docker  # Linux
```

### "Container exits immediately"
```bash
# Check logs
docker-compose logs oms-app

# Start in foreground
docker-compose up oms-app  # Not -d

# Check for errors in output
```

---

## Production Considerations

### Security

1. **Don't expose database port**
   ```yaml
   mysql:
     ports: []  # Don't expose, only internal network
     expose:
       - "3306"
   ```

2. **Use strong passwords**
   - Store in secure location
   - Use secrets management

3. **Run as non-root**
   - Already configured in Dockerfile
   - Verify: `docker exec oms-app whoami`

4. **Set resource limits**
   ```yaml
   oms-app:
     deploy:
       resources:
         limits:
           cpus: '1'
           memory: 2G
         reservations:
           cpus: '0.5'
           memory: 1G
   ```

### Performance

1. **Use external volumes for MySQL**
   ```yaml
   volumes:
     mysql_data:
       driver: local
       driver_opts:
         type: nfs
         o: addr=nfs-server,vers=4,soft,timeo=180,bg,tcp,rw
         device: ":/export/mysql"
   ```

2. **Enable logging driver**
   ```yaml
   logging:
     driver: "json-file"
     options:
       max-size: "10m"
       max-file: "3"
   ```

3. **Use restart policies**
   ```yaml
   restart_policy:
     condition: on-failure
     delay: 5s
     max_attempts: 3
     window: 120s
   ```

### Monitoring

```yaml
services:
  oms-app:
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 40s
```

---

## Docker Compose Best Practices

### File Organization
```
project/
├── docker-compose.yml          # Main config
├── docker-compose.dev.yml      # Development overrides
├── docker-compose.staging.yml  # Staging overrides
├── docker-compose.prod.yml     # Production overrides
├── Dockerfile
├── .env.example
├── .env                        # Not committed
└── .dockerignore
```

### .dockerignore
```
.git
.gitignore
.idea
target
*.log
node_modules
.env
.DS_Store
```

### Version Pinning
Always use specific versions, not `latest`:
```yaml
image: mysql:8.0.35     # ✅ Specific version
image: mysql:8.0        # ⚠️ Minor version
image: mysql:latest     # ❌ Avoid in production
```

---

## Quick Reference

```bash
# Start
docker-compose up -d

# Stop
docker-compose down

# Logs
docker-compose logs -f oms-app

# MySQL access
docker-compose exec mysql mysql -u root -p

# Bash in container
docker-compose exec oms-app /bin/bash

# Restart
docker-compose restart

# Full cleanup
docker-compose down -v --rmi all
```

---

**Last Updated**: January 2026
**Version**: 1.0.0

