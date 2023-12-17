package luckystore.datn.rest.admin;

import jakarta.validation.Valid;
import luckystore.datn.model.request.YeuCauRequest;
import luckystore.datn.model.request.YeuCauSearch;
import luckystore.datn.service.YeuCauService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RestController
@RequestMapping("/rest/admin/yeu-cau")
public class RestYeuCauController {


    private final YeuCauService yeuCauService;

    @Autowired
    public RestYeuCauController(YeuCauService yeuCauService) {
        this.yeuCauService = yeuCauService;
    }


    @GetMapping("/get-all")
//    @PreAuthorize('hasAuthor(ABC)')
    public ResponseEntity<?> getAll() {
        return new ResponseEntity(yeuCauService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/hoa-don/{id}")
    public ResponseEntity<?> getYeuCau(@PathVariable("id") Long id) {
        return new ResponseEntity<>(yeuCauService.getOneHoaDonYeuCauRespone(id), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity addYeuCau(@Valid @RequestBody YeuCauRequest yeuCauRequest, BindingResult result) {
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;
        return new ResponseEntity(yeuCauService.addYeuCau(yeuCauRequest), HttpStatus.OK);
    }

    @PostMapping("/tra-hang-nhanh")
    public ResponseEntity traHangNhanh(@Valid @RequestBody YeuCauRequest yeuCauRequest, BindingResult result) {
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;
        yeuCauService.traHangNhanh(yeuCauRequest);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/confirm")
    public ResponseEntity confirmYeuCau(@Valid @RequestBody YeuCauRequest yeuCauRequest, BindingResult result) {
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;
        return new ResponseEntity(yeuCauService.confirmYeuCau(yeuCauRequest), HttpStatus.OK);
    }
    @PutMapping("/unconfirm")
    public ResponseEntity unConfirmYeuCau(@Valid @RequestBody YeuCauRequest yeuCauRequest, BindingResult result) {
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;
        return new ResponseEntity(yeuCauService.unConfirmYeuCau(yeuCauRequest), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity updateYeuCau(@Valid @RequestBody YeuCauRequest yeuCauRequest, BindingResult result) {
        return new ResponseEntity(yeuCauService.updateYeuCau(yeuCauRequest), HttpStatus.OK);
    }

    @GetMapping("/find-by-status")
    public ResponseEntity findByStatus(){
        return new ResponseEntity(yeuCauService.findByStatus(), HttpStatus.OK);
    }

    @GetMapping(("/{id}"))
    public ResponseEntity getOne(@PathVariable Long id) {
        return new ResponseEntity(yeuCauService.findById(id), HttpStatus.OK);
    }


    @PostMapping()
    public ResponseEntity getYeuCauPage(@RequestBody YeuCauSearch yeuCauSearch){
        return new ResponseEntity(yeuCauService.getPage(yeuCauSearch), HttpStatus.OK);
    }

    private ResponseEntity getErrorJson(BindingResult result) {
        if (result.hasErrors()) {
            List<String> fieldErrors = new ArrayList<>();
            for (FieldError fieldError : result.getFieldErrors()) {
                String errorMessage = JsonString.errorToJsonObject(fieldError.getField(), fieldError.getDefaultMessage());
            }
            String errorJson = JsonString.stringToJson(String.join(",", fieldErrors));
            return new ResponseEntity(errorJson, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

}

