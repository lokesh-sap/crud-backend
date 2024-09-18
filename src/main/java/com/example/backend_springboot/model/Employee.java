package com.example.backend_springboot.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstname;

    @Column(name="last_name")
    private String lastname;

    @Column(name="email_id")
    private String emailId;
    public Employee(){

    }
    public Employee(String firstname, String lastname, String emailId){
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailId = emailId;
    }

}
