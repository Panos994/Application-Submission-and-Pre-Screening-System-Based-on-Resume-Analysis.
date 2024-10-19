
package com.Job_Prescreening_system.demo.Services;


import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ResumeParsingService {

    //private final Tika tika = new Tika();

    public String parseResume(MultipartFile resumeFile) throws IOException {
        try (PDDocument document = PDDocument.load(resumeFile.getInputStream())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }



}
