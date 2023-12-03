package luckystore.datn.rest;

import luckystore.datn.service.HoaDonChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rest/admin/hoa-don-chi-tiet")
public class RestHoaDonChiTietController {

    @Autowired
    HoaDonChiTietService hoaDonChiTietService;

    @GetMapping("/find-by-id-hoa-don/{id}")
    public ResponseEntity getListByIdHoaDon(@PathVariable("id") Long id){
        return new ResponseEntity<>(hoaDonChiTietService.getAllByIdHoaDon(id),HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity getHangKhachHangPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                               @RequestParam(value = "status", required = false) Integer status) {
        return new ResponseEntity(hoaDonChiTietService.getAll(page,status), HttpStatus.OK);
    }
}
