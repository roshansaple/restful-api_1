package com.restful.api.service;



import java.util.List;

import com.restful.api.model.Employee;

public interface EmployeeService {
    List<Employee> findAll();
    Employee findById(int employeeNumber);
    Employee save(Employee employee);
    void update(Employee employee);
    void delete(int employeeNumber);
}
