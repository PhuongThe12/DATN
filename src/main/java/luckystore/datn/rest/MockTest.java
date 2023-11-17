package luckystore.datn.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/mock/")
public class MockTest {
    @GetMapping("/abc")
    public ResponseEntity<?> mock() {
        try {
            File file = ResourceUtils.getFile("classpath:test.json");
            // Sử dụng ObjectMapper của Jackson để đọc JSON từ tệp
            ObjectMapper objectMapper = new ObjectMapper();
            Object json = objectMapper.readValue(file, Object.class);
            // Trả về JSON dưới dạng chuỗi trong ResponseEntity
            return ResponseEntity.ok(objectMapper.writeValueAsString(json));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Lỗi khi đọc tệp JSON: " + e.getMessage());
        }
    }

}
