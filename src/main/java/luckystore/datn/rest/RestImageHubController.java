package luckystore.datn.rest;

import luckystore.datn.service.ImageHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/admin/rest/images")
public class RestImageHubController {

    @Autowired
    private ImageHubService imageHubService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("files") MultipartFile[] files) throws IOException {
        for (MultipartFile file : files) {
            System.out.println(file);
        }
        return imageHubService.upload(files);
    }

    @GetMapping
    public ResponseEntity<?> getFileNames() {
        return ResponseEntity.ok(imageHubService.getFileNames());
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteFile(@PathVariable(value = "name") String name) {
        return ResponseEntity.ok(imageHubService.deleteFile(name));
    }

}
