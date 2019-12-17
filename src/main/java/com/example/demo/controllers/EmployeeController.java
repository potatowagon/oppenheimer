package com.example.demo.controllers;

import javax.validation.Valid;

import com.example.demo.entities.BookkeeperEmployeeView;
import com.example.demo.entities.EmployeeList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entities.Employee;
import com.example.demo.repositories.EmployeeRepository;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Controller
@SessionAttributes("form")
public class EmployeeController {
    
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    private String showInvalidEmployeeInsertion(Model model, String msg){
        model.addAttribute("status", msg);
        String maxBirthday = LocalDate.now().toString();
        model.addAttribute("maxBirthday", maxBirthday);
        return "add-employee";
    }
    @GetMapping("/")
    public String showIndex() {
        return "index";
    }

    @GetMapping("/clerk")
    public String showSignUpForm(Model model) {
        EmployeeList employeesForm = new EmployeeList();
        employeesForm.addEmployee(new Employee());
        model.addAttribute("form" , employeesForm);
        String maxBirthday = LocalDate.now().toString();
        model.addAttribute("maxBirthday", maxBirthday);
        return "add-employee";
    }

    @PostMapping("/saveemployees")
    public String saveEmployees(@ModelAttribute @Valid EmployeeList form, BindingResult result, Model model) {
        if (result.hasErrors()){
            showInvalidEmployeeInsertion(model, "One or more fields invalid.");
        }
        System.out.println("iterating employee form next");
        form.getEmployees().forEach((employee) -> {

            saveEmployee(employee, result, model);
        });
        model.addAttribute("employees" , form.getEmployees());
        return "insert-employee-success";
    }

    private boolean saveEmployee(@Valid Employee employee, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println("employee invalid");
            return false;
        }
        Optional<Employee> oldEmployee = employeeRepository.findById(employee.getId());
        if (oldEmployee.isPresent()) {
            employeeRepository.delete(oldEmployee.get());
        }
        employeeRepository.save(employee);
        System.out.println("employee saved");
        return true;
    }

    @PostMapping("/addemployee")
    public String addEmployee(@ModelAttribute EmployeeList form, Model model) {
        form.addEmployee(new Employee());
        model.addAttribute("form" , form);
        String maxBirthday = LocalDate.now().toString();
        model.addAttribute("maxBirthday", maxBirthday);
        return "add-employee";
    }

    @GetMapping("/bookkeeper")
    public String showEmployees(Model model) {
        List<Employee> employees = (List<Employee>) employeeRepository.findAll();
        List<BookkeeperEmployeeView> bookkeeperEmployeeViews = new ArrayList<>();
        for(Employee employee : employees){
            bookkeeperEmployeeViews.add(new BookkeeperEmployeeView(employee));
        }

        model.addAttribute("employees", bookkeeperEmployeeViews);
        return "view-all";
    }

    @PostMapping("/deleteuninserted/{index}")
    public String deleteUninsertedEmployee(@ModelAttribute EmployeeList form, @PathVariable("index") int index, Model model) {
        form.getEmployees().remove(index);
        model.addAttribute("form" , form);
        String maxBirthday = LocalDate.now().toString();
        model.addAttribute("maxBirthday", maxBirthday);
        return "add-employee";
    }

    @GetMapping("/admin")
    public String showCRUD(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        return "crud";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));
        model.addAttribute("employee", employee);
        return "update-employee";
    }
    
    @PostMapping("/update/{id}")
    public String updateEmployee(@PathVariable("id") long id, @Valid Employee employee, BindingResult result, Model model) {
        if (result.hasErrors()) {
            employee.setId(id);
            return "update-user";
        }
        
        employeeRepository.save(employee);
        model.addAttribute("employees", employeeRepository.findAll());
        return "crud";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable("id") long id, Model model) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid employee Id:" + id));
        employeeRepository.delete(employee);
        model.addAttribute("employees", employeeRepository.findAll());
        return "crud";
    }

    @PostMapping("/uploadFile")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model, @Valid Employee modelEmployee, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            showInvalidEmployeeInsertion(model, "One or more fields invalid.");
        }
        //read file
        Reader targetReader = new StringReader(new String(file.getBytes()));
        //output  file to stdout
        System.out.println(new String(file.getBytes()));

        EmployeeList employeeList = new EmployeeList();
        //Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(targetReader);
        CSVFormat csvFormat = CSVFormat.newFormat(',');
        Iterable<CSVRecord> records = csvFormat.withFirstRecordAsHeader().parse(targetReader);
        for (CSVRecord record : records) {
            Employee employee = new Employee();
            try {
                employee.setId(Long.parseLong(record.get("id")));
                employee.setName(record.get("name"));
                employee.setBirthday(record.get("birthday"));
                employee.setGender(record.get("gender"));
                employee.setSalary(Integer.parseInt(record.get("salary")));
                employee.setTaxPaid(Integer.parseInt(record.get("taxPaid")));
                employeeList.addEmployee(employee);
            } catch (java.lang.IllegalArgumentException e){
                System.out.println(e);
                showInvalidEmployeeInsertion(model, "Invalid CSV");
            }
        }
        employeeList.getEmployees().forEach((employee) -> {
            boolean success = saveEmployee(employee, result, model);
            System.out.println(employee.toString());
            System.out.println(success);
            if (!success){
                return showInvalidEmployeeInsertion(model, "One or more fields invalid.");
            }
        });
        model.addAttribute("employees" , employeeList.getEmployees());
        return "insert-employee-success";
    }
}
