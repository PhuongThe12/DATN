package luckystore.datn.rest.user;

import luckystore.datn.model.request.GiayRequest;
import luckystore.datn.model.request.GioHangThanhToanRequest;
import luckystore.datn.model.request.HoaDonRequest;
import luckystore.datn.service.user.HoaDonKhachHangService;
import luckystore.datn.validation.groups.CreateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/rest/hoa-don")
public class RestHoaDonKhachHangController {

    @Autowired
    HoaDonKhachHangService hoaDonKhachHangService;


    @PostMapping
    ResponseEntity<?> addHoaDon(@RequestBody GioHangThanhToanRequest gioHangThanhToanRequest) {
        return new ResponseEntity(hoaDonKhachHangService.addHoaDon(gioHangThanhToanRequest), HttpStatus.OK);
    }


}
