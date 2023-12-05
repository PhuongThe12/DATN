package luckystore.datn.rest.admin;

import jakarta.validation.Valid;
import luckystore.datn.entity.LyDo;
import luckystore.datn.model.request.LotGiayRequest;
import luckystore.datn.model.request.LyDoRequest;
import luckystore.datn.service.LyDoService;
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
@RequestMapping("/rest/admin/ly-do")
public class RestLyDoController {

    private final LyDoService lyDoService;

    @Autowired
    public RestLyDoController(LyDoService lyDoService) {
        this.lyDoService = lyDoService;
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAll(){
        return new ResponseEntity<>(lyDoService.getAll(),HttpStatus.OK);
    }

    @PostMapping("/save")
    public ResponseEntity<?> addLyDo(@Valid @RequestBody LyDoRequest lyDoRequest, BindingResult result) {
        ResponseEntity<?> errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;
        return new ResponseEntity<>(lyDoService.insert(lyDoRequest), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateLyDo(@Valid @RequestBody LyDoRequest lyDoRequest, BindingResult result) {
        ResponseEntity<?> errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;
        return new ResponseEntity<>(lyDoService.update(lyDoRequest), HttpStatus.OK);
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
