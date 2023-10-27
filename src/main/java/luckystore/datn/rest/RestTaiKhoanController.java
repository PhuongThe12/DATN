package luckystore.datn.rest;

import jakarta.validation.Valid;
import luckystore.datn.model.request.TaiKhoanRequest;
import luckystore.datn.service.impl.TaiKhoanServiceImpl;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/tai-khoan")
public class RestTaiKhoanController {

    @Autowired
    TaiKhoanServiceImpl taiKhoanService;

    @GetMapping("/detail")
    public ResponseEntity<?> detail(@Valid @RequestBody TaiKhoanRequest taiKhoanRequest,
                                BindingResult bindingResult){
        ResponseEntity errorJson = getErrorJson(bindingResult);
        if (errorJson != null) return errorJson;

        return new ResponseEntity(taiKhoanService.login(taiKhoanRequest), HttpStatus.OK);
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
