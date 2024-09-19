package com.example.backend_springboot.controller;

import com.example.backend_springboot.exception.ResourceNotFoundException;
import com.example.backend_springboot.model.Employee;
import com.example.backend_springboot.repositary.EmployeeRepositary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/")
public class EmployeeController {
    @Autowired
    private EmployeeRepositary employeeRepositary;

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeRepositary.findAll();
    }

    @PostMapping("/employees")
    public Employee createEmployee(@RequestBody Employee employee){
        return employeeRepositary.save(employee);
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id){
        Employee employee= employeeRepositary.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with the id: "+id));
        return ResponseEntity.ok(employee);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
        Employee employee = employeeRepositary.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with the id: " + id));
        employee.setFirstname(employeeDetails.getFirstname());
        employee.setLastname(employeeDetails.getLastname());
        employee.setEmailId(employeeDetails.getEmailId());
        Employee updatedEmployee = employeeRepositary.update(employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long id){
        Employee employee= employeeRepositary.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with the id: "+id));
        employeeRepositary.delete(employee);
        Map<String,Boolean> res=new HashMap<>();
        res.put("deleted", true);
        return ResponseEntity.ok(res);
    }

}
