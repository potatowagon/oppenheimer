package com.example.demo.entities;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class BookkeeperEmployeeView {
    private String maskedId;
    private String name;
    private int taxRelief;

    public BookkeeperEmployeeView() {}

    public BookkeeperEmployeeView(long id, String name, int taxRelief) {
        this.maskedId = maskId(id);
        this.name = name;
        this.taxRelief = taxRelief;
    }

    public BookkeeperEmployeeView(Employee employee) {
        this.maskedId = maskId(employee.getId());
        this.name = employee.getName();
        this.taxRelief = calcTaxRelief(employee.getSalary(), employee.getTaxPaid(), ageFromBirthday(employee.getBirthday()), employee.getGender());
    }

    public String getMaskedId() {
        return maskedId;
    }

    public void setMaskedId(String maskedId) {
        this.maskedId = maskedId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTaxRelief() {
        return taxRelief;
    }

    public void setTaxRelief(int taxRelief) {
        this.taxRelief = taxRelief;
    }

    private String maskId(long id){
        char[] arr = String.valueOf(id).toCharArray();
        for (int i=0; i<arr.length; i++){
            if (i >= 4){
                arr[i] = '$';
            }
        }
        return new String(arr);
    }

    public int calcTaxRelief(int salary, int taxPaid, int age, String gender){
        double var = 0;
        if (age <= 18){
            var = 1;
        }
        else if (age <= 35){
            var = 0.8;
        }
        else if (age <= 50){
            var = 0.5;
        }
        else if (age <= 75){
            var = 0.367;
        }
        else if (age >= 76){
            var = 0.05;
        }

        int genderBonus = 0;
        if (gender.equals("Male")){
            genderBonus = 0;
        }
        else if (gender.equals("Female")){
            genderBonus = 500;
        }

        double taxRelief = ((salary - taxPaid) * var) + genderBonus;
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.DOWN);
        double taxRelief2dp = Double.valueOf(df.format(taxRelief));
        int taxReliefRounded = (int) Math.round(taxRelief2dp);
        if (taxReliefRounded > 0 && taxReliefRounded < 50){
            return 50;
        }
        return taxReliefRounded;
    }

    public int ageFromBirthday(String birthday){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDateBirthday = LocalDate.parse(birthday, formatter);
        LocalDate currentDate = LocalDate.now();
        return Period.between(localDateBirthday, currentDate).getYears();
    }
}
