package luckystore.datn.rest;

import luckystore.datn.model.request.GioHangChiTietRequest;
import luckystore.datn.model.request.GioHangRequest;
import luckystore.datn.model.response.GioHangResponse;
import luckystore.datn.repository.GioHangChiTietRepository;
import luckystore.datn.service.GioHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/rest/gio-hang")
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

}
