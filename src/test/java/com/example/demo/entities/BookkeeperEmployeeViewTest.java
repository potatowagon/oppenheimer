package com.example.demo.entities;

import junitparams.*;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.*;
import static junitparams.JUnitParamsRunner.*;

@RunWith(JUnitParamsRunner.class)
public class BookkeeperEmployeeViewTest {

    @Test
    @junitparams.Parameters({
            "123, 123",
            "1234, 1234",
            "12345, 1234$",
            "123456789, 1234$$$$$"
    })
    public void getMaskedId(Long id, String expectedMaskedId) {
        assertThat(new BookkeeperEmployeeView(id, "mock", 0).getMaskedId()).isEqualTo(expectedMaskedId);
    }

    @Test
    @junitparams.Parameters({
            "1000, 10, 17, Male, 990",
            "1000, 20, 18, Male, 980",
            "1000, 10, 20, Male, 792",
            "1000, 10, 18, Female, 1490",
            "1000, 10, 76, Male, 50",
            "1000, 10, 75, Male, 363",
            "10, 10, 75, Male, 0",
            "10, 9, 18, Male, 50",
            "10, 9, 18, Female, 501",
            "10, 9, 50, Male, 50",
            "10, 9, 50, Female, 501",
            "10, 9, 75, Male, 0",
            "10, 9, 75, Female, 500"
    })
    public void calcTaxRelief(Integer salary, Integer taxPaid, Integer age, String gender, Integer expectedTaxRelief) {
        BookkeeperEmployeeView bev = new BookkeeperEmployeeView();
        assertThat(bev.calcTaxRelief(salary, taxPaid, age, gender)).isEqualTo(expectedTaxRelief);
    }

    @Test
    @junitparams.Parameters({
            "1994-01-01, 26",
            "2002-01-01, 18",
            "2000-01-01, 20",
            "1985-01-01, 35"
    })
    public void ageFromBirthday(String birthday, Integer expectedAge) {
        BookkeeperEmployeeView bev = new BookkeeperEmployeeView();
        assertThat(bev.ageFromBirthday(birthday)).isEqualTo(expectedAge);
    }
}