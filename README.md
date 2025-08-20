# Employee Management System

A comprehensive web application built with Spring Boot and Thymeleaf for managing employee information, tracking performance, and generating insightful reports.

## 🚀 Features

### Core Functionality
- **Employee Management**: Add, edit, view, and delete employee records
- **Advanced Search**: Search employees by name, email, position, or department
- **Filtering & Sorting**: Filter by department, status, and sort by various criteria
- **Pagination**: Efficient data display with configurable page sizes
- **Data Validation**: Comprehensive client-side and server-side validation
- **Statistics Dashboard**: Real-time employee statistics and department analytics

### Technical Features
- **RESTful API**: Complete REST API for all employee operations
- **Responsive Design**: Mobile-first design using Bootstrap 5
- **Form Validation**: Custom validation annotations and error handling
- **Exception Handling**: Global exception handling with proper HTTP status codes
- **Database Integration**: H2 database with JPA/Hibernate
- **Audit Trail**: Automatic tracking of creation and modification timestamps

## 🛠️ Technology Stack

- **Backend**: Spring Boot 3.2.0, Spring Data JPA, Spring Validation
- **Database**: H2 Database (in-memory for development)
- **Frontend**: Thymeleaf templates, Bootstrap 5, JavaScript (ES6+)
- **Build Tool**: Maven
- **Java Version**: 17+

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/employeems/
│   │   ├── controller/          # REST and Web controllers
│   │   ├── entity/             # JPA entities
│   │   ├── enums/              # Enums for departments and status
│   │   ├── exception/          # Custom exceptions
│   │   ├── repository/         # Data access layer
│   │   ├── service/            # Business logic layer
│   │   ├── validation/         # Custom validation annotations
│   │   ├── dto/                # Data Transfer Objects
│   │   └── config/             # Configuration classes
│   └── resources/
│       ├── static/             # CSS, JavaScript, images
│       ├── templates/          # Thymeleaf templates
│       └── application.properties
└── test/                       # Test classes
```

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher
- IDE (IntelliJ IDEA, Eclipse, or VS Code)

### Installation & Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd employee-management-system
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - Main Application: http://localhost:8080
   - H2 Database Console: http://localhost:8080/h2-console
   - API Base URL: http://localhost:8080/api

### Database Configuration

The application uses H2 in-memory database by default. Database credentials:
- **URL**: `jdbc:h2:mem:employeedb`
- **Username**: `sa`
- **Password**: `password`

## 📖 API Documentation

### Employee Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/employees` | Get all employees with pagination |
| GET | `/api/employees/{id}` | Get employee by ID |
| POST | `/api/employees` | Create new employee |
| PUT | `/api/employees/{id}` | Update employee |
| DELETE | `/api/employees/{id}` | Delete employee |
| GET | `/api/employees/search` | Search employees by keyword |
| GET | `/api/employees/department/{dept}` | Get employees by department |
| GET | `/api/employees/statistics` | Get employee statistics |

### Query Parameters

- **Pagination**: `page`, `size`
- **Sorting**: `sortBy`, `sortDir`
- **Filtering**: `department`, `status`, `keyword`

### Example API Calls

```bash
# Get all employees with pagination
GET /api/employees?page=0&size=10&sortBy=firstName&sortDir=asc

# Search employees
GET /api/employees/search?keyword=john

# Get employees by department
GET /api/employees/department/IT

# Get employee statistics
GET /api/employees/statistics
```

## 🎨 User Interface

### Web Pages
- **Home Page** (`/`): Welcome page with system overview
- **Employee List** (`/employees`): View all employees with search and filters
- **Add Employee** (`/employees/new`): Form to add new employees
- **Edit Employee** (`/employees/{id}/edit`): Form to edit existing employees
- **Employee Details** (`/employees/{id}`): Detailed view of employee information

### Features
- **Responsive Design**: Works on desktop, tablet, and mobile devices
- **Modern UI**: Clean, professional interface using Bootstrap 5
- **Interactive Elements**: Hover effects, animations, and smooth transitions
- **Form Validation**: Real-time validation with helpful error messages
- **Search & Filter**: Advanced search with multiple filter options

## 🔧 Configuration

### Application Properties

#### Development (`application.properties`)
```properties
# Server Configuration
server.port=8080

# H2 Database
spring.datasource.url=jdbc:h2:mem:employeedb
spring.h2.console.enabled=true

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Thymeleaf
spring.thymeleaf.cache=false
```

#### Production (`application-prod.properties`)
```properties
# Production database configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/employeedb
spring.jpa.hibernate.ddl-auto=validate
spring.thymeleaf.cache=true
```

## 🧪 Testing

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=EmployeeServiceTest

# Run with coverage
mvn test jacoco:report
```

### Test Structure
- **Unit Tests**: Service layer testing with Mockito
- **Integration Tests**: Controller and repository testing
- **Test Data**: Comprehensive sample data for testing

## 📊 Sample Data

The application comes with pre-loaded sample data including:
- **50+ Employee Records**: Various departments and positions
- **Multiple Departments**: IT, HR, Finance, Marketing, Operations, Sales
- **Different Statuses**: Active, Inactive, On Leave, Terminated
- **Realistic Data**: Names, emails, phone numbers, and salary ranges

## 🚀 Deployment

### Development
```bash
mvn spring-boot:run
```

### Production
```bash
# Build JAR file
mvn clean package

# Run JAR file
java -jar target/employee-management-system-1.0.0.jar
```

### Docker (Optional)
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/employee-management-system-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

## 🔒 Security Considerations

- **Input Validation**: Comprehensive validation on all inputs
- **SQL Injection Protection**: JPA/Hibernate parameterized queries
- **XSS Protection**: Thymeleaf automatic HTML escaping
- **CSRF Protection**: Form-based CSRF protection
- **Error Handling**: Secure error messages without information leakage

## 📈 Performance Features

- **Database Indexing**: Optimized database queries with proper indexes
- **Pagination**: Efficient data loading with configurable page sizes
- **Caching**: Thymeleaf template caching in production
- **Lazy Loading**: JPA lazy loading for related entities
- **Query Optimization**: Custom queries for complex operations

## 🐛 Troubleshooting

### Common Issues

1. **Port Already in Use**
   ```bash
   # Change port in application.properties
   server.port=8081
   ```

2. **Database Connection Issues**
   - Verify H2 console is accessible
   - Check database credentials
   - Ensure no other application is using the same port

3. **Build Errors**
   ```bash
   # Clean and rebuild
   mvn clean install
   ```

### Logs
- Application logs are available in the console
- Hibernate SQL logs are enabled in development mode
- Log level can be configured in `application.properties`

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Submit a pull request

## 📝 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Bootstrap team for the responsive CSS framework
- Thymeleaf team for the template engine
- All contributors and users of this system

## 📞 Support

For support and questions:
- Create an issue in the repository
- Check the documentation
- Review the sample code and tests

---

**Employee Management System** - Built with ❤️ using Spring Boot and Thymeleaf


# idk
