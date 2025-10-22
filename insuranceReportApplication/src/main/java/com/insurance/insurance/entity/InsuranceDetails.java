package com.insurance.insurance.entity;

import com.insurance.insurance.dto.InsuranceDetailsDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class InsuranceDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String planType;
    private String planStatus;
    private String gender;
    private LocalDate planStartDate;
    private LocalDate planEndDate;

    public InsuranceDetailsDTO toDTO(){
        return new InsuranceDetailsDTO(this.id,this.planType,this.planStatus,this.gender,this.planStartDate,
                this.planEndDate);
    }
}
