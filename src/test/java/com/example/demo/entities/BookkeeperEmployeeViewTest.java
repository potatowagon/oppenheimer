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
    public void getTaxRelief() {
    }
}