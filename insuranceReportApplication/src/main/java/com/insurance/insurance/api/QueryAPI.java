package com.insurance.insurance.api;

import com.insurance.insurance.entity.InsuranceDetails;
import com.insurance.insurance.service.InsuranceDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/insurance")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class QueryAPI {

    private final  InsuranceDetailsService insuranceDetailsService;

    @GetMapping("/search")
    public List<InsuranceDetails> searchPatients(
            @RequestParam(required = false) String planType,
            @RequestParam(required = false) String planStatus,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate planStartDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate planEndDate
    ) {
        if (planType != null && planType.isEmpty()) planType = null;
        if (planStatus != null && planStatus.isEmpty()) planStatus = null;
        if (gender != null && gender.isEmpty()) gender = null;

        // Dates are already LocalDate, null check is enough
        if (planStartDate != null && planStartDate.toString().isEmpty()) planStartDate = null;
        if (planEndDate != null && planEndDate.toString().isEmpty()) planEndDate = null;
        if(planStartDate != null && planEndDate != null && planStartDate.isAfter(planEndDate)) {
            return null;
        }

        if(planType==null && planStatus==null && gender==null && planStartDate==null && planEndDate==null)
            return null;

        return insuranceDetailsService.searchQuery(planType, planStatus, gender, planStartDate, planEndDate);


    }

    @PostMapping("/export/{type}")
    public ResponseEntity<byte[]> exportReport(@PathVariable String type, @RequestBody List<InsuranceDetails> data) throws Exception {
        byte[] fileBytes;

        if (type.equalsIgnoreCase("excel")) {
            fileBytes = insuranceDetailsService.exportToExcel(data);
        } else if (type.equalsIgnoreCase("pdf")) {
            fileBytes = insuranceDetailsService.exportToPdf(data);
        } else {
            throw new IllegalArgumentException("Invalid file type: " + type);
        }

        // Send email with attachment (optional)
        insuranceDetailsService.sendReportEmail(fileBytes, type);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Insurance_Report." + (type.equals("excel") ? "xlsx" : "pdf"));

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(type.equals("excel") ?
                        MediaType.APPLICATION_OCTET_STREAM : MediaType.APPLICATION_PDF)
                .body(fileBytes);
    }

}
