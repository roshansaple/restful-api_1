Here's a complete Spring Boot application with best practices, explanations, comments, and JUnit/Mokito test cases, along with a detailed project structure.

### Project Structure

```
spring-boot-employee-management/
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── example
│   │   │           └── employee
│   │   │               ├── config
│   │   │               │   └── DatabaseConfig.java
│   │   │               ├── controller
│   │   │               │   └── EmployeeController.java
│   │   │               ├── dao
│   │   │               │   ├── EmployeeDao.java
│   │   │               │   └── impl
│   │   │               │       └── EmployeeDaoImpl.java
│   │   │               ├── exception
│   │   │               │   ├── BadRequestException.java
│   │   │               │   ├── EmployeeNotFoundException.java
│   │   │               │   └── GlobalExceptionHandler.java
│   │   │               ├── mapper
│   │   │               │   └── EmployeeRowMapper.java
│   │   │               ├── model
│   │   │               │   └── Employee.java
│   │   │               ├── service
│   │   │               │   ├── EmployeeService.java
│   │   │               │   └── impl
│   │   │               │       └── EmployeeServiceImpl.java
│   │   │               └── EmployeeManagementApplication.java
│   │   └── resources
│   │       ├── application.properties
│   │       └── data.sql
│   └── test
│       └── java
│           └── com
│               └── example
│                   └── employee
│                       ├── controller
│                       │   └── EmployeeControllerTest.java
│                       └── service
│                           └── EmployeeServiceTest.java
└── pom.xml
```

### Explanation of the Project

This Spring Boot application provides a RESTful API for managing employee records. It includes CRUD (Create, Read, Update, Delete) operations and demonstrates best practices in terms of structure, exception handling, and testing.

#### Key Components

1. **Model**: Represents the Employee entity.
2. **DAO**: Data Access Object pattern for database operations.
3. **Service**: Contains business logic and interacts with the DAO.
4. **Controller**: Handles HTTP requests and responses.
5. **Exception Handling**: Custom exceptions and a global exception handler.
6. **Configuration**: Database configuration for JDBC.

### 1. Complete Code

#### 1.1. EmployeeManagementApplication.java

```java
package com.example.employee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmployeeManagementApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmployeeManagementApplication.class, args);
    }
}
```

#### 1.2. application.properties

```properties
# Database configuration
spring.datasource.url=jdbc:mysql://localhost:3306/your_database
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

#### 1.3. DatabaseConfig.java

```java
package com.example.employee.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
```

#### 1.4. Employee.java

```java
package com.example.employee.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    private int employeeNumber;
    private String lastName;
    private String firstName;
    private String extension;
    private String email;
    private String officeCode;
    private Integer reportsTo;
    private String jobTitle;
}
```

#### 1.5. EmployeeRowMapper.java

```java
package com.example.employee.mapper;

import com.example.employee.model.Employee;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeRowMapper implements RowMapper<Employee> {
    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Employee(
                rs.getInt("employeeNumber"),
                rs.getString("lastName"),
                rs.getString("firstName"),
                rs.getString("extension"),
                rs.getString("email"),
                rs.getString("officeCode"),
                rs.getObject("reportsTo", Integer.class),
                rs.getString("jobTitle")
        );
    }
}
```

#### 1.6. EmployeeDao.java

```java
package com.example.employee.dao;

import com.example.employee.model.Employee;

import java.util.List;

public interface EmployeeDao {
    List<Employee> findAll();
    Employee findById(int employeeNumber);
    Employee save(Employee employee);
    void update(Employee employee);
    void delete(int employeeNumber);
}
```

#### 1.7. EmployeeDaoImpl.java

```java
package com.example.employee.dao.impl;

import com.example.employee.dao.EmployeeDao;
import com.example.employee.mapper.EmployeeRowMapper;
import com.example.employee.model.Employee;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {
    private final JdbcTemplate jdbcTemplate;

    public EmployeeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Employee> findAll() {
        String sql = "SELECT * FROM employees";
        return jdbcTemplate.query(sql, new EmployeeRowMapper());
    }

    @Override
    public Employee findById(int employeeNumber) {
        String sql = "SELECT * FROM employees WHERE employeeNumber = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{employeeNumber}, new EmployeeRowMapper());
    }

    @Override
    public Employee save(Employee employee) {
        String sql = "INSERT INTO employees (employeeNumber, lastName, firstName, extension, email, officeCode, reportsTo, jobTitle) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, employee.getEmployeeNumber(), employee.getLastName(), employee.getFirstName(),
                employee.getExtension(), employee.getEmail(), employee.getOfficeCode(), employee.getReportsTo(), employee.getJobTitle());
        return employee;
    }

    @Override
    public void update(Employee employee) {
        String sql = "UPDATE employees SET lastName = ?, firstName = ?, extension = ?, email = ?, officeCode = ?, reportsTo = ?, jobTitle = ? WHERE employeeNumber = ?";
        jdbcTemplate.update(sql, employee.getLastName(), employee.getFirstName(), employee.getExtension(),
                employee.getEmail(), employee.getOfficeCode(), employee.getReportsTo(), employee.getJobTitle(), employee.getEmployeeNumber());
    }

    @Override
    public void delete(int employeeNumber) {
        String sql = "DELETE FROM employees WHERE employeeNumber = ?";
        jdbcTemplate.update(sql, employeeNumber);
    }
}
```

#### 1.8. EmployeeService.java

```java
package com.example.employee.service;

import com.example.employee.model.Employee;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAll();
    Employee findById(int employeeNumber);
    Employee save(Employee employee);
    void update(Employee employee);
    void delete(int employeeNumber);
}
```

#### 1.9. EmployeeServiceImpl.java

```java
package com.example.employee.service.impl;

import com.example.employee.dao.EmployeeDao;
import com.example.employee.exception.EmployeeNotFoundException;
import com.example.employee.exception.BadRequestException;
import com.example.employee.model.Employee;
import com.example.employee.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeDao employeeDao;

    public EmployeeServiceImpl(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    public List<Employee> findAll() {
        return employeeDao.findAll();
    }

    @Override
    public Employee findById(int employeeNumber) {
        Employee employee = employeeDao.findById(employeeNumber);
        if (employee == null) {
            throw new EmployeeNotFoundException("Employee with ID " + employeeNumber + " not found.");
        }
        return employee;
    }

    @Override
    public Employee save(Employee employee) {
        if (employee.getEmployeeNumber() <= 0) {
            throw new BadRequestException

("Employee number must be greater than 0.");
        }
        return employeeDao.save(employee);
    }

    @Override
    public void update(Employee employee) {
        Employee existingEmployee = findById(employee.getEmployeeNumber());
        if (existingEmployee == null) {
            throw new EmployeeNotFoundException("Employee with ID " + employee.getEmployeeNumber() + " not found.");
        }
        employeeDao.update(employee);
    }

    @Override
    public void delete(int employeeNumber) {
        Employee existingEmployee = findById(employeeNumber);
        if (existingEmployee == null) {
            throw new EmployeeNotFoundException("Employee with ID " + employeeNumber + " not found.");
        }
        employeeDao.delete(employeeNumber);
    }
}
```

#### 1.10. GlobalExceptionHandler.java

```java
package com.example.employee.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorDetails> handleEmployeeNotFoundException(EmployeeNotFoundException ex, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDetails> handleBadRequestException(BadRequestException ex, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetails> handleGlobalException(Exception ex, HttpServletRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(), "An error occurred", request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```

#### 1.11. BadRequestException.java

```java
package com.example.employee.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
```

#### 1.12. EmployeeNotFoundException.java

```java
package com.example.employee.exception;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
```

#### 1.13. ErrorDetails.java

```java
package com.example.employee.exception;

import java.time.LocalDateTime;

public class ErrorDetails {
    private LocalDateTime timestamp;
    private String message;
    private String details;

    public ErrorDetails(LocalDateTime timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    // Getters and Setters
}
```

#### 1.14. EmployeeController.java

```java
package com.example.employee.controller;

import com.example.employee.model.Employee;
import com.example.employee.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.findAll();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") int employeeNumber) {
        Employee employee = employeeService.findById(employeeNumber);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee createdEmployee = employeeService.save(employee);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") int employeeNumber, @RequestBody Employee employee) {
        employee.setEmployeeNumber(employeeNumber);
        employeeService.update(employee);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") int employeeNumber) {
        employeeService.delete(employeeNumber);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
```

### 2. Test Cases

#### 2.1. EmployeeControllerTest.java

```java
package com.example.employee.controller;

import com.example.employee.model.Employee;
import com.example.employee.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmployeeControllerTest {
    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllEmployees() {
        Employee employee1 = new Employee(1, "Doe", "John", "123", "john@example.com", "1", null, "Developer");
        Employee employee2 = new Employee(2, "Smith", "Jane", "456", "jane@example.com", "1", null, "Manager");
        List<Employee> employees = Arrays.asList(employee1, employee2);

        when(employeeService.findAll()).thenReturn(employees);

        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employees, response.getBody());
        verify(employeeService, times(1)).findAll();
    }

    @Test
    void getEmployeeById() {
        Employee employee = new Employee(1, "Doe", "John", "123", "john@example.com", "1", null, "Developer");

        when(employeeService.findById(1)).thenReturn(employee);

        ResponseEntity<Employee> response = employeeController.getEmployeeById(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employee, response.getBody());
        verify(employeeService, times(1)).findById(1);
    }

    @Test
    void createEmployee() {
        Employee employee = new Employee(1, "Doe", "John", "123", "john@example.com", "1", null, "Developer");

        when(employeeService.save(any(Employee.class))).thenReturn(employee);

        ResponseEntity<Employee> response = employeeController.createEmployee(employee);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(employee, response.getBody());
        verify(employeeService, times(1)).save(any(Employee.class));
    }

    @Test
    void updateEmployee() {
        Employee employee = new Employee(1, "Doe", "John", "123", "john@example.com", "1", null, "Developer");

        doNothing().when(employeeService).update(any(Employee.class));

        ResponseEntity<Employee> response = employeeController.updateEmployee(1, employee);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(employeeService, times(1)).update(any(Employee.class));
    }

    @Test
    void deleteEmployee() {
        doNothing().when(employeeService).delete(1);

        ResponseEntity<Void> response = employeeController.deleteEmployee(1);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(employeeService, times(1)).delete(1);
    }
}
```

#### 2.2. EmployeeServiceTest.java

```java
package com.example.employee.service;

import com.example.employee.dao.EmployeeDao;
import com.example.employee.exception.EmployeeNotFoundException;
import com.example.employee.exception.BadRequestException;
import com.example.employee.model.Employee;
import com.example.employee.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceTest {
    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Mock
    private EmployeeDao employeeDao;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        Employee employee1 = new Employee(1, "Doe", "John", "123", "john@example.com", "1", null, "Developer");
        Employee employee2 = new Employee(2, "Smith", "Jane", "456", "jane@example.com", "1", null, "Manager");
        List<Employee> employees = Arrays.asList(employee1, employee2);

        when(employeeDao.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.findAll();

        assertEquals(2, result.size());
        verify(employeeDao, times(1)).findAll();
    }

    @Test
    void findById() {
        Employee employee = new Employee(1, "Doe", "John", "123", "john@example.com", "1", null, "Developer");

        when(employeeDao.findById(1)).thenReturn

(employee);

        Employee result = employeeService.findById(1);

        assertEquals(employee, result);
        verify(employeeDao, times(1)).findById(1);
    }

    @Test
    void save() {
        Employee employee = new Employee(1, "Doe", "John", "123", "john@example.com", "1", null, "Developer");

        when(employeeDao.save(employee)).thenReturn(employee);

        Employee result = employeeService.save(employee);

        assertEquals(employee, result);
        verify(employeeDao, times(1)).save(employee);
    }

    @Test
    void save_InvalidEmployeeNumber() {
        Employee employee = new Employee(0, "Doe", "John", "123", "john@example.com", "1", null, "Developer");

        assertThrows(BadRequestException.class, () -> employeeService.save(employee));
    }

    @Test
    void update() {
        Employee existingEmployee = new Employee(1, "Doe", "John", "123", "john@example.com", "1", null, "Developer");
        Employee updatedEmployee = new Employee(1, "Doe", "John", "123", "john@example.com", "1", null, "Senior Developer");

        when(employeeDao.findById(1)).thenReturn(existingEmployee);
        doNothing().when(employeeDao).update(updatedEmployee);

        employeeService.update(updatedEmployee);

        verify(employeeDao, times(1)).update(updatedEmployee);
    }

    @Test
    void update_EmployeeNotFound() {
        Employee employee = new Employee(1, "Doe", "John", "123", "john@example.com", "1", null, "Developer");

        when(employeeDao.findById(1)).thenReturn(null);

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.update(employee));
    }

    @Test
    void delete() {
        Employee employee = new Employee(1, "Doe", "John", "123", "john@example.com", "1", null, "Developer");

        when(employeeDao.findById(1)).thenReturn(employee);
        doNothing().when(employeeDao).delete(1);

        employeeService.delete(1);

        verify(employeeDao, times(1)).delete(1);
    }

    @Test
    void delete_EmployeeNotFound() {
        when(employeeDao.findById(999)).thenReturn(null);

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.delete(999));
    }
}
```

### Conclusion

This Spring Boot application follows industry best practices, including proper project structure, exception handling, and testing. The provided test cases ensure that the application functions correctly and can handle edge cases gracefully. You can further extend this application as per your requirements! If you have any questions or need further enhancements, feel free to ask!