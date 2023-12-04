package luckystore.datn.infrastructure.security.session;


import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import luckystore.datn.entity.KhachHang;
import luckystore.datn.entity.NhanVien;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final HttpSession httpSession;

    @Override
    public KhachHang getCustomer() {
        return (KhachHang) httpSession.getAttribute("customer");
    }

    @Override
    public NhanVien getAdmintrator() {
        return (NhanVien) httpSession.getAttribute("employee");
    }
}
