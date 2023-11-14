package luckystore.datn.service.impl;

import luckystore.datn.entity.HoaDonChiTiet;
import luckystore.datn.model.response.HoaDonChiTietResponse;
import luckystore.datn.model.response.HoaDonResponse;
import luckystore.datn.repository.HoaDonChiTietRepository;
import luckystore.datn.service.HoaDonChiTietService;
import luckystore.datn.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoaDonChiTietServiceImpl implements HoaDonChiTietService {
    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepo;


    @Override
    public List<HoaDonChiTietResponse> getDonMua() {
        return hoaDonChiTietRepo.findAllResponse();
    }

    @Override
    public Page<HoaDonChiTietResponse> getPage(int page, String searchText, Integer status) {
        return hoaDonChiTietRepo.getPageResponse(searchText, status, PageRequest.of((page - 1), 5));
    }





}
