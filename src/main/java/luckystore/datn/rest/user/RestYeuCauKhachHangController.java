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
@RequestMapping("/user/rest/yeu-cau-khach-hang")
public class RestYeuCauKhachHangController {
    private final YeuCauKhachHangService yeuCauKhachHangService;

    @Autowired
    public RestYeuCauKhachHangController(YeuCauKhachHangService yeuCauKhachHangService) {
        this.yeuCauKhachHangService = yeuCauKhachHangService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getChatLieu(@PathVariable("id") Long id) {
        return new ResponseEntity<>(yeuCauKhachHangService.getOneHoaDonYeuCauRespone(id), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity addYeuCau(@Valid @RequestBody YeuCauRequest yeuCauRequest, BindingResult result) {
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;
        return new ResponseEntity(yeuCauKhachHangService.addYeuCau(yeuCauRequest), HttpStatus.OK);
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
