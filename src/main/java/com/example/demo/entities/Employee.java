package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class Employee {
    
    @Id
    @NotNull(message = "Id is mandatory")
    private long id;

    @NotBlank(message = "Name is mandatory")
    private String name;
    
    @NotBlank(message = "Gender is mandatory")
    private String gender;

    @NotNull(message = "Salary is mandatory")
    private int salary;

    @NotBlank(message = "birthday is mandatory")
    private String birthday;

    @NotNull(message = "tax paid is mandatory")
    private int taxPaid;

    public Employee() {}

    public Employee(String name, String gender) {
        this.name = name;
        this.gender = gender;
    }

    public void setId(long id) {
        this.id = id;
    }
    
    public long getId() {
        return id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getTaxPaid() {
        return taxPaid;
    }

    public void setTaxPaid(int taxPaid) {
        this.taxPaid = taxPaid;
    }

    @Override
    public String toString() {
        return "Employee{" + "id=" + id + ", name=" + name + ", gender=" + gender + '}';
    }
}