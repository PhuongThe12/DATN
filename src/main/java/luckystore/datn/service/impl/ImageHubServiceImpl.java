package luckystore.datn.service.impl;

import luckystore.datn.service.ImageHubService;
import luckystore.datn.util.JsonString;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class ImageHubServiceImpl implements ImageHubService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public ResponseEntity<String> upload(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            try {
                InputStream fileInputStream = file.getInputStream();
                if (isImage(fileInputStream)) {
                    Path uploadPath = Paths.get(uploadDir);
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }
                    Path filePath = uploadPath.resolve(System.currentTimeMillis() + file.getOriginalFilename());
                    Files.copy(fileInputStream, filePath, StandardCopyOption.REPLACE_EXISTING);

                } else {
                    throw new RuntimeException(JsonString.stringToJson("error: \"Không đúng định dạng\""));
                }
            } catch (IOException e) {
                throw new RuntimeException(JsonString.stringToJson("error: \"Lỗi truy cập dường dẫn ảnh\""));
            }
        }
        return new ResponseEntity<>("Tải ảnh thành công", HttpStatus.OK);
    }

    @Override
    public boolean isImage(InputStream fileInputStream) {
        try {
            Tika tika = new Tika();
            String mimeType = tika.detect(fileInputStream);
            return mimeType.startsWith("image/");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String[] getFileNames() {
        try {
            File directory = new File(uploadDir);
            String[] fileNames = directory.list();

            if (fileNames != null) {
                Arrays.sort(fileNames, Comparator.reverseOrder());
                System.out.println(fileNames.length);
                for (String fileName : fileNames) {
                    System.out.println(fileName);
                }

                return fileNames;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi truy cập");
        }
    }

    @Override
    public String upload2(MultipartFile file) {
        try {
            InputStream inputStream = file.getInputStream();
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(file.getOriginalFilename());
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return "File uploaded successfully.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to upload file.";
        }
    }
}
