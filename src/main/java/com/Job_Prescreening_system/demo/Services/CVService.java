package com.Job_Prescreening_system.demo.Services;

import com.Job_Prescreening_system.demo.Entities.CV;
import com.Job_Prescreening_system.demo.Entities.User;
import com.Job_Prescreening_system.demo.Repositories.CVRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class CVService {

    @Autowired
    private CVRepository cvRepository;

    @Autowired
    private MinioService minioService;

    public List<CV> getCVsByUser(User user) {
        return cvRepository.findByUser(user);
    }

    public CV uploadCV(User user, MultipartFile file) throws Exception {
        // Upload file to MinIO
        String fileUrl = minioService.uploadFile(file);

        // Save CV metadata in the database
        CV cv = new CV();
        cv.setUser(user);
        cv.setFileName(file.getOriginalFilename());
        cv.setFileUrl(fileUrl);
        return cvRepository.save(cv);
    }

    public void deleteCV(Long cvId, User user) throws Exception {
        CV cv = cvRepository.findById(cvId)
                .orElseThrow(() -> new RuntimeException("CV not found"));

        if (!cv.getUser().equals(user)) {
            throw new RuntimeException("You do not have permission to delete this CV");
        }

        // Delete file from MinIO
        String fileName = cv.getFileUrl().substring(cv.getFileUrl().lastIndexOf("/") + 1);
        minioService.deleteFile(fileName);

        // Delete CV metadata from the database
        cvRepository.delete(cv);
    }

    public CV getCVById(Long cvId, User user) {
        // Fetch the CV by ID from the repository
        CV cv = cvRepository.findById(cvId)
                .orElseThrow(() -> new RuntimeException("CV not found"));

        // Check if the CV belongs to the user
        if (!cv.getUser().equals(user)) {
            throw new RuntimeException("You do not have permission to access this CV");
        }

        return cv;
    }
}
