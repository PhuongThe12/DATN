package luckystore.datn.rest;

import jakarta.validation.Valid;
import luckystore.datn.entity.TaiKhoan;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.model.request.TaiKhoanRequest;
import luckystore.datn.model.response.TaiKhoanResponse;
import luckystore.datn.service.TaiKhoanKhachHangService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin/rest/tai-khoan")
public class RestTaiKhoanKhachHangController {
    @Autowired
    private TaiKhoanKhachHangService taiKhoanKhachHangService;

    @GetMapping
    public ResponseEntity getHangKhachHangPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                               @RequestParam(value = "search", required = false) String searchText,
                                               @RequestParam(value = "status", required = false) Integer status) {
        return new ResponseEntity(taiKhoanKhachHangService.getPage(page, searchText, status), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity addTaiKhoan(@Valid @RequestBody TaiKhoanRequest taiKhoanRequest, BindingResult result) {
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return new ResponseEntity(taiKhoanKhachHangService.addTaiKhoan(taiKhoanRequest), HttpStatus.OK);
    }


//        @PostMapping("/login")
//        public ResponseEntity<TaiKhoanResponse> khachHangLogin(@RequestBody TaiKhoanRequest taiKhoanRequest) {
//            try {
//                TaiKhoanResponse response = taiKhoanKhachHangService.khachHanglogin(taiKhoanRequest);
//                return new ResponseEntity<>(response, HttpStatus.OK);
//            } catch (NotFoundException e) {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        }


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
