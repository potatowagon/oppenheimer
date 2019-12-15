package com.example.demo.entities;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

public class EmployeeList {
    @Valid
    private List<Employee> employees;

    public EmployeeList() {
        this.employees = new ArrayList<>();
    }

    public void addEmployee(Employee employee) {
        this.employees.add(employee);
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}

