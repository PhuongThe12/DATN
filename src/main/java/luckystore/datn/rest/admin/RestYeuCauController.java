package luckystore.datn.rest.admin;

import jakarta.validation.Valid;
import luckystore.datn.model.request.YeuCauRequest;
import luckystore.datn.service.YeuCauService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/rest/yeu-cau")
public class RestYeuCauController {

    @Autowired
    private YeuCauService yeuCauService;

    @GetMapping("/get-all")
//    @PreAuthorize('hasAuthor(ABC)')
    public ResponseEntity<?> getAll() {
        return new ResponseEntity(yeuCauService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity addYeuCau(@Valid @RequestBody YeuCauRequest yeuCauRequest, BindingResult result) {
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;
        return new ResponseEntity(yeuCauService.addYeuCau(yeuCauRequest), HttpStatus.OK);
    }

    @PutMapping("/confirm")
    public ResponseEntity confirmYeuCau(@Valid @RequestBody YeuCauRequest yeuCauRequest, BindingResult result) {
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;
        return new ResponseEntity(yeuCauService.confirmYeuCau(yeuCauRequest), HttpStatus.OK);
    }

    @PutMapping("/change")
    public ResponseEntity updateYeuCau(@Valid @RequestBody YeuCauRequest yeuCauRequest, BindingResult result) {
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;
        return new ResponseEntity(yeuCauService.updateYeuCau(yeuCauRequest), HttpStatus.OK);
    }

    @GetMapping("/find-by-status")
    public ResponseEntity findByStatus() {
        return new ResponseEntity(yeuCauService.findByStatus(), HttpStatus.OK);
    }

    @GetMapping(("/{id}"))
    public ResponseEntity getOne(@PathVariable Long id) {
        return new ResponseEntity(yeuCauService.findById(id), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity getYeuCauPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "ngayBatDau", required = false) LocalDateTime ngayBatDau,
            @RequestParam(value = "ngayKetThuc", required = false) LocalDateTime ngayKetThuc,
            @RequestParam(value = "searchText", required = false) Long searchText,
            @RequestParam(value = "trangThai", required = false) Integer trangThai) {

        // Điều chỉnh ngày kết thúc về cuối ngày nếu cần
        if (ngayBatDau != null) {
            ngayBatDau = adjustToStartOfDay(ngayBatDau);
        }
        if (ngayKetThuc != null) {
            ngayKetThuc = adjustToEndOfDay(ngayKetThuc);
        }

        System.out.println("Ngày bắt đầu: " + ngayBatDau);
        System.out.println("Ngày kết thúc: " + ngayKetThuc);

        // Gọi service và trả về response
        return new ResponseEntity(yeuCauService.getPage(page, searchText, ngayBatDau, ngayKetThuc, trangThai), HttpStatus.OK);
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
    public static LocalDateTime adjustToStartOfDay(LocalDateTime dateTime) {
        // Chuyển về đầu ngày
        return dateTime.toLocalDate().atStartOfDay();
    }

    public static LocalDateTime adjustToEndOfDay(LocalDateTime dateTime) {
        // Chuyển về cuối ngày
        return LocalDateTime.of(dateTime.toLocalDate(), LocalTime.MAX);
    }
}

