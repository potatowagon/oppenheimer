package com.example.demo.controllers;

import javax.validation.Valid;

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

import java.io.*;

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
        return "add-employee";
    }

    @PostMapping("/saveemployees")
    public String saveEmployees(@ModelAttribute @Valid EmployeeList form, BindingResult result, Model model) {
        if (result.hasErrors()){
            return "add-employee";
        }
        System.out.println("iterating employee form next");
        form.getEmployees().forEach((employee) -> {

            saveEmployee(employee, result, model);
            model.addAttribute("status", "Employee saved");
        });
        model.addAttribute("form" , form);
        return "add-employee";
    }

    private boolean saveEmployee(@Valid Employee employee, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return false;
        }

        employeeRepository.save(employee);
        return true;
    }

    @PostMapping("/addemployee")
    public String addEmployee(@ModelAttribute EmployeeList form, Model model) {
        form.addEmployee(new Employee());
        model.addAttribute("form" , form);
        return "add-employee";
    }

    @GetMapping("/bookkeeper")
    public String showEmployees(Model model) {
        model.addAttribute("employees", employeeRepository.findAll());
        return "view-all";
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
    public void handleFileUpload(@RequestParam("file") MultipartFile file) throws Exception {
        File convFile = new File( file.getOriginalFilename() );
        FileOutputStream fos = new FileOutputStream( convFile );
        fos.write( file.getBytes() );
        fos.close();
        FileReader fr = new FileReader(convFile);
        BufferedReader br=new BufferedReader(fr);
        String line;
        while((line=br.readLine())!=null) {
            System.out.println(br.readLine());
        }
    }
}
