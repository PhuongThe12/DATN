package luckystore.datn.rest.user;

import jakarta.validation.Valid;
import luckystore.datn.model.request.YeuCauRequest;
import luckystore.datn.service.user.YeuCauKhachHangService;
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
@RequestMapping("/rest/user/yeu-cau")
public class RestYeuCauKhachHangController {
    private final YeuCauKhachHangService yeuCauKhachHangService;

    @Autowired
    public RestYeuCauKhachHangController(YeuCauKhachHangService yeuCauKhachHangService) {
        this.yeuCauKhachHangService = yeuCauKhachHangService;
    }


    @GetMapping("/hoa-don/{id}")
    public ResponseEntity<?> getHoaDonYeuCau(@PathVariable("id") Long id) {
        return new ResponseEntity<>(yeuCauKhachHangService.getOneHoaDonYeuCauRespone(id), HttpStatus.OK);
    }

    @GetMapping("/list/{id}")
    public ResponseEntity<?> getListYeuCauKhachHang(@PathVariable("id") Long idHoaDon) {
        return new ResponseEntity<>(yeuCauKhachHangService.getListYeuCau(idHoaDon), HttpStatus.OK);
    }
    @PostMapping("/add")
    public ResponseEntity addYeuCau(@Valid @RequestBody YeuCauRequest yeuCauRequest, BindingResult result) {
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;
        return new ResponseEntity(yeuCauKhachHangService.addYeuCau(yeuCauRequest), HttpStatus.OK);
    }
    @PutMapping("/update")
    public ResponseEntity updateYeuCau(@Valid @RequestBody YeuCauRequest yeuCauRequest, BindingResult result) {
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;
        return new ResponseEntity(yeuCauKhachHangService.updateYeuCau(yeuCauRequest), HttpStatus.OK);
    }
    @PutMapping("/cancel")
    public ResponseEntity cancelYeuCau(@Valid @RequestBody YeuCauRequest yeuCauRequest, BindingResult result) {
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;
        return new ResponseEntity(yeuCauKhachHangService.cancelYeuCau(yeuCauRequest), HttpStatus.OK);
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
