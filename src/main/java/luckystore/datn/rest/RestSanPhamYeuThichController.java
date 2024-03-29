package luckystore.datn.rest;


import luckystore.datn.model.request.SanPhamYeuThichRequest;
import luckystore.datn.service.SanPhamYeuThichService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/rest/khach-hang/san-pham-yeu-thich")
public class RestSanPhamYeuThichController {
    @Autowired
    private SanPhamYeuThichService sanPhamYeuThichService;

    @GetMapping
    public ResponseEntity getSanPhamYeuThichPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                 @RequestParam(value = "search", required = false) String searchText,
                                                 @RequestParam(value = "idKhachHang", required = false) Long idKhachHang
    ) {
        return new ResponseEntity(sanPhamYeuThichService.getPage(page, searchText, idKhachHang), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteSanPhamYeuThich(@PathVariable("id") Long id) {

        sanPhamYeuThichService.deleteSanPhamYeuThick(id);
    }

    @PostMapping
    public ResponseEntity addSanPhamYeuThich(@RequestBody SanPhamYeuThichRequest sanPhamYeuThichRequest, BindingResult result) {
        System.out.println(sanPhamYeuThichRequest.toString());
        ResponseEntity errorJson = getErrorJson(result);
        if (errorJson != null) return errorJson;

        return new ResponseEntity(sanPhamYeuThichService.addSanPhamYeuThich(sanPhamYeuThichRequest), HttpStatus.OK);
    }

    @GetMapping("/existsByIdKhachHangAndIdIdGiay")
    public ResponseEntity existsByIdKhachHangAndIdGiay(@RequestParam("idKhachHang") Long idKhachHang, @RequestParam("idGiay") Long idGiay) {
        return new ResponseEntity(sanPhamYeuThichService.existsByIdKhachHangAndIdIdGiay(idKhachHang, idGiay), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity deleteSanPhamYeuThichByIdKhachHangAndIdGiay(@RequestParam("idKhachHang") Long idKhachHang, @RequestParam("idGiay") Long idGiay) {
        try {
            sanPhamYeuThichService.deleteSanPhamYeuThichByIdKhachHangAndIdGiay(idKhachHang, idGiay);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
