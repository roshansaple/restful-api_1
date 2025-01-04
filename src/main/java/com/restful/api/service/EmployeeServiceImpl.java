package com.restful.api.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.restful.api.dao.EmployeeDao;
import com.restful.api.exception.BadRequestException;
import com.restful.api.exception.EmployeeNotFoundException;
import com.restful.api.model.Employee;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeDao employeeDao;

    public EmployeeServiceImpl(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> employeeList = employeeDao.findAll();
		return employeeList;
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
            throw new BadRequestException("Employee number must be greater than 0.");
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
