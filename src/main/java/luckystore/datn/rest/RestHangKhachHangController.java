package luckystore.datn.rest;

import jakarta.validation.Valid;
import luckystore.datn.model.request.HangKhachHangRequest;
import luckystore.datn.service.HangKhachHangService;
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
@RequestMapping("/admin/rest/hang-khach-hang")
public class RestHangKhachHangController {
    @Autowired
    private HangKhachHangService hangKhachHangService;

    @GetMapping("/get-all")
    public ResponseEntity getAll() {

        return new ResponseEntity(hangKhachHangService.getAll(), HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity getHangKhachHangPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                        @RequestParam(value = "search", required = false) String searchText,
                                        @RequestParam(value = "status", required = false) Integer status) {
        return new ResponseEntity(hangKhachHangService.getPage(page, searchText, status), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity addHangKhachHang(@Valid @RequestBody HangKhachHangRequest hangKhachHangRequest, BindingResult result) {
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return new ResponseEntity(hangKhachHangService.addHangKhachHang(hangKhachHangRequest), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateHangKhachHang(@PathVariable("id") Long id, @Valid @RequestBody HangKhachHangRequest hangKhachHangRequest, BindingResult result) {
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return new ResponseEntity(hangKhachHangService.updateHangKhachHang(id, hangKhachHangRequest), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getHangKhachHang(@PathVariable("id") Long id) {
        return new ResponseEntity(hangKhachHangService.findById(id), HttpStatus.OK);
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
