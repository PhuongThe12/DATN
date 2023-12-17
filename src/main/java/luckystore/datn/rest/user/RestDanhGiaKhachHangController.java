package luckystore.datn.rest.user;

import jakarta.validation.Valid;
import luckystore.datn.model.request.DanhGiaRequest;
import luckystore.datn.service.DanhGiaService;
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
@RequestMapping("/rest/user/danh-gia")
public class RestDanhGiaKhachHangController {

    @Autowired
    DanhGiaService danhGiaService;

    @PostMapping
    public ResponseEntity<?> addDanhGia(@Valid @RequestBody DanhGiaRequest danhGiaRequest, BindingResult result) {
        ResponseEntity<?> errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;
        return new ResponseEntity<>(danhGiaService.addDanhGia(danhGiaRequest), HttpStatus.OK);
    }

    @GetMapping("/find-by-khach-hang")
    public ResponseEntity<?> findById(@RequestParam("idKhachHang") Long idKhachHang, @RequestParam("idGiay") Long idGiay) {
        return new ResponseEntity<>(danhGiaService.findByIdKhAndIdGiay(idKhachHang, idGiay), HttpStatus.OK);
    }

    @GetMapping("/get-all-by-khach-hang/{idKhachHang}")
    public ResponseEntity<?> findAllByIdKhachHang(@PathVariable("idKhachHang") Long idKhachHang) {
        return new ResponseEntity<>(danhGiaService.getAllByIdKhachHang(idKhachHang), HttpStatus.OK);
    }

    @GetMapping("/get-danh-gia-by-id-giay/{idGiay}")
    public ResponseEntity<?> getDanhGiaByIdGiay(@PathVariable("idGiay") Long idGiay){
        return ResponseEntity.ok(danhGiaService.getDanhGiaByIdGiay(idGiay));
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
