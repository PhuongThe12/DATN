package luckystore.datn.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public interface ImageHubService {

    ResponseEntity<String> upload(MultipartFile[] files) throws IOException;

    public boolean isImage(InputStream fileInputStream);

    String[] getFileNames();

    String deleteFile(String name);
}
