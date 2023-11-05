package luckystore.datn.rest;

import luckystore.datn.service.ImageHubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/admin/rest/images")
public class RestImageHubController {

    @Autowired
    private ImageHubService imageHubService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("files") MultipartFile[] files) throws IOException {
        return imageHubService.upload(files);
    }

    @PostMapping
    public ResponseEntity<?> getFileNames(@RequestBody String[] fileNames) {
        System.out.println("fo;ds;fldkf;" + fileNames.length);
//        return ResponseEntity.ok(imageHubService.getImage(fileNames));
        return null;
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteFile(@PathVariable(value = "name") String name) {
        return ResponseEntity.ok(imageHubService.deleteFile(name));
    }

}
