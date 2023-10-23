package luckystore.datn.rest;

import jakarta.validation.Valid;
import luckystore.datn.model.request.NhanVienRequest;
import luckystore.datn.service.NhanVienService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/rest/nhan-vien")
public class RestNhanVienController {

    @Autowired
    private NhanVienService nhanVienService;

    @PostMapping
    public ResponseEntity addNhanVien(@Valid @RequestBody NhanVienRequest nhanVienRequest, BindingResult result) {
        System.out.println(nhanVienRequest.toString());
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return new ResponseEntity(nhanVienService.addNhanVien(nhanVienRequest), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateNhanVien(@PathVariable("id") Long id,
                                         @Valid @RequestBody NhanVienRequest nhanVienRequest,
                                         BindingResult result) {
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return new ResponseEntity(nhanVienService.updateNhanVien(id, nhanVienRequest), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getNhanVien(@PathVariable("id") Long id) {
        return new ResponseEntity(nhanVienService.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getNhanVienPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                          @RequestParam(value = "search", required = false) String searchText,
                                          @RequestParam(value = "status", required = false) Integer status,
                                          @RequestParam(value = "chucVu", required = false) Integer chucVu) {
        return new ResponseEntity(nhanVienService.getPage(page, searchText, status, chucVu), HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity getAll() {
        return new ResponseEntity(nhanVienService.getAll(), HttpStatus.OK);
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
}
