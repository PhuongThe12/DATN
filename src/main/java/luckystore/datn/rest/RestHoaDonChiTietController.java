package luckystore.datn.rest;

import luckystore.datn.service.HoaDonChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/rest/hoa-don-chi-tiet")
public class RestHoaDonChiTietController {


    @Autowired
    HoaDonChiTietService hoaDonChiTietService;

    @GetMapping("/find-by-id-hoa-don/{id}")
    public ResponseEntity getListByIdHoaDon(@PathVariable("id") Long id){
        return new ResponseEntity<>(hoaDonChiTietService.getAllByIdHoaDon(id),HttpStatus.OK);
    }

}
