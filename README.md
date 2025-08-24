# Expenses API

A Spring Boot REST API for personal expense and income tracking with user authentication, transaction management, and built-in categories.

## Features

- **User Authentication**: Registration, login, logout with session-based security
- **Transaction Management**: CRUD operations for income/expense transactions
- **Category System**: Predefined categories (managed by developers)
- **User Profile Management**: Update profile information and account deletion
- **Multi-user Support**: Each user can only access their own transactions
- **Database Migration**: Automated schema management with Flyway

## Tech Stack

- **Backend**: Spring Boot 3.x, Spring Security, Spring Data JPA
- **Database**: PostgreSQL with Flyway migrations
- **Build Tool**: Maven (built-in in IntelliJ IDEA)
- **Java Version**: 21 (Temurin recommended)

## Requirements

- Java 21+
- PostgreSQL 12+
- IDE (IntelliJ IDEA recommended)

## Setup

### 1. Clone the repository
```bash
git clone https://github.com/yihsing01/expenses.git
cd expenses
```

### 2. Configure the database
Create a PostgreSQL database:
```sql
CREATE DATABASE expenses;
```

### 3. Environment configuration
Create a `.env` file in the project root:
```env
DB_URL=jdbc:postgresql://localhost:5432/expenses
DB_USERNAME=postgres
DB_PASSWORD=your_password
```

### 4. Application properties
Ensure your `src/main/resources/application.properties` includes:
```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
```

### 5. Run the application
Use the IDE's built-in Maven support or run via terminal:
```bash
mvn spring-boot:run
```
The API will be available at `http://localhost:8080`

## API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | User login |
| POST | `/api/auth/logout` | User logout |
| GET | `/api/auth/me` | Get current user info |

### Users
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users/me` | Get current user profile |
| PUT | `/api/users/me` | Update current user profile |
| DELETE | `/api/users/me` | Delete own account |

### Transactions
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/transactions` | Get all transactions for current user |
| GET | `/api/transactions/{id}` | Get specific transaction by ID |
| POST | `/api/transactions` | Create new transaction |
| PUT | `/api/transactions/{id}` | Update transaction by ID |
| DELETE | `/api/transactions/{id}` | Delete transaction by ID |

### Categories
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/categories` | Get all categories |
| GET | `/api/categories/{id}` | Get specific category by ID |

## Example Requests

### Register
```json
POST /api/auth/register
{
  "name": "Dan",
  "email": "dan@example.com",
  "password": "password123"
}
```

### Create Transaction
```json
POST /api/transactions
{
  "categoryId": 2,
  "amount": 20.50,
  "description": "Lunch",
  "transactionDate": "2025-08-20"
}
```

### Update Transaction
```json
PUT /api/transactions/{id}
{
  "categoryId": 2,
  "amount": 25.00,
  "description": "Lunch",
  "transactionDate": "2025-08-20T14:30:00"
}
```

## Security

- Session-based authentication with Spring Security
- Users can only access their own transactions
- Passwords hashed with BCrypt
- CSRF protection enabled

## Database

- PostgreSQL with Flyway for versioning and migrations
- Migration scripts located in `src/main/resources/db/migration/`

## Testing

The application includes seeded test users for quick testing:
- **Email:** `john@gmail.com` **Password:** `password`
- **Email:** `jane@gmail.com` **Password:** `password`

Use Postman or similar API testing tools. Login first to establish a session, then test other endpoints.

## License

MIT
