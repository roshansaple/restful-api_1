package com.restful.api.mapper;


import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.restful.api.model.Employee;

public class EmployeeRowMapper implements RowMapper<Employee> {
    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
    	
        int employeeNumber = rs.getInt("employeeNumber");
		String lastName = rs.getString("lastName");
		
		Employee employee = new Employee(
                employeeNumber,
                lastName,
                rs.getString("firstName"),
                rs.getString("extension"),
                rs.getString("email"),
                rs.getString("officeCode"),
                rs.getObject("reportsTo", Integer.class),
                rs.getString("jobTitle")
        );
		return employee;
    }
}
