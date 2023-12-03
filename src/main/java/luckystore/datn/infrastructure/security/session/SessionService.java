package luckystore.datn.infrastructure.security.session;


import luckystore.datn.entity.KhachHang;
import luckystore.datn.entity.NhanVien;

public interface SessionService {

    KhachHang getCustomer();

    NhanVien getAdmintrator();
}
