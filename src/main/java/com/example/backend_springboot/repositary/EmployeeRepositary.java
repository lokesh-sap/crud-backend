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

    public Employee save(Employee employee) {
        if (employee.getId() == null) {
            employee.setId(idCounter.getAndIncrement());
        }
        employeeList.add(employee);
        return employee;
    }

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

    public Optional<Employee> findById(Long id) {
        return employeeList.stream()
                .filter(emp -> emp.getId().equals(id))
                .findFirst();
    }

    public void delete(Employee employee) {
        employeeList.removeIf(emp -> emp.getId().equals(employee.getId()));
    }
}
