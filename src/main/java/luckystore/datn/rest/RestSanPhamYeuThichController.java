package luckystore.datn.rest;

import luckystore.datn.model.request.DiaChiNhanHangRequest;
import luckystore.datn.model.request.SanPhamYeuThichRequest;
import luckystore.datn.service.ProvincesService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rest/khach-hang/san-pham-yeu-thich")
public class RestSanPhamYeuThichController {
    @Autowired
    private SanPhamYeuThichService sanPhamYeuThichService;

    @GetMapping()
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(sanPhamYeuThichService.getAll(), HttpStatus.OK);
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
