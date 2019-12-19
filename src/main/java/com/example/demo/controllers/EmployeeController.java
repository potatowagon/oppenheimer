package com.example.demo.controllers;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
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
    public String saveEmployees(@ModelAttribute("form") @Valid EmployeeList form, BindingResult result, Model model) {
        if (result.hasErrors()){
            model.addAttribute("status", "One or more fields invalid.");
            String maxBirthday = LocalDate.now().toString();
            model.addAttribute("maxBirthday", maxBirthday);
            return "add-employee";
        }
        System.out.println("iterating employee form next");
        form.getEmployees().forEach((employee) -> {
            saveEmployee(employee, result);
        });
        model.addAttribute("employees" , form.getEmployees());
        return "insert-employee-success";
    }

    private boolean saveEmployee(@Valid Employee employee, BindingResult result) {
        if (result.hasErrors()) {
            System.out.println("Invalid employee");
            return false;
        }
        Optional<Employee> oldEmployee = employeeRepository.findById(employee.getId());
        if (oldEmployee.isPresent()) {
            employeeRepository.delete(oldEmployee.get());
        }
        employeeRepository.save(employee);
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

    @PostMapping("/uploadFile")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model, @Valid Employee modelEmployee, BindingResult result) throws IOException {
        //read file
        Reader targetReader = new StringReader(new String(file.getBytes()));
        //output  file to stdout
        System.out.println(new String(file.getBytes()));

        EmployeeList employeeList = new EmployeeList();
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
                model.addAttribute("status", "CSV invalid");
                String maxBirthday = LocalDate.now().toString();
                model.addAttribute("maxBirthday", maxBirthday);
                return "add-employee";
            }
        }

        model.addAttribute("form", employeeList);
        return "forward:/saveemployees";
    }

    @GetMapping("/gov")
    public String showDispenseCash() {
        return "dispense-cash";
    }

    @GetMapping("/dispensecash")
    public String showDispenseCashSuccess() {
        return "dispense-cash-success";
    }

    @GetMapping("/error")
    public String showErrorPage(HttpServletRequest request, Model model) {
        return "error";
    }
}
