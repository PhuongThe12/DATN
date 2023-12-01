package luckystore.datn.service.customer.impl;

import luckystore.datn.model.response.HoaDonYeuCauRespone;
import luckystore.datn.repository.HoaDonRepository;
import luckystore.datn.service.customer.YeuCauKhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class YeuCauKhachHangServiceImpl implements YeuCauKhachHangService {
    private final HoaDonRepository hoaDonRepository;

    @Autowired
    public YeuCauKhachHangServiceImpl(HoaDonRepository hoaDonRepository) {
        this.hoaDonRepository = hoaDonRepository;
    }

    @Override
    public HoaDonYeuCauRespone getOneHoaDonYeuCauRespone(Long id) {
        return hoaDonRepository.getOneHoaDonYeuCau(id);
    }
}
