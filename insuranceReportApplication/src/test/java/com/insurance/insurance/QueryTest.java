package com.insurance.insurance;

import com.insurance.insurance.entity.InsuranceDetails;
import com.insurance.insurance.service.InsuranceDetailsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest

public class QueryTest {

    @Autowired
    private InsuranceDetailsService insuranceDetailsService;

    @Test
    void testSearchQuery1() {
        // Search by planType only
        List<InsuranceDetails> result1 = insuranceDetailsService.searchQuery("Cash", null, null, null, null);
        result1.stream().forEach(System.out::println);
    }

    @Test
    void testSearchQuery2(){
        // Search by planStatus only
        List<InsuranceDetails> result2 = insuranceDetailsService.searchQuery(null, "Denied", null, null, null);
        assertEquals(1, result2.size());
        result2.stream().forEach(System.out::println);
    }

    @Test
    void testSearchQuery3(){
        // Search by gender and planType
        List<InsuranceDetails> result3 = insuranceDetailsService.searchQuery("Cash", null, "Female", null, null);
        result3.stream().forEach(System.out::println);
    }

    @Test
    void testSearchQuery4(){
        // Search by date range
        List<InsuranceDetails> result4 = insuranceDetailsService.searchQuery(null, null, null,
                LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31));
        assertEquals(2, result4.size());
        result4.stream().forEach(System.out::println);
    }
}
