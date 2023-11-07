package luckystore.datn.service.impl;

import luckystore.datn.model.response.HoaDonResponse;
import luckystore.datn.service.HoaDonService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoaDonChiTietServiceImpl implements HoaDonService {
    @Override
    public List<HoaDonResponse> getAll() {
        return null;
    }

    @Override
    public Page<HoaDonResponse> getPage(int page, String searchText, Integer status) {
        return null;
    }

    @Override
    public HoaDonResponse findById(Long id) {
        return null;
    }
}
