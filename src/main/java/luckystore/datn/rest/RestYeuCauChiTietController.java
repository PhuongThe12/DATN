package luckystore.datn.rest;

import luckystore.datn.entity.HoaDonChiTiet;
import luckystore.datn.model.request.YeuCauChiTietRequest;
import luckystore.datn.repository.HoaDonChiTietRepository;
import luckystore.datn.service.YeuCauChiTietService;
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
@RequestMapping("/admin/rest/yeu-cau-chi-tiet")
public class RestYeuCauChiTietController {

    @Autowired
    private YeuCauChiTietService yeuCauChiTietService;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;
    @GetMapping("/Hoa-Don-Chi-Tiet/{id}")
    public HoaDonChiTiet getHoaDonChiTiet(@PathVariable Long id) {
        System.out.println(hoaDonChiTietRepository.findById(id).get().toString());
        return hoaDonChiTietRepository.findById(id).get();
    }

    @PostMapping("/add")
    public ResponseEntity addYeuCauChiTiet(@RequestBody List<YeuCauChiTietRequest> yeuCauChiTietRequest){
        System.out.println(yeuCauChiTietRequest);

//        return new ResponseEntity(yeuCauChiTietService.addYeuCauChiTiet(yeuCauChiTietRequest),HttpStatus.OK);
        return null;
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
