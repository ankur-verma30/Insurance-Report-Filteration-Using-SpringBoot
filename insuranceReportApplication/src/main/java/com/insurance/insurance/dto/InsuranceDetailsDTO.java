package com.insurance.insurance.dto;

import com.insurance.insurance.entity.InsuranceDetails;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsuranceDetailsDTO {
    private Long id;
    private String planType;
    private String planStatus;
    private String gender;
    private LocalDate planStartDate;
    private LocalDate planEndDate;

    public InsuranceDetails toEntity() {
        return new InsuranceDetails(this.id,this.planType,this.planStatus,this.gender,this.planStartDate,
                this.planEndDate);
    }
}
