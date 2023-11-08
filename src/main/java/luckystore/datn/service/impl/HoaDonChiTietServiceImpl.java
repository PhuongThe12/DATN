package luckystore.datn.service.impl;

import luckystore.datn.model.response.HoaDonChiTietResponse;
import luckystore.datn.model.response.HoaDonResponse;
import luckystore.datn.service.HoaDonChiTietService;
import luckystore.datn.service.HoaDonService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoaDonChiTietServiceImpl implements HoaDonChiTietService {

    @Override
    public List<HoaDonChiTietResponse> getAll() {
        return null;
    }

    @Override
    public Page<HoaDonChiTietResponse> getPage(int page, String searchText, Integer status) {
        return null;
    }

    @Override
    public HoaDonChiTietResponse findById(Long id) {
        return null;
    }
}
