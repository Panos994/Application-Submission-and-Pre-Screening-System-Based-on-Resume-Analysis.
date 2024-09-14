package com.Job_Prescreening_system.demo.Services;

import com.Job_Prescreening_system.demo.Entities.Job;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
public class ExcelExportService {

    public void exportJobListToExcel(List<Job> jobs, HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Job List");

        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Title", "Description", "Required Skills", "Experience", "Education Level", "Highest Score", "Top CV Files"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(createHeaderCellStyle(workbook));
        }

        // Fill data rows
        int rowIndex = 1;
        for (Job job : jobs) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(job.getTitle());
            row.createCell(1).setCellValue(job.getDescription());
            row.createCell(2).setCellValue(job.getRequiredSkills());
            row.createCell(3).setCellValue(job.getMinExperience());
            row.createCell(4).setCellValue(job.getEducationLevel());
            row.createCell(5).setCellValue(job.getTopMatchScore() != null ? job.getTopMatchScore().doubleValue() : 0.0);

            // This line has been updated to include all top CV file names
            row.createCell(6).setCellValue(job.getTopCvFileNames() != null ? job.getTopCvFileNames() : "N/A");
        }

        // Set response headers and write Excel file to output stream
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=job_list.xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    private CellStyle createHeaderCellStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }
}
