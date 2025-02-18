package com.example.backend_springboot.repositary;

import com.example.backend_springboot.model.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sap.xs.audit.api.exception.AuditLogException;
import com.sap.xs.audit.api.exception.AuditLogNotAvailableException;
import com.sap.xs.audit.api.exception.AuditLogWriteException;
import com.sap.xs.audit.api.v2.AuditLogMessageFactory;
import com.sap.xs.audit.api.v2.AuditedDataSubject;
import com.sap.xs.audit.api.v2.AuditedObject;
import com.sap.xs.audit.api.v2.ConfigurationChangeAuditMessage;
import com.sap.xs.audit.api.v2.DataAccessAuditMessage;
import com.sap.xs.audit.api.v2.SecurityEventAuditMessage;
import com.sap.xs.audit.client.impl.v2.AuditLogMessageFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import com.fasterxml.jackson.databind.ObjectMapper;

import static java.lang.String.format;

@Repository
public class EmployeeRepositary {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeRepositary.class);
    private final List<Employee> employeeList = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    private final ObjectMapper objectMapper;

    private final AuditLogMessageFactory auditLogFactory;

    @Autowired
    public EmployeeRepositary(ObjectMapper objectMapper) throws AuditLogException {
        this.objectMapper = objectMapper;
        this.auditLogFactory = new AuditLogMessageFactoryImpl();
    }

    public List<Employee> findAll() {
        return new ArrayList<>(employeeList);
    }

    public Employee save(Employee employee) throws AuditLogException, JsonProcessingException {
        if (employee.getId() == null) {
            employee.setId(idCounter.getAndIncrement());
        }
        employeeList.add(employee);

        AuditedObject ao = auditLogFactory.createAuditedObject();
        SecurityEventAuditMessage am = auditLogFactory.createSecurityEventAuditMessage();

        LogRecord auditLogData = new LogRecord(Level.INFO, "Audit log message");
        auditLogData.setSourceClassName("EmployeeRepositary");
        auditLogData.setSourceMethodName("save");
        auditLogData.setLoggerName("EmployeeRepositary");
        auditLogData.setParameters(new Object[]{employee});
        auditLogData.setResourceBundleName("com.example.backend_springboot.repositary.EmployeeRepositary");
        auditLogData.setLevel(Level.INFO);
        auditLogData.setInstant(java.time.Instant.now());
        String data = objectMapper.writeValueAsString(auditLogData);
        SecurityEventAuditMessage auditMessage = auditLogFactory.createSecurityEventAuditMessage();
        auditMessage.setUser("System");
        auditMessage.setData(data);
        try {
            auditMessage.log();
        } catch (AuditLogNotAvailableException e) {
            throw new RuntimeException(
                    format("Could not log security event: {0} for IP: {1} and user: {2}, as audit log service is not available", data), e);
        } catch (AuditLogWriteException e) {
            throw new RuntimeException(format("Could not log security event: {0} for IP: {1} and user: {2}. Error message: {3}, errors: {4}", data, e.getMessage(), e.getErrors()), e);
        }




        ao.setType("Employee");
        ao.addIdentifier("ID", employee.getId().toString());

        AuditedDataSubject as = auditLogFactory.createAuditedDataSubject();
        as.setRole("User");
        as.setType("Employee");
        as.addIdentifier("ID", employee.getId().toString());

        DataAccessAuditMessage message = auditLogFactory.createDataAccessAuditMessage();
        message.setChannel("EmployeeService");
        message.setObject(ao);
        message.addAttribute("Action", true);
        message.setDataSubject(as);
        message.setUser("System");
        message.log();

//        logger.info("Employee added with ID: {}", employee.getId());
        return employee;
    }

    public Employee update(Employee employee) throws AuditLogException {
        Optional<Employee> existingEmployee = findById(employee.getId());
        if (existingEmployee.isPresent()) {
            employeeList.remove(existingEmployee.get());
            employeeList.add(employee);

            AuditedObject ao = auditLogFactory.createAuditedObject();
            ao.setType("Employee");
            ao.addIdentifier("ID", employee.getId().toString());

            AuditedDataSubject as = auditLogFactory.createAuditedDataSubject();
            as.setRole("User");
            as.setType("Employee");
            as.addIdentifier("ID", employee.getId().toString());

            ConfigurationChangeAuditMessage message = auditLogFactory.createConfigurationChangeAuditMessage();
            message.setObject(ao);
            message.addValue("Action", "Update", "Updated Employee ID: " + employee.getId());
            message.setUser("System");
            message.logSuccess();

            logger.info("Employee updated with ID: {}", employee.getId());
            return employee;
        } else {
            logger.error("Employee not found with ID: {}", employee.getId());
            throw new RuntimeException("Employee not found with ID: " + employee.getId());
        }
    }

    public Optional<Employee> findById(Long id) {
        return employeeList.stream()
                .filter(emp -> emp.getId().equals(id))
                .findFirst();
    }

    public void delete(Employee employee) throws AuditLogException {
        employeeList.removeIf(emp -> emp.getId().equals(employee.getId()));

        AuditedObject ao = auditLogFactory.createAuditedObject();
        ao.setType("Employee");
        ao.addIdentifier("ID", employee.getId().toString());

        AuditedDataSubject as = auditLogFactory.createAuditedDataSubject();
        as.setRole("User");
        as.setType("Employee");
        as.addIdentifier("ID", employee.getId().toString());

        SecurityEventAuditMessage message = auditLogFactory.createSecurityEventAuditMessage();
        message.setData("Deleted Employee ID: " + employee.getId());
        message.setUser("System");
        message.log();

        logger.info("Employee deleted with ID: {}", employee.getId());
    }
}