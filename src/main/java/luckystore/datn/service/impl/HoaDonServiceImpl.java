package luckystore.datn.service.impl;

import luckystore.datn.constraints.ErrorMessage;
import luckystore.datn.entity.HoaDon;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.model.request.HoaDonSearch;
import luckystore.datn.model.response.HashTagResponse;
import luckystore.datn.model.response.HoaDonResponse;
import luckystore.datn.model.response.HoaDonYeuCauRespone;
import luckystore.datn.repository.HoaDonRepository;
import luckystore.datn.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class HoaDonServiceImpl implements HoaDonService {

    @Autowired
    HoaDonRepository hoaDonRepository;
    @Override
    public List<HoaDonResponse> getAll() {
        return hoaDonRepository.findAllResponse();
    }

    @Override
    public Page<HoaDonResponse> getPage(int page, String searchText, Integer status) {
        return hoaDonRepository.getPageResponse(searchText, status, PageRequest.of((page - 1), 5));
    }

    @Override
    public Page<HoaDonYeuCauRespone> getPageHoaDonYeuCau(int page, HoaDonSearch hoaDonSearch) {
        return hoaDonRepository.getPageHoaDonYeuCauResponse(hoaDonSearch,PageRequest.of((page - 1), 5));
    }

    @Override
    public HoaDonResponse findById(Long id) {
        return new HoaDonResponse(hoaDonRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }
}
