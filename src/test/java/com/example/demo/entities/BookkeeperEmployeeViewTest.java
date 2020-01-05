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

        Employee employee = new Employee();
        employee.setId(id);
        employee.setName("mock");
        employee.setBirthday("2020-01-02");
        employee.setSalary(0);
        employee.setTaxPaid(0);
        employee.setGender("");
        assertThat(new BookkeeperEmployeeView(employee).getMaskedId()).isEqualTo(expectedMaskedId);
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