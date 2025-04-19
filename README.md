
# Spring Boot JWT + JSP + Bootstrap + REST API Project

A demo project combining **Spring Boot 3.4.4**, **JWT-based authentication**, **JSP views**, and **REST APIs** with H2 database support. Tokens are stored and managed using secure HttpOnly cookies.

## ✨ Features

- Spring Security with JWT authentication
- Access & refresh tokens stored in cookies
- Stateless REST API
- JSP-based UI support
- Role-based access control
- H2 embedded database
- Token refresh logic integrated in filter
- Secure endpoints for user and admin

---

## 🧰 Tech Stack

- Java 21
- Spring Boot 3.4.4
- Spring Security 6.2.0
- JWT (JJWT) 0.12.5
- JSP + JSTL
- H2 Database
- Lombok
- Maven

---

## ⚙️ Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com.example.demo/
│   │       ├── config/
│   │       ├── controller/
│   │       ├── dto/
│   │       │  └── payload/
│   │       │      ├── request/
│   │       │      └── response/
│   │       ├── security/
│   │       │   ├── filters/
│   │       │   └── entrypoints/
│   │       ├── entity/
│   │       ├── exception/        <-- custom exceptions and handlers
│   │       ├── repository/
│   │       ├── service/
│   │       ├── utils/
│   │       └── ...
│   ├── resources/
│   │   ├── db/                 <-- H2 database
│   │   ├── static/
│   │   └── application.properties
│   ├── webapp/
│   │   └── views/             <-- JSP files
```

---

## 🔐 Authentication & Security

- Stateless authentication using JWT
- Tokens are stored in **HttpOnly cookies**
- Access token lifespan: in seconds in `application.properties`
- Refresh token lifespan: in seconds in `application.properties`
- Auto-refresh handled inside `JwtAuthenticationFilter.java`
- Role-based access restrictions via `SecurityFilterChain` and `SecurityConfig.java`
- Custom `AuthenticationEntryPoint` and `AccessDeniedHandler` for better error handling in `security/entrypoints`
---

## 🧪 How to Run

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/spring-jwt-jsp-bootstrap.git
   cd spring-jwt-jsp-bootstrap
   ```

2. **Build and run**
   ```bash
   mvn clean spring-boot:run
   ```

3. **Access endpoints**
    - JSP Views: `http://localhost:8080/home`
    - H2 Console: `http://localhost:8080/h2-console`
    - REST API: `http://localhost:8080/api/...`

---

## ⚙️ Configuration

`application.properties`:

```properties
server.port=8080
spring.datasource.url=jdbc:h2:file:./src/main/resources/db/testdb
spring.datasource.username=sa
spring.datasource.password=1234
spring.h2.console.enabled=true

# JWT Config
app.jwt.secret=YourVerySecretKeyHereMakeItLongForSecurityReasons123!@#
app.auth.token.expiration.access=300 # 5 minutes
app.auth.token.expiration.refresh=604800 # 1 week

# JSP
spring.mvc.view.prefix=/views/
spring.mvc.view.suffix=.jsp
```

---
---

## 📦 Dependencies Highlights

```xml
<dependencies>
  <!-- Spring Boot Starters -->
  spring-boot-starter-web
  spring-boot-starter-security
  spring-boot-starter-data-jpa 
  spring-security-taglibs
   

  <!-- JSP / JSTL -->
  tomcat-embed-jasper
  jakarta.servlet.jsp.jstl

  <!-- JWT -->
  jjwt-api, jjwt-impl, jjwt-jackson

  <!-- Lombok -->
  lombok

  <!-- In-Memory DB -->
  h2
</dependencies>
```

---

## 🛡️ Security Filter Overview

JWT access token is extracted from:
- `Authorization` header (for APIs)
- `access_token` cookie (for JSP/browser clients/APIs)

If expired, the refresh token (from `refresh_token` cookie) is used to auto-generate a new one.

---

## 📄 License

MIT (or update this accordingly)

---

## 📬 Contact

For questions or contributions, feel free to open an issue or PR.
