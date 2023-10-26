package luckystore.datn.rest;

import luckystore.datn.service.ImageHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/images")
public class RestImageHubController {

    @Autowired
    private ImageHubService imageHubService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("files") List<MultipartFile> files) throws IOException {
        return imageHubService.upload(files);
    }

    @PostMapping("/upload2")
    public String uploadFile2(@RequestParam("file") MultipartFile file) {
        return imageHubService.upload2(file);
    }
    @GetMapping
    public String[] getFileNames() {
        return imageHubService.getFileNames();
    }

}
