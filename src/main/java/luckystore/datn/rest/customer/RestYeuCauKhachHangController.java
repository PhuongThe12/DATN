package luckystore.datn.rest.customer;

import luckystore.datn.service.customer.YeuCauKhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/rest/yeu-cau-khach-hang")
public class RestYeuCauKhachHangController {
    private final YeuCauKhachHangService yeuCauKhachHangService;

    @Autowired
    public RestYeuCauKhachHangController(YeuCauKhachHangService yeuCauKhachHangService) {
        this.yeuCauKhachHangService = yeuCauKhachHangService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getChatLieu(@PathVariable("id") Long id) {
        return new ResponseEntity<>(yeuCauKhachHangService.getOneHoaDonYeuCauRespone(id), HttpStatus.OK);
    }
}
