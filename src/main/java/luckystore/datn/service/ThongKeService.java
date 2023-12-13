package luckystore.datn.service;

import luckystore.datn.model.request.ThongKeRequest;
import luckystore.datn.model.response.thongKe.HoaDonThongKe;
import luckystore.datn.model.response.thongKe.SanPhamBanChay;
import luckystore.datn.model.response.thongKe.ThongKeTheoNam;
import luckystore.datn.model.response.thongKe.ThongKeTongQuan;
import java.util.List;

public interface ThongKeService {

    List<SanPhamBanChay> getSanPhamBanChay(ThongKeRequest request);

    ThongKeTongQuan getThongKeTongQuan(ThongKeRequest request);

    List<HoaDonThongKe> getListHoaDonThongKe(ThongKeRequest request);

    List<ThongKeTheoNam> getThongKeTheoNam(Integer year);
}
