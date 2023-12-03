package luckystore.datn.rest.admin;

import luckystore.datn.entity.HoaDonChiTiet;
import luckystore.datn.model.response.YeuCauChiTietResponse;
import luckystore.datn.repository.HoaDonChiTietRepository;
import luckystore.datn.repository.YeuCauChiTietRepository;
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
@RequestMapping("/rest/admin/yeu-cau-chi-tiet")
public class RestYeuCauChiTietController {


    private final YeuCauChiTietRepository yeuCauChiTietRepository;
    @Autowired
    public RestYeuCauChiTietController(HoaDonChiTietRepository hoaDonChiTietRepository, YeuCauChiTietRepository yeuCauChiTietRepository) {
        this.yeuCauChiTietRepository = yeuCauChiTietRepository;
    }


    @GetMapping("/list/{id}")
    public List<YeuCauChiTietResponse> getHoaDonChiTiet(@PathVariable Long id) {
        return yeuCauChiTietRepository.getPageResponse(id);
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
