# Database configuration

### Setup with application-profile.yml
spring:
    datasource:
        driver-class-name: [driveName]
        url: [jdbcUrl]
        username: [dbUsername]
        password: [dbPassword]

* dev/locally, H2 is used for now
* prod, AWS RDS-MySQL is used

### Create database command
```sql
CREATE DATABASE db_blog_prod CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;
```
