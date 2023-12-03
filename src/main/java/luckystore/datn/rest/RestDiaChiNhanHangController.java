package luckystore.datn.rest;

import jakarta.validation.Valid;
import luckystore.datn.model.request.DiaChiNhanHangRequest;
import luckystore.datn.model.request.KhachHangRequest;
import luckystore.datn.model.response.DiaChiNhanHangResponse;
import luckystore.datn.service.DiaChiNhanHangService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/rest/admin/dia-chi-nhan-hang")
public class RestDiaChiNhanHangController {
    @Autowired
    private DiaChiNhanHangService diaChiNhanHangService;

    @GetMapping
    public ResponseEntity getDiaChiNhanHangPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                               @RequestParam(value = "search", required = false) String searchText,
                                               @RequestParam(value = "status", required = false) Integer status) {
        return new ResponseEntity(diaChiNhanHangService.getPage(page, searchText, status), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity addDiaChiNhanHang( @RequestBody DiaChiNhanHangRequest diaChiNhanHangRequest, BindingResult result) {
        System.out.println(diaChiNhanHangRequest.toString());
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return new ResponseEntity(diaChiNhanHangService.addDiaChiNhanHang(diaChiNhanHangRequest), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateDiaChiNhanHang(@PathVariable("id") Long id, @Valid @RequestBody DiaChiNhanHangRequest diaChiNhanHangRequest, BindingResult result) {
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return new ResponseEntity(diaChiNhanHangService.updateDiaChiNhanHang(id, diaChiNhanHangRequest), HttpStatus.OK);
    }

    @PutMapping("/update-trang-thai/{id}")
    public DiaChiNhanHangResponse updateDiaChiNhanHang(@PathVariable Long id,
                                                       @RequestBody DiaChiNhanHangRequest diaChiNhanHangRequest) {
        return diaChiNhanHangService.updateTrangThaiDiaChiNhan(id, diaChiNhanHangRequest);
    }

    @GetMapping("/{id}")
    public ResponseEntity getDiaChiNhanHang(@PathVariable("id") Long id) {
        return new ResponseEntity(diaChiNhanHangService.findById(id), HttpStatus.OK);
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
    @DeleteMapping("/delete/{id}")
    public void deleteDieuKien(@PathVariable("id") Long id) {
        diaChiNhanHangService.deleteDieuKien(id);
    }
}
