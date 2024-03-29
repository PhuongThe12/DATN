package luckystore.datn.rest;

import jakarta.validation.Valid;
import luckystore.datn.model.request.KhuyenMaiRequest;
import luckystore.datn.model.request.KhuyenMaiSearch;
import luckystore.datn.model.response.ChiTietKhuyenMaiResponse;
import luckystore.datn.service.KhuyenMaiService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rest/admin/khuyen-mai")
public class RestKhuyenMaiController {

    @Autowired
    KhuyenMaiService khuyenMaiService;

    @GetMapping("/get-all")
    public ResponseEntity getAll() {
        return new ResponseEntity(khuyenMaiService.getAll(), HttpStatus.OK);
    }

    private ResponseEntity getErrorJson(BindingResult result) {
        if (result.hasErrors()) {
            List<String> fieldErrors = new ArrayList<>();
            for (FieldError fieldError : result.getFieldErrors()) {
                String errorMessage = JsonString.errorToJsonObject(fieldError.getField(), fieldError.getDefaultMessage());
                fieldErrors.add(errorMessage);
            }
            String errorJson = JsonString.stringToJson(String.join(",", fieldErrors));
            return new ResponseEntity(errorJson, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @PostMapping
    public ResponseEntity addKhuyenMai(@Valid @RequestBody KhuyenMaiRequest khuyenMaiRequest, BindingResult result) {
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return new ResponseEntity(khuyenMaiService.addKhuyenMai(khuyenMaiRequest), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getDot(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                 @RequestParam(value = "search", required = false) String searchText,
                                 @RequestParam(value = "status", required = false) Integer status) {
        return new ResponseEntity(khuyenMaiService.getPage(page, searchText, status), HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchingKhuyenMai(@RequestBody KhuyenMaiSearch kmSearch) {
        return ResponseEntity.ok(khuyenMaiService.searchingKhuyenMai(kmSearch));
    }

    @PutMapping("/hien-thi/{id}")
    public ResponseEntity<?> hienThiKhuyenMai(@PathVariable("id")Long id) {
        return ResponseEntity.ok(khuyenMaiService.hienThiKhuyenMai(id));
    }

 @PutMapping("/an/{id}")
    public ResponseEntity<?> anKhuyenMai(@PathVariable("id")Long id) {
        return ResponseEntity.ok(khuyenMaiService.anKhuyenMai(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity getKhuyenMai(@PathVariable("id") Long id) {
        return new ResponseEntity(khuyenMaiService.findById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateKhuyenMai(@PathVariable("id") Long id, @Valid @RequestBody KhuyenMaiRequest khuyenMaiRequest, BindingResult result) {
        ResponseEntity<?> errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;
        return new ResponseEntity<>(khuyenMaiService.updateKhuyenMai(id, khuyenMaiRequest), HttpStatus.OK);
    }

    @GetMapping("/giay/{id}")
    public ResponseEntity getKhuyenMaiGiay(@PathVariable("id") Long id) {
        return new ResponseEntity(khuyenMaiService.getKhuyenMaiById(id), HttpStatus.OK);
    }


}
