package luckystore.datn.rest;

import luckystore.datn.service.HoaDonChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/rest/hoa-don-chi-tiet")
public class RestHoaDonChiTietController {
    @Autowired
    private HoaDonChiTietService hoaDonChiTietService;

    @GetMapping("/get-all")
    public ResponseEntity getHangKhachHangPage(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                               @RequestParam(value = "status", required = false) Integer status) {
        return new ResponseEntity(hoaDonChiTietService.getAll(page,status), HttpStatus.OK);
    }
}
