package luckystore.datn.rest;

import jakarta.validation.Valid;
import luckystore.datn.common.PageableRequest;
import luckystore.datn.model.request.PhieuGiamGiaRequest;
import luckystore.datn.service.PhieuGiamGiaService;
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
@RequestMapping("/admin/rest/phieu-giam-gia")
public class RestPhieuGiamGiaController {

    @Autowired
    private PhieuGiamGiaService phieuGiamGiaService;

    @GetMapping
    public ResponseEntity getAll() {

        return new ResponseEntity(phieuGiamGiaService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity getPage(@RequestBody PageableRequest request) {

        return new ResponseEntity(phieuGiamGiaService.getPagePhieuGiamGia(request), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getPhieuGiamGia(@PathVariable("id") Long id) {

        return new ResponseEntity(phieuGiamGiaService.findPhieuGiamGiaById(id), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity addPhieuGiamGia(@Valid @RequestBody PhieuGiamGiaRequest phieuGiamGiaRequest, BindingResult result) {

        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;
        return new ResponseEntity(phieuGiamGiaService.addPhieuGiamGia(phieuGiamGiaRequest), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity updatePhieuGiamGia(@PathVariable("id") Long id, @Valid @RequestBody PhieuGiamGiaRequest phieuGiamGiaRequest,
                                             BindingResult result) {
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return new ResponseEntity(phieuGiamGiaService.updatePhieuGiamGia(id, phieuGiamGiaRequest), HttpStatus.OK);
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
