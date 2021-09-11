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
