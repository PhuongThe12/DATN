package luckystore.datn.rest;

import luckystore.datn.exception.ConflictException;
import luckystore.datn.model.request.BienTheGiayGioHangRequest;
import luckystore.datn.model.request.GioHangChiTietRequest;
import luckystore.datn.model.request.GioHangRequest;
import luckystore.datn.model.response.GioHangResponse;
import luckystore.datn.repository.GioHangChiTietRepository;
import luckystore.datn.service.GioHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/rest/user/gio-hang")
public class RestGioHangController {

    @Autowired
    GioHangService gioHangService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getGioHangByIdKhachHang(@PathVariable("id") Long id) {
        return new ResponseEntity(gioHangService.getGioHangByKhachHang(id), HttpStatus.OK);
    }

    @PutMapping("/update/so-luong")
    public ResponseEntity<?> updateGioHangChiTiet(@RequestBody GioHangChiTietRequest gioHangChiTietRequest) {
        try {
            gioHangService.updateSoLuongGioHang(gioHangChiTietRequest);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{idGioHang}/so-luong/{id}")
    public ResponseEntity<?> getSoLuong(@PathVariable("idGioHang") Long idGioHang, @PathVariable("id") Long id) {
        System.out.println(idGioHang + " " + id);
        return ResponseEntity.ok(gioHangService.getSoLuong(id, idGioHang));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteGioHangChiTiet(@RequestBody GioHangChiTietRequest gioHangChiTietRequest) {
        try {
            gioHangService.deleteGioHangChiTiet(gioHangChiTietRequest);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping
    public ResponseEntity<?> addGioHangChiTiet(@RequestBody GioHangChiTietRequest gioHangChiTietRequest) {
        return new ResponseEntity<>(gioHangService.addGiohangChiTiet(gioHangChiTietRequest), HttpStatus.OK);
    }

    @PostMapping("/check-so-luong")
    public ResponseEntity<?> checkSoLuong(@RequestBody Set<BienTheGiayGioHangRequest> bienTheGiayGioHangRequestList) {
        try {
            gioHangService.checkSoLuong(bienTheGiayGioHangRequestList);
            return ResponseEntity.ok().build();
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getData());
        }
    }

}
