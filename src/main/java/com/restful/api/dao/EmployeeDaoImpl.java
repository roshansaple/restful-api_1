package com.restful.api.dao;


import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.restful.api.mapper.EmployeeRowMapper;
import com.restful.api.model.Employee;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {
    private final JdbcTemplate jdbcTemplate;

    public EmployeeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Employee> findAll() {
        String sql = "SELECT * FROM employees";
        List<Employee> employeeList = jdbcTemplate.query(sql, new EmployeeRowMapper());
		return employeeList;
    }

    @Override
    public Employee findById(int employeeNumber) {
        String sql = "SELECT * FROM employees WHERE employeeNumber = ?";
        Object[] args = new Object[]{employeeNumber};
		
        Employee employee = jdbcTemplate.queryForObject(sql, args, new EmployeeRowMapper());
		return employee;
    }

    @Override
    public Employee save(Employee employee) {
        String sql = "INSERT INTO employees (employeeNumber, lastName, firstName, extension, email, officeCode, reportsTo, jobTitle) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        Object[] args = new Object[]{ employee.getEmployeeNumber(), employee.getLastName(), employee.getFirstName(),
                employee.getExtension(), employee.getEmail(), employee.getOfficeCode(), employee.getReportsTo(), employee.getJobTitle()};
        
        jdbcTemplate.update(sql, args);
        return employee;
    }

    @Override
    public void update(Employee employee) {
        String sql = "UPDATE employees SET lastName = ?, firstName = ?, extension = ?, email = ?, officeCode = ?, reportsTo = ?, jobTitle = ? WHERE employeeNumber = ?";
       
        Object[] args = new Object[]{employee.getLastName(), employee.getFirstName(), employee.getExtension(),
                employee.getEmail(), employee.getOfficeCode(), employee.getReportsTo(), employee.getJobTitle(), employee.getEmployeeNumber() };
        
        
        jdbcTemplate.update(sql, args );
    }

    @Override
    public void delete(int employeeNumber) {
        String sql = "DELETE FROM employees WHERE employeeNumber = ?";
        jdbcTemplate.update(sql, employeeNumber);
    }
}
