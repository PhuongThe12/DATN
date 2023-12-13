package luckystore.datn.service.impl;

import lombok.RequiredArgsConstructor;
import luckystore.datn.model.request.ThongKeRequest;
import luckystore.datn.model.response.thongKe.HoaDonThongKe;
import luckystore.datn.model.response.thongKe.SanPhamBanChay;
import luckystore.datn.model.response.thongKe.ThongKeTheoNam;
import luckystore.datn.model.response.thongKe.ThongKeTongQuan;
import luckystore.datn.repository.HoaDonChiTietRepository;
import luckystore.datn.repository.HoaDonRepository;
import luckystore.datn.service.ThongKeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThongKeServiceImpl implements ThongKeService {

    private final HoaDonRepository hoaDonRepository;

    private final HoaDonChiTietRepository hoaDonChiTietRepository;


    @Override
    public List<SanPhamBanChay> getSanPhamBanChay(ThongKeRequest request) {
        return hoaDonChiTietRepository.getSanPhamBanChay(request);
    }

    @Override
    public ThongKeTongQuan getThongKeTongQuan(ThongKeRequest request) {
        return hoaDonRepository.getThongKeTongQuan(request);
    }

    @Override
    public List<HoaDonThongKe> getListHoaDonThongKe(ThongKeRequest request) {
        return hoaDonRepository.getListHoaDonThongKe(request);
    }

    @Override
    public List<ThongKeTheoNam> getThongKeTheoNam(Integer year) {
        return hoaDonRepository.getThongKeTheoNam(year);
    }
}
