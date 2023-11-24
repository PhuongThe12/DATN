package luckystore.datn.vnpay;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import luckystore.datn.model.request.HoaDonThanhToanTaiQuayRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vnpay")
@RequiredArgsConstructor
public class VNPayController {

    private final VNPayService vnPayService;

    @PostMapping("/create-vnpay-order-tai-quay")
    public ResponseEntity<String> createOrder(@Valid @RequestBody HoaDonThanhToanTaiQuayRequest hoaDonRequest,
                                              HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + ReturnUrlConst.taiQuay;
        String returnUrl = vnPayService.createOrder(hoaDonRequest.getTienChuyenKhoan(), hoaDonRequest.getIdHoaDon(), baseUrl);
        return ResponseEntity.ok(new Gson().toJson(returnUrl));
    }

}
