package luckystore.datn.rest;

import jakarta.validation.Valid;
import luckystore.datn.dto.MauSacDto;
import luckystore.datn.service.MauSacService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rest/mau-sac")
public class RestMauSacController {

    @Autowired
    private MauSacService mauSacService;

    @PostMapping
    public ResponseEntity addMauSac(@Valid @RequestBody MauSacDto mauSacDto, BindingResult result) {
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return new ResponseEntity(mauSacService.addMauSac(mauSacDto), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateMauSac(@PathVariable("id") Long id, @Valid @RequestBody MauSacDto mauSacDto, BindingResult result) {
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return new ResponseEntity(mauSacService.updateMauSac(id, mauSacDto), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity getMauSac(@PathVariable("id") Long id){
        return new ResponseEntity(mauSacService.findById(id), HttpStatus.OK);
    }

    private ResponseEntity getErrorJson(BindingResult result) {
        if(result.hasErrors()) {
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
