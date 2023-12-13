package luckystore.datn.rest.user;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import luckystore.datn.model.request.GiayRequest;
import luckystore.datn.model.request.GioHangThanhToanRequest;
import luckystore.datn.model.request.HoaDonRequest;
import luckystore.datn.model.request.HoaDonThanhToanTaiQuayRequest;
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
@RequestMapping("/rest/user/hoa-don")
public class RestHoaDonKhachHangController {

    @Autowired
    HoaDonKhachHangService hoaDonKhachHangService;


    @PostMapping
    ResponseEntity<?> addHoaDon(@RequestBody GioHangThanhToanRequest gioHangThanhToanRequest) throws MessagingException {
        return new ResponseEntity(hoaDonKhachHangService.addHoaDon(gioHangThanhToanRequest), HttpStatus.OK);
    }

    @PostMapping("/hoan-tat-banking")
    ResponseEntity<?> hoanTatBanking(@Valid @RequestBody HoaDonThanhToanTaiQuayRequest request) {
        return ResponseEntity.ok(hoaDonKhachHangService.hoanTatThanhToan(request));

    }

}
