# Server Deployment Guide (Linux/VPS)

## 📡 Deploy lên VPS/Server Production

Hướng dẫn chi tiết deploy Ordering Management System lên Linux server (Ubuntu/Debian).

---

## Prerequisites

### Server Requirements
- Ubuntu 20.04 LTS hoặc Debian 11+
- 2GB RAM minimum (4GB recommended)
- 20GB disk space
- Public IP address
- Domain name (optional nhưng recommended)

### Tools Required
- SSH access with sudo privileges
- Git
- Docker & Docker Compose (hoặc Java + Maven)

---

## Step 1: Initial Server Setup

### 1.1 Update System
```bash
sudo apt-get update
sudo apt-get upgrade -y
sudo apt-get install -y curl wget git vim htop
```

### 1.2 Create Application User
```bash
# Create non-root user
sudo useradd -m -s /bin/bash appuser

# Add to sudo group
sudo usermod -aG sudo appuser

# Add to docker group (if using Docker)
sudo usermod -aG docker appuser

# Login as appuser
su - appuser
```

### 1.3 Set Timezone
```bash
sudo timedatectl set-timezone Asia/Ho_Chi_Minh

# Or your timezone
sudo timedatectl set-timezone UTC
```

### 1.4 Setup Firewall
```bash
# Enable UFW
sudo ufw enable

# Allow SSH
sudo ufw allow 22/tcp

# Allow HTTP
sudo ufw allow 80/tcp

# Allow HTTPS
sudo ufw443/tcp

# Check status
sudo ufw status
```

---

## Step 2: Install Dependencies

### Option A: Using Docker (Recommended)

#### 2A.1 Install Docker
```bash
# Install Docker
curl -fsSL https://get.docker.com -o get-docker.sh
sudo sh get-docker.sh

# Install Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Verify
docker --version
docker-compose --version

# Add user to docker group (log out and back in after)
sudo usermod -aG docker appuser
```

#### 2A.2 Configure Docker (Optional but recommended)
```bash
# Create daemon.json for logging and limits
sudo mkdir -p /etc/docker
sudo tee /etc/docker/daemon.json > /dev/null <<EOF
{
  "log-driver": "json-file",
  "log-opts": {
    "max-size": "10m",
    "max-file": "3"
  },
  "storage-driver": "overlay2"
}
EOF

# Restart Docker
sudo systemctl restart docker
```

### Option B: Using Java + Maven

#### 2B.1 Install Java 21
```bash
sudo apt-get install -y openjdk-21-jdk

# Verify
java -version
```

#### 2B.2 Install Maven
```bash
sudo apt-get install -y maven

# Verify
mvn --version
```

#### 2B.3 Install MySQL (if not using Docker)
```bash
sudo apt-get install -y mysql-server

# Start service
sudo systemctl start mysql
sudo systemctl enable mysql

# Secure MySQL
sudo mysql_secure_installation

# Verify
mysql --version
```

---

## Step 3: Clone & Setup Repository

### 3.1 Clone Repository
```bash
cd /opt
sudo git clone <repository-url> ordering-management-system
sudo chown -R appuser:appuser ordering-management-system
cd ordering-management-system
```

### 3.2 Create Environment File
```bash
cp .env-example .env
nano .env  # Edit dengan credentials production
```

### 3.3 Set File Permissions
```bash
chmod 600 .env
ls -la .env  # Should show: -rw-------
```

---

## Step 4: Deploy with Docker Compose

### 4.1 Create Directory Structure
```bash
mkdir -p /opt/ordering-management-system/data
mkdir -p /opt/ordering-management-system/logs
mkdir -p /opt/ordering-management-system/backups

sudo chown -R appuser:appuser /opt/ordering-management-system
```

### 4.2 Build and Start Services
```bash
cd /opt/ordering-management-system

# Build images
docker-compose build

# Start services
docker-compose up -d

# Check status
docker-compose ps

# View logs
docker-compose logs -f
```

### 4.3 Verify Deployment
```bash
# Wait a few seconds for app to start
sleep 10

# Test health endpoint
curl http://localhost:8080/actuator/health

# Expected response: {"status":"UP"}
```

---

## Step 5: Setup Reverse Proxy (Nginx)

### 5.1 Install Nginx
```bash
sudo apt-get install -y nginx

# Start service
sudo systemctl start nginx
sudo systemctl enable nginx
```

### 5.2 Create Nginx Configuration
```bash
sudo tee /etc/nginx/sites-available/ordering-app > /dev/null <<'EOF'
upstream backend {
    server localhost:8080;
}

server {
    listen 80;
    server_name yourdomain.com www.yourdomain.com;

    client_max_body_size 100M;

    # Redirect to HTTPS
    location / {
        return 301 https://$server_name$request_uri;
    }

    # Let's Encrypt validation
    location /.well-known/acme-challenge/ {
        root /var/www/certbot;
    }
}

server {
    listen 443 ssl http2;
    server_name yourdomain.com www.yourdomain.com;

    client_max_body_size 100M;

    # SSL certificates (after installing)
    ssl_certificate /etc/letsencrypt/live/yourdomain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/yourdomain.com/privkey.pem;

    # SSL configuration
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;
    ssl_prefer_server_ciphers on;
    ssl_session_cache shared:SSL:10m;
    ssl_session_timeout 10m;

    # Security headers
    add_header Strict-Transport-Security "max-age=31536000; includeSubDomains" always;
    add_header X-Content-Type-Options "nosniff" always;
    add_header X-Frame-Options "SAMEORIGIN" always;
    add_header X-XSS-Protection "1; mode=block" always;

    # Proxy to backend
    location / {
        proxy_pass http://backend;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_read_timeout 90;
        proxy_connect_timeout 90;

        # WebSocket support (if needed)
        proxy_http_version 1.1;
        proxy_set_header Upgrade $http_upgrade;
        proxy_set_header Connection "upgrade";
    }

    # Static files caching
    location ~* \.(jpg|jpeg|png|gif|ico|css|js)$ {
        expires 1y;
        add_header Cache-Control "public, immutable";
    }

    # Deny access to .env and other sensitive files
    location ~ /\.env {
        deny all;
    }
}
EOF
```

### 5.3 Enable Nginx Site
```bash
sudo ln -s /etc/nginx/sites-available/ordering-app /etc/nginx/sites-enabled/

# Remove default site
sudo rm -f /etc/nginx/sites-enabled/default

# Test Nginx configuration
sudo nginx -t

# Reload Nginx
sudo systemctl reload nginx
```

---

## Step 6: Setup SSL Certificate (Let's Encrypt)

### 6.1 Install Certbot
```bash
sudo apt-get install -y certbot python3-certbot-nginx

# Create directory for validation
sudo mkdir -p /var/www/certbot
```

### 6.2 Generate Certificate
```bash
sudo certbot certonly --nginx -d yourdomain.com -d www.yourdomain.com

# Or standalone (if Nginx not ready)
sudo certbot certonly --standalone -d yourdomain.com
```

### 6.3 Setup Auto-Renewal
```bash
# Test renewal
sudo certbot renew --dry-run

# Enable auto-renewal (cron job)
sudo systemctl enable certbot.timer
sudo systemctl start certbot.timer

# Check status
sudo systemctl status certbot.timer
```

---

## Step 7: Database Backup Strategy

### 7.1 Create Backup Script
```bash
sudo tee /usr/local/bin/backup-oms-db.sh > /dev/null <<'EOF'
#!/bin/bash

BACKUP_DIR="/opt/ordering-management-system/backups"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="$BACKUP_DIR/oms_backup_$TIMESTAMP.sql"

# Create backup
docker-compose -f /opt/ordering-management-system/docker-compose.yml \
  exec -T mysql mysqldump -u root -p${MYSQL_ROOT_PASSWORD} \
  ordering_system > "$BACKUP_FILE"

# Compress
gzip "$BACKUP_FILE"

# Keep only last 30 days
find "$BACKUP_DIR" -name "*.sql.gz" -mtime +30 -delete

echo "Backup completed: $BACKUP_FILE.gz"
EOF

sudo chmod +x /usr/local/bin/backup-oms-db.sh
```

### 7.2 Setup Cron Job
```bash
# Edit crontab
sudo crontab -e

# Add this line (2 AM daily)
0 2 * * * /usr/local/bin/backup-oms-db.sh >> /var/log/oms-backup.log 2>&1
```

### 7.3 Test Backup
```bash
sudo /usr/local/bin/backup-oms-db.sh
ls -lh /opt/ordering-management-system/backups/
```

---

## Step 8: Monitoring & Logging

### 8.1 Setup Log Rotation
```bash
sudo tee /etc/logrotate.d/ordering-app > /dev/null <<'EOF'
/opt/ordering-management-system/logs/*.log {
    daily
    rotate 14
    compress
    delaycompress
    notifempty
    create 0640 appuser appuser
    sharedscripts
}
EOF
```

### 8.2 Monitor Service Health
```bash
# View Docker stats
docker stats

# View container logs
docker logs -f oms-app --tail 100

# Monitor disk space
df -h
du -sh /opt/ordering-management-system

# Monitor memory
free -h
```

### 8.3 Setup Health Check Cron
```bash
# Create monitoring script
sudo tee /usr/local/bin/check-oms-health.sh > /dev/null <<'EOF'
#!/bin/bash

HEALTH_ENDPOINT="https://yourdomain.com/actuator/health"
RESPONSE=$(curl -s "$HEALTH_ENDPOINT")

if echo "$RESPONSE" | grep -q "UP"; then
    echo "Status: HEALTHY"
else
    echo "Status: UNHEALTHY"
    # Send alert (email, slack, etc.)
    # mail -s "OMS Health Check Failed" admin@example.com
fi
EOF

sudo chmod +x /usr/local/bin/check-oms-health.sh

# Add to crontab (every 5 minutes)
# */5 * * * * /usr/local/bin/check-oms-health.sh >> /var/log/oms-health.log 2>&1
```

---

## Step 9: Update & Maintenance

### 9.1 Update Application
```bash
cd /opt/ordering-management-system

# Pull latest code
git pull origin main

# Rebuild images
docker-compose build

# Stop old containers
docker-compose down

# Start new containers
docker-compose up -d

# Verify
docker-compose logs -f
```

### 9.2 Database Migration
```bash
# Backup before migration
sudo /usr/local/bin/backup-oms-db.sh

# Run migration
docker-compose exec mysql mysql -u root -p ordering_system < migration.sql

# Verify
docker-compose logs -f oms-app | grep -i "migration\|started"
```

### 9.3 System Updates
```bash
# Update system packages
sudo apt-get update
sudo apt-get upgrade -y

# Update Docker images
docker pull mysql:8.0
docker pull eclipse-temurin:21-jre

# Restart services
docker-compose restart
```

---

## Step 10: Troubleshooting

### Check Service Status
```bash
# Docker containers
docker ps
docker-compose ps

# Nginx
sudo systemctl status nginx

# Firewall
sudo ufw status

# Port usage
sudo ss -tlnp | grep 8080
sudo ss -tlnp | grep 443
```

### View Logs
```bash
# Application logs
docker-compose logs -f oms-app --tail 100

# Database logs
docker-compose logs mysql

# Nginx logs
sudo tail -f /var/log/nginx/error.log
sudo tail -f /var/log/nginx/access.log

# System logs
sudo journalctl -u docker -f
```

### Common Issues

#### 1. Application won't start
```bash
# Check logs
docker-compose logs oms-app

# Check database connection
docker-compose exec oms-mysql mysql -u root -p -e "SELECT 1;"

# Restart all services
docker-compose restart
```

#### 2. Database connection refused
```bash
# Check MySQL is running
docker-compose ps mysql

# Check credentials in .env
grep MYSQL .env

# Test connection
docker-compose exec mysql mysql -u root -p -e "SHOW DATABASES;"
```

#### 3. SSL certificate issues
```bash
# Check certificate validity
sudo certbot certificates

# Renew certificate manually
sudo certbot renew --force-renewal

# Check Nginx SSL config
sudo nginx -t
sudo openssl s_client -connect yourdomain.com:443
```

#### 4. Port already in use
```bash
# Find process using port
sudo lsof -i :8080
sudo lsof -i :443

# Stop process or change port in .env
```

### Performance Optimization

```bash
# Increase Java heap memory
# In docker-compose.yml
environment:
  JAVA_OPTS: "-Xms1g -Xmx3g"

# Increase Nginx worker connections
# In /etc/nginx/nginx.conf
events {
    worker_connections 4096;
}

# Enable Nginx gzip compression
# In /etc/nginx/nginx.conf
gzip on;
gzip_types text/plain text/css text/xml application/json application/javascript;
```

---

## Useful Commands Reference

```bash
# View all OMS processes
ps aux | grep oms

# Restart entire application
docker-compose restart

# View container resource usage
docker stats

# SSH into container
docker-compose exec oms-app /bin/bash

# Check open ports
sudo ss -tlnp

# Monitor system resources
htop

# Disk usage
du -sh /opt/ordering-management-system/*

# Test API endpoint
curl -v https://yourdomain.com/actuator/health

# View real-time logs
docker-compose logs -f --tail 50
```

---

## Security Checklist

- [ ] Changed all default passwords
- [ ] Firewall configured (SSH, HTTP, HTTPS only)
- [ ] SSH key-based authentication enabled
- [ ] SSL certificate installed
- [ ] .env file permissions set to 600
- [ ] Non-root user for application
- [ ] Regular backups scheduled
- [ ] Monitoring enabled
- [ ] Logs rotated
- [ ] fail2ban installed (optional)
- [ ] Server automatic updates enabled
- [ ] Nginx security headers configured

---

## Post-Deployment Verification

```bash
# 1. Test API endpoints
curl -X GET https://yourdomain.com/actuator/health
curl -X GET https://yourdomain.com/actuator/metrics

# 2. Check logs for errors
docker-compose logs --tail 50

# 3. Verify database
docker-compose exec mysql mysql -u root -p -e "SELECT COUNT(*) FROM users;"

# 4. Check disk space
df -h

# 5. Monitor resources
docker stats

# 6. Test SSL
echo | openssl s_client -servername yourdomain.com -connect yourdomain.com:443 2>/dev/null | grep -A2 "subject="
```

---

**Last Updated**: January 2026
**Version**: 1.0.0

