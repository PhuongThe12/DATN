package luckystore.datn.service.impl;

import luckystore.datn.exception.FileException;
import luckystore.datn.repository.BienTheGiayRepository;
import luckystore.datn.repository.GiayRepository;
import luckystore.datn.service.ImageHubService;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.ServiceLoader;

@Service
public class ImageHubServiceImpl implements ImageHubService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Autowired
    private GiayRepository giayRepository;

    @Autowired
    private BienTheGiayRepository bienTheGiayRepository;

    @Override
    public ResponseEntity<?> upload(MultipartFile[] files) {
        List<String> result = new ArrayList<>();
        for (MultipartFile file : files) {

            try (InputStream inputStream = file.getInputStream()) {
                if (file.getSize() > 5 * 1024 * 1024) {
                    throw new FileException("Không đúng định dạng");
                }
                if (isImage(file.getInputStream())) {
                    Path uploadPath = Paths.get(uploadDir);
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }
                    String fileName = System.currentTimeMillis() + file.getOriginalFilename();
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                    result.add(fileName);
                } else {
                    throw new FileException("Không đúng định dạng");
                }
            } catch (IOException e) {
                throw new FileException("Lỗi truy cập dường dẫn ảnh");
            }
        }
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public boolean isImage(InputStream fileInputStream) {
        try {
            Tika tika = new Tika();
            String mimeType = tika.detect(fileInputStream);
            return mimeType.startsWith("image/");
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public ResponseEntity<?> getImage(String[] fileNames) {
        List<String> result = new ArrayList<>();
        for (String filename : fileNames) {
            try {
                Path imagePath = Paths.get(uploadDir, filename);
                Resource resource = new UrlResource(imagePath.toUri());

                if (resource.exists() && resource.isReadable()) {
                    byte[] imageBytes = Files.readAllBytes(imagePath);
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    result.add("data:image/jpeg;base64," + base64Image);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public String getImage(String filename) {
        try {
            Path imagePath = Paths.get(uploadDir, filename);
            Resource resource = new UrlResource(imagePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                byte[] imageBytes = Files.readAllBytes(imagePath);
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                return "data:image/jpeg;base64," + base64Image;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String deleteFile(String name) {

        if (giayRepository.checkImageUsed(name) || bienTheGiayRepository.existsByHinhAnh(name)) {
            throw new FileException("Ảnh đã được sử dụng. Không thể xóa");
        }
        Path path = Paths.get(uploadDir + "/" + name);
        try {
            Files.delete(path);
            return "Tệp đã được xóa thành công.";
        } catch (IOException e) {
            throw new FileException("Không thể xóa, lỗi truy cập");
        }
    }

}
