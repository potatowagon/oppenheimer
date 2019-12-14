package com.example.demo.controllers;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class UserController {
    
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String showIndex() {
        return "index";
    }

    @GetMapping("/clerk")
    public String showSignUpForm(User user) {
        return "add-user";
    }
    
    @PostMapping("/adduser")
    public String addUser(@Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "add-user";
        }
        
        userRepository.save(user);
        model.addAttribute("status", "User added");
        return "add-user";
    }

    @GetMapping("/bookkeeper")
    public String showUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "view-all";
    }

    @GetMapping("/admin")
    public String showCRUD(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "crud";
    }

    @GetMapping("/edit/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        return "update-user";
    }
    
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "update-user";
        }
        
        userRepository.save(user);
        model.addAttribute("users", userRepository.findAll());
        return "crud";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        userRepository.delete(user);
        model.addAttribute("users", userRepository.findAll());
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
