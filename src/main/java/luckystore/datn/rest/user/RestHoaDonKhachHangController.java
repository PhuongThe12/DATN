package luckystore.datn.rest.user;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import luckystore.datn.model.request.*;
import luckystore.datn.service.user.HoaDonKhachHangService;
import luckystore.datn.validation.groups.CreateGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public ResponseEntity getOne(@PathVariable("id") Long id) {
        return ResponseEntity.ok(hoaDonKhachHangService.findById(id));
    }

    @PutMapping("/update-dia-chi-nhan")
    public ResponseEntity updateDiaChiNhan(@RequestBody HoaDonDiaChiNhanRequest hoaDonDiaChiNhanRequest){
        return ResponseEntity.ok(hoaDonKhachHangService.capNhatDiaChiNhan(hoaDonDiaChiNhanRequest));
    }

    @GetMapping("/get-chi-tiet-thanh-toan/{id}")
    public ResponseEntity getChiTietThanhToan(@PathVariable("id") Long id){
        return ResponseEntity.ok(hoaDonKhachHangService.getThanhToanChiTiet(id));
    }


}
