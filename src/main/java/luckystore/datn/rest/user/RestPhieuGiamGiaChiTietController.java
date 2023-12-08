package luckystore.datn.rest.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import luckystore.datn.entity.PhieuGiamGiaChiTiet;
import luckystore.datn.model.request.CoGiayRequest;
import luckystore.datn.service.PhieuGiamGiaChiTietService;
import luckystore.datn.service.PhieuGiamGiaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/rest/phieu-chi-tiet")
@RequiredArgsConstructor
public class RestPhieuGiamGiaChiTietController {

    private final PhieuGiamGiaChiTietService phieuGiamGiaChiTietService;

    @PostMapping("/add-phieu-chi-tiet")
    public ResponseEntity<?> addCoGiay(@RequestBody PhieuGiamGiaChiTiet phieuGiamGiaChiTiet) {
        return new ResponseEntity<>(phieuGiamGiaChiTietService.save(phieuGiamGiaChiTiet), HttpStatus.OK);
    }
}
