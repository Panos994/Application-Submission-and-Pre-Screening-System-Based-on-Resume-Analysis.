package com.Job_Prescreening_system.demo.REST;

import com.Job_Prescreening_system.demo.Entities.CV;
import com.Job_Prescreening_system.demo.Entities.User;
import com.Job_Prescreening_system.demo.Repositories.UserRepository;
import com.Job_Prescreening_system.demo.Services.CVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cvs")
@CrossOrigin(origins = "http://localhost:8081")
public class CVController {

    @Autowired
    private CVService cvService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/upload")
    public ResponseEntity<CV> uploadCV(@RequestParam("cv") MultipartFile file,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            CV uploadedCV = cvService.uploadCV(user, file);
            return ResponseEntity.ok(uploadedCV);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<Map<String, String>>> getUserCVs(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<CV> cvs = cvService.getCVsByUser(user);

            // Convert CV objects to a simplified structure for the frontend
            List<Map<String, String>> cvList = cvs.stream()
                    .map(cv -> Map.of(
                            "id", String.valueOf(cv.getId()),
                            "fileName", cv.getFileName(),
                            "fileUrl", cv.getFileUrl()
                    ))
                    .collect(Collectors.toList());

            return ResponseEntity.ok(cvList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCV(@PathVariable Long id,
                                           @AuthenticationPrincipal UserDetails userDetails) {
        try {
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            cvService.deleteCV(id, user);
            return ResponseEntity.ok("CV deleted successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting CV: " + e.getMessage());
        }
    }
}
