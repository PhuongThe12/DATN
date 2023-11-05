package luckystore.datn.rest;


import jakarta.validation.Valid;
import luckystore.datn.model.request.LotGiayRequest;
import luckystore.datn.service.LotGiayService;
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
@RequestMapping("/admin/rest/lot-giay")
public class RestLotGiayController {

    @Autowired
    private LotGiayService lotGiayService;

    @PostMapping
    public ResponseEntity<?> addLotGiay(@Valid @RequestBody LotGiayRequest lotGiayRequest, BindingResult result) {
        ResponseEntity<?> errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return new ResponseEntity<>(lotGiayService.addLotGiay(lotGiayRequest), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLotGiay(@PathVariable("id") Long id, @Valid @RequestBody LotGiayRequest lotGiayRequest, BindingResult result) {
        ResponseEntity<?> errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return new ResponseEntity<>(lotGiayService.updateLotGiay(id, lotGiayRequest), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLotGiay(@PathVariable("id") Long id) {
        return new ResponseEntity<>(lotGiayService.findById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getLotGiayPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                        @RequestParam(value = "search", required = false) String searchText,
                                        @RequestParam(value = "status", required = false) Integer status) {
        return new ResponseEntity<>(lotGiayService.getPage(page, searchText, status), HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(lotGiayService.getAll(), HttpStatus.OK);
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
