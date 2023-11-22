package luckystore.datn.service.impl;

import luckystore.datn.entity.HoaDonChiTiet;
import luckystore.datn.model.response.DonMuaResponse;
import luckystore.datn.model.response.HangKhachHangResponse;
import luckystore.datn.model.response.HoaDonChiTietResponse;
import luckystore.datn.repository.HoaDonChiTietRepository;
import luckystore.datn.service.HoaDonChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoaDonChiTietServiceImpl implements HoaDonChiTietService {
    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepo;

    @Override
    public Page<DonMuaResponse> getAll(int page, Integer status) {
        return hoaDonChiTietRepo.donMua(status, PageRequest.of((page - 1), 5));
    }

}
