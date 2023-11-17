package luckystore.datn.service.impl;

import luckystore.datn.model.response.HoaDonChiTietResponse;
import luckystore.datn.model.response.HoaDonResponse;
import luckystore.datn.repository.HoaDonChiTietRepository;
import luckystore.datn.service.HoaDonChiTietService;
import luckystore.datn.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoaDonChiTietServiceImpl implements HoaDonChiTietService {

    @Autowired
    HoaDonChiTietRepository hoaDonChiTietRepository;

    @Override
    public List<HoaDonChiTietResponse> getAll() {
        return hoaDonChiTietRepository.findAllResponse();
    }

    @Override
    public List<HoaDonChiTietResponse> getAllByIdHoaDon(Long id) {
        return hoaDonChiTietRepository.findAllResponse(id);
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
