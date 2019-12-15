package com.example.demo.repositories;

import com.example.demo.entities.Employee;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {
    
    List<Employee> findByName(String name);
}
