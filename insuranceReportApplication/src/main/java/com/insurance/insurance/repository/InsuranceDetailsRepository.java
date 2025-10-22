package com.insurance.insurance.repository;

import com.insurance.insurance.entity.InsuranceDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InsuranceDetailsRepository extends JpaRepository<InsuranceDetails, Long>,
        JpaSpecificationExecutor<InsuranceDetails> {

}