package com.example.backend_springboot.repositary;

import com.example.backend_springboot.model.Employee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class EmployeeRepositary {

    private final List<Employee> employeeList = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1); // To simulate auto-increment IDs

    // Get all employees
    public List<Employee> findAll() {
        return new ArrayList<>(employeeList);
    }

    // Save a new employee
    public Employee save(Employee employee) {
        if (employee.getId() == null) {
            // Assign a new ID for a new employee
            employee.setId(idCounter.getAndIncrement());
        }
        // Add the new employee to the list
        employeeList.add(employee);
        return employee;
    }

    // Update an existing employee
    public Employee update(Employee employee) {
        Optional<Employee> existingEmployee = findById(employee.getId());
        if (existingEmployee.isPresent()) {
            // Remove the existing employee and add the updated one
            employeeList.remove(existingEmployee.get());
            employeeList.add(employee);
            return employee;
        } else {
            throw new RuntimeException("Employee not found with ID: " + employee.getId());
        }
    }

    // Find employee by ID
    public Optional<Employee> findById(Long id) {
        return employeeList.stream()
                .filter(emp -> emp.getId().equals(id))
                .findFirst();
    }

    // Delete an employee
    public void delete(Employee employee) {
        employeeList.removeIf(emp -> emp.getId().equals(employee.getId()));
    }
}
