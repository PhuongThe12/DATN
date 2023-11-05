package luckystore.datn.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public interface ImageHubService {

    ResponseEntity<?> upload(MultipartFile[] files) throws IOException;

    boolean isImage(InputStream fileInputStream);

    ResponseEntity<?> getImage(String[] fileNames);

    String deleteFile(String name);

    String getImage(String filename);
}
