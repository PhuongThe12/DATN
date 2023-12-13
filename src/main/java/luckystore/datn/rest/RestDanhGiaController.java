package luckystore.datn.rest;

import jakarta.validation.Valid;
import luckystore.datn.model.request.DanhGiaRequest;
import luckystore.datn.model.request.MauSacRequest;
import luckystore.datn.service.DanhGiaService;
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
@RequestMapping("/rest/admin/danh-gia")
public class RestDanhGiaController {

    @Autowired
    DanhGiaService danhGiaService;

    @GetMapping
    public ResponseEntity<?> getMauSacPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                           @RequestParam(value = "star", required = false) Integer star) {
        return new ResponseEntity<>(danhGiaService.getPage(page, star), HttpStatus.OK);
    }

    @DeleteMapping("/delete-danh-gia/{id}")
    public void deleteDanhGia(@PathVariable("id") Long id) {
        danhGiaService.deleteDanhGia(id);
    }

    @PostMapping
    public ResponseEntity<?> addDanhGia(@Valid @RequestBody DanhGiaRequest danhGiaRequest, BindingResult result) {
        ResponseEntity<?> errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;
        return new ResponseEntity<>(danhGiaService.addDanhGia(danhGiaRequest), HttpStatus.OK);
    }

    private ResponseEntity<?> getErrorJson(BindingResult result) {
        if (result.hasErrors()) {
            List<String> fieldErrors = new ArrayList<>();
            for (FieldError fieldError : result.getFieldErrors()) {
                String errorMessage = JsonString.errorToJsonObject(fieldError.getField(), fieldError.getDefaultMessage());
                fieldErrors.add(errorMessage);
            }
            String errorJson = JsonString.stringToJson(String.join(",", fieldErrors));
            return new ResponseEntity<>(errorJson, HttpStatus.BAD_REQUEST);
        }
        return null;
    }


}
