package luckystore.datn.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

@Service
public interface ImageHubService {

    ResponseEntity<?> upload(MultipartFile[] files) throws IOException;

    public boolean isImage(InputStream fileInputStream);

    String getBase64FromFile(String filename);

    public String base64ToFile(String base64Data);

    String deleteFile(String name);

    void deleteFile(Set<String> files);

    String getImage(String filename);
}
