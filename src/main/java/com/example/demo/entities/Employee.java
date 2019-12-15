package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Entity
public class Employee {
    
    @Id
    @NotBlank(message = "Id is mandatory")
    private long id;

    @NotBlank(message = "Name is mandatory")
    private String name;
    
    @NotBlank(message = "Gender is mandatory")
    private String gender;

    @NotBlank(message = "Salary is mandatory")
    private int salary;

    @NotBlank(message = "birthday is mandatory")
    private String birthday;

    @NotBlank(message = "tax paid is mandatory")
    private String taxPaid;

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

    public String getTaxPaid() {
        return taxPaid;
    }

    public void setTaxPaid(String taxPaid) {
        this.taxPaid = taxPaid;
    }

    @Override
    public String toString() {
        return "Employee{" + "id=" + id + ", name=" + name + ", gender=" + gender + '}';
    }
}