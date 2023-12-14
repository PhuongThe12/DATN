package luckystore.datn.rest.admin;

import luckystore.datn.entity.KhachHang;
import luckystore.datn.entity.NhanVien;
import luckystore.datn.infrastructure.security.session.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/session")
public class RestSessionController {

    @Autowired
    private SessionService sessionService;

    @GetMapping("/get-staff")
    public NhanVien getStaff() {
        return sessionService.getAdmintrator();
    }
    
    @GetMapping("/get-customer")
    public KhachHang getKhachHang() {
        return sessionService.getCustomer();
    }


}
