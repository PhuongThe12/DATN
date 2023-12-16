package luckystore.datn.rest.user;

import luckystore.datn.service.user.HoaDonChiTietKhachHangService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/user/hoa-don-chi-tiet")
public class RestHoaDonChiTietKhachHangController {

    private final HoaDonChiTietKhachHangService hoaDonChiTietKhachHangService;

    public RestHoaDonChiTietKhachHangController(HoaDonChiTietKhachHangService hoaDonChiTietKhachHangService) {
        this.hoaDonChiTietKhachHangService = hoaDonChiTietKhachHangService;
    }


    @GetMapping("/find-by-id-hoa-don/{id}")
    public ResponseEntity getListByIdHoaDon(@PathVariable("id") Long id){
        return new ResponseEntity<>(hoaDonChiTietKhachHangService.getAllByIdHoaDon(id), HttpStatus.OK);
    }
}
