package com.insurance.insurance.service;

import com.insurance.insurance.entity.InsuranceDetails;
import com.insurance.insurance.repository.InsuranceDetailsRepository;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class InsuranceDetailsService {

    private final InsuranceDetailsRepository insuranceDetailsRepository;

    @Autowired
    private JavaMailSender mailSender;


    public List<InsuranceDetails> searchQuery(String planType, String planStatus, String gender,
                                                 LocalDate planStartDate, LocalDate planEndDate) {
        return insuranceDetailsRepository.findAll((root, query, cb) -> {
            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

            if (planType != null) predicates.add(cb.like(cb.lower(root.get("planType")), "%" + planType + "%"));

            if (planStatus != null) {
                predicates.add(cb.equal(root.get("planStatus"), planStatus));
            }
            if (gender != null) {
                predicates.add(cb.equal(root.get("gender"), gender));
            }
            if (planStartDate != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("planStartDate"), planStartDate));
            }
            if (planEndDate != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("planEndDate"), planEndDate));
            }

            return cb.and(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        });
    }

    public byte[] exportToExcel(List<InsuranceDetails> data) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Insurance Report");

        Row header = sheet.createRow(0);
        String[] columns = {"Plan Type", "Plan Status", "Gender", "Start Date", "End Date"};
        for (int i = 0; i < columns.length; i++) {
            header.createCell(i).setCellValue(columns[i]);
        }

        int rowIdx = 1;
        for (InsuranceDetails record : data) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(record.getPlanType());
            row.createCell(1).setCellValue(record.getPlanStatus());
            row.createCell(2).setCellValue(record.getGender().toString());
            row.createCell(3).setCellValue(record.getPlanStartDate().toString());
            row.createCell(4).setCellValue(record.getPlanEndDate().toString());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();
        return out.toByteArray();
    }

    public byte[] exportToPdf(List<InsuranceDetails> data) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter.getInstance(document, out);
        document.open();

        // Title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Insurance Report", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" ")); // Empty line

        // Table with 5 columns
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10f);
        table.setSpacingAfter(10f);

        // Header Font
        Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        // Add table headers
        String[] headers = {"Plan Type", "Plan Status", "Gender", "Start Date", "End Date"};
        for (String header : headers) {
            PdfPCell headerCell = new PdfPCell(new Paragraph(header, headFont));
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(headerCell);
        }

        // Add data rows
        for (InsuranceDetails record : data) {
            table.addCell(record.getPlanType() != null ? record.getPlanType() : "-");
            table.addCell(record.getPlanStatus() != null ? record.getPlanStatus() : "-");
            table.addCell(record.getGender() != null ? record.getGender().toString() : "-");
            table.addCell(record.getPlanStartDate() != null ? record.getPlanStartDate().toString() : "-");
            table.addCell(record.getPlanEndDate() != null ? record.getPlanEndDate().toString() : "-");
        }

        document.add(table);
        document.close();

        return out.toByteArray();
    }

    public void sendReportEmail(byte[] file, String type) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo("ankurverma7619@gmail.com");
        helper.setSubject("Insurance Report Export");
        helper.setText("Please find attached the exported report.");

        String filename = "Insurance_Report." + (type.equals("excel") ? "xlsx" : "pdf");
        helper.addAttachment(filename, new ByteArrayResource(file));

        mailSender.send(message);
    }
}
