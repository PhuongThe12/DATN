package luckystore.datn.vnpay;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import luckystore.datn.model.request.DatHangTaiQuayRequest;
import luckystore.datn.model.request.HoaDonThanhToanTaiQuayRequest;
import luckystore.datn.service.HoaDonService;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vnpay")
@RequiredArgsConstructor
public class VNPayController {

    private final VNPayService vnPayService;
    private final HoaDonService hoaDonService;

    @PostMapping("/create-vnpay-order-tai-quay")
    public ResponseEntity<String> createOrder(@Valid @RequestBody HoaDonThanhToanTaiQuayRequest hoaDonRequest,
                                              HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + ReturnUrlConst.taiQuay;
        String returnUrl = vnPayService.createOrder(hoaDonRequest.getTienChuyenKhoan(), hoaDonRequest.getIdHoaDon(), baseUrl);
        return ResponseEntity.ok(new Gson().toJson(returnUrl));
    }

    @GetMapping("/cancel-banking/{id}")
    public ResponseEntity<?> cancelBanking(@PathVariable("id") Long id) {
        hoaDonService.cancelBanking(id);
        return ResponseEntity.ok(HttpEntity.EMPTY);
    }


}
