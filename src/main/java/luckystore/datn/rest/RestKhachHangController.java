package luckystore.datn.rest;

import jakarta.validation.Valid;
import luckystore.datn.model.request.KhachHangRequest;
import luckystore.datn.service.KhachHangService;
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
@RequestMapping("/rest/admin/khach-hang")
public class RestKhachHangController {
    @Autowired
    private KhachHangService khachHangService;

    @GetMapping("/get-all")
    public ResponseEntity getAll() {
        return new ResponseEntity(khachHangService.getAll(), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getHangKhachHangPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                               @RequestParam(value = "search", required = false) String searchText,
                                               @RequestParam(value = "status", required = false) Integer status) {
        return new ResponseEntity(khachHangService.getPage(page, searchText, status), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity addKhachHang(@Valid @RequestBody KhachHangRequest khachHangRequest, BindingResult result) {
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return new ResponseEntity(khachHangService.addKhachHang(khachHangRequest), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateKhachHang(@PathVariable("id") Long id, @Valid @RequestBody KhachHangRequest khachHangRequest, BindingResult result) {
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return new ResponseEntity(khachHangService.updateKhachHang(id, khachHangRequest), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getKhachHang(@PathVariable("id") Long id) {
        return new ResponseEntity(khachHangService.findById(id), HttpStatus.OK);
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

    @PostMapping("/search-by-name")
    public ResponseEntity<?> searchByName(@RequestBody String searchText) {
        return new ResponseEntity<>(khachHangService.searchByName(searchText), HttpStatus.OK);
    }

    @PutMapping("/cap-nhat-mot-phan/{id}")
    public ResponseEntity<?> updateKhachHang(@PathVariable("id") Long id, @RequestBody KhachHangRequest khachHangRequest) {
        return new ResponseEntity<>(khachHangService.updateMotPhanKhachHang(id, khachHangRequest), HttpStatus.OK);
    }

}
