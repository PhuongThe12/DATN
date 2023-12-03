package luckystore.datn.rest;

import jakarta.validation.Valid;
import luckystore.datn.model.request.CoGiayRequest;
import luckystore.datn.service.CoGiayService;
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
@RequestMapping("/rest/adminco-giay")
public class RestCoGiayController {

    @Autowired
    private CoGiayService coGiayService;

    @PostMapping
    public ResponseEntity<?> addCoGiay(@Valid @RequestBody CoGiayRequest coGiayRequest, BindingResult result) {
        ResponseEntity<?> errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return new ResponseEntity<>(coGiayService.addCoGiay(coGiayRequest), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCoGiay(@PathVariable("id") Long id, @Valid @RequestBody CoGiayRequest coGiayRequest, BindingResult result) {
        ResponseEntity<?> errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return new ResponseEntity<>(coGiayService.updateCoGiay(id, coGiayRequest), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCoGiay(@PathVariable("id") Long id) {
        return new ResponseEntity<>(coGiayService.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getCoGiayPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                        @RequestParam(value = "search", required = false) String searchText,
                                        @RequestParam(value = "status", required = false) Integer status) {
        return new ResponseEntity<>(coGiayService.getPage(page, searchText, status), HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(coGiayService.getAll(), HttpStatus.OK);
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
