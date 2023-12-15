package luckystore.datn.service.user.impl;

import luckystore.datn.model.response.HoaDonChiTietResponse;
import luckystore.datn.repository.HoaDonChiTietRepository;
import luckystore.datn.service.user.HoaDonChiTietKhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoaDonChiTietKhachHangServiceImpl implements HoaDonChiTietKhachHangService {
    private final HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    public HoaDonChiTietKhachHangServiceImpl(HoaDonChiTietRepository hoaDonChiTietRepository) {
        this.hoaDonChiTietRepository = hoaDonChiTietRepository;
    }

    @Override
    public List<HoaDonChiTietResponse> getAllByIdHoaDon(Long id) {
        return hoaDonChiTietRepository.findAllResponse(id);
    }
}
