package luckystore.datn.rest;

import luckystore.datn.model.response.GioHangResponse;
import luckystore.datn.service.GioHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/rest/gio-hang")
public class RestGioHangController {

    @Autowired
    GioHangService gioHangService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getGioHangByIdKhachHang(@PathVariable("id") Long id) {
        return new ResponseEntity(gioHangService.getGioHangByKhachHang(id), HttpStatus.OK);
    }
}
