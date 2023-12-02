package luckystore.datn.rest;

import jakarta.validation.Valid;
import luckystore.datn.model.request.YeuCauRequest;
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
    public ResponseEntity findByStatus(){
        return new ResponseEntity(yeuCauService.findByStatus(), HttpStatus.OK);
    }

    @GetMapping(("/{id}"))
    public ResponseEntity getOne(@PathVariable Long id) {
        return new ResponseEntity(yeuCauService.findById(id), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity getYeuCauPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "ngayBatDau", required = false) String ngayBatDauStr,
            @RequestParam(value = "ngayKetThuc", required = false) String ngayKetThucStr,
            @RequestParam(value = "searchText", required = false) Long searchText,
            @RequestParam(value = "trangThai", required = false) Integer trangThai) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date ngayBatDau = null;
        java.sql.Date ngayKetThuc = null;
        try {
            if (ngayBatDauStr != null) {
                java.util.Date ngayBatDauUtil = sdf.parse(ngayBatDauStr);
                ngayBatDau = new java.sql.Date(ngayBatDauUtil.getTime());
            }

            if (ngayKetThucStr != null) {
                java.util.Date ngayKetThucUtil = sdf.parse(ngayKetThucStr);

                // Chuyển ngày kết thúc thành cuối ngày
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(ngayKetThucUtil);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                ngayKetThuc = new java.sql.Date(calendar.getTime().getTime());
            }
            System.out.println(ngayBatDau);
            System.out.println(ngayKetThuc);
            return new ResponseEntity(yeuCauService.getPage(page,searchText,ngayBatDau,ngayKetThuc,trangThai), HttpStatus.OK);
        } catch (ParseException e) {
            // Xử lý lỗi nếu ngày không đúng định dạng
            return new ResponseEntity("Ngày không đúng định dạng (yyyy-MM-dd)", HttpStatus.BAD_REQUEST);
        }
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

