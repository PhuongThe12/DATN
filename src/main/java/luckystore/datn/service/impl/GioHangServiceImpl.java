package luckystore.datn.service.impl;

import luckystore.datn.model.response.GioHangResponse;
import luckystore.datn.repository.GioHangRepository;
import luckystore.datn.service.GioHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GioHangServiceImpl implements GioHangService {

    @Autowired
    GioHangRepository gioHangRepository;

    @Override
    public GioHangResponse getGioHangByKhachHang(Long id) {
        System.out.println(gioHangRepository.getGioHangByIdKhachHang(id));
        return gioHangRepository.getGioHangByIdKhachHang(id);
    }
}
