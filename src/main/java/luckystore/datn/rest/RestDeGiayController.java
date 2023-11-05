package luckystore.datn.rest;


import jakarta.validation.Valid;
import luckystore.datn.model.request.DeGiayRequest;
import luckystore.datn.service.DeGiayService;
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
@RequestMapping("/admin/rest/de-giay")
public class RestDeGiayController {

    @Autowired
    private DeGiayService deGiayService;

    @PostMapping
    public ResponseEntity<?> addDeGiay(@Valid @RequestBody DeGiayRequest deGiayRequest, BindingResult result) {
        ResponseEntity<?> errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return new ResponseEntity<>(deGiayService.addDeGiay(deGiayRequest), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateDeGiay(@PathVariable("id") Long id, @Valid @RequestBody DeGiayRequest deGiayRequest, BindingResult result) {
        ResponseEntity<?> errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return new ResponseEntity<>(deGiayService.updateDeGiay(id, deGiayRequest), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDeGiay(@PathVariable("id") Long id) {
        return new ResponseEntity<>(deGiayService.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getDeGiayPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                        @RequestParam(value = "search", required = false) String searchText,
                                        @RequestParam(value = "status", required = false) Integer status) {
        return new ResponseEntity<>(deGiayService.getPage(page, searchText, status), HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(deGiayService.getAll(), HttpStatus.OK);
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