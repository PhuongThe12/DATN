package luckystore.datn.rest;

import jakarta.validation.Valid;
import luckystore.datn.model.request.DotGiamGiaRequest;
import luckystore.datn.model.request.MauSacRequest;
import luckystore.datn.service.DotGiamGiaService;
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
@RequestMapping("/admin/rest/dot-giam-gia")
public class RestDotGiamGiaController {

    @Autowired
    DotGiamGiaService dotGiamGiaService;

    @PostMapping
    public ResponseEntity addDotGiamGia(@Valid @RequestBody DotGiamGiaRequest dotGiamGiaRequest, BindingResult result) {
        System.out.println(dotGiamGiaRequest.toString());
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return new ResponseEntity(dotGiamGiaService.addDotGiamGia(dotGiamGiaRequest), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getDotGiamGia(@PathVariable("id") Long id) {
        return new ResponseEntity(dotGiamGiaService.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity getDotGiamGiaPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                          @RequestParam(value = "search", required = false) String searchText,
                                          @RequestParam(value = "status", required = false) Integer status) {
        return new ResponseEntity(dotGiamGiaService.getPage(page, searchText, status), HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity getAll() {
        return new ResponseEntity(dotGiamGiaService.getAll(), HttpStatus.OK);
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
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDotGiamGia(@PathVariable("id") Long id, @Valid @RequestBody DotGiamGiaRequest dotGiamGiaRequest, BindingResult result) {
        ResponseEntity<?> errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return new ResponseEntity<>(dotGiamGiaService.updateDotGiamGia(id, dotGiamGiaRequest), HttpStatus.OK);
    }

}
