package luckystore.datn.service.impl;

import luckystore.datn.infrastructure.constraints.ErrorMessage;
import luckystore.datn.entity.HangKhachHang;
import luckystore.datn.exception.DuplicateException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.exception.NullException;
import luckystore.datn.model.request.HangKhachHangRequest;
import luckystore.datn.model.response.HangKhachHangResponse;
import luckystore.datn.repository.HangKhachHangRepository;
import luckystore.datn.service.HangKhachHangService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HangKhachHangServiceImpl implements HangKhachHangService {
    @Autowired
    private HangKhachHangRepository hangKhachHangRepo;

    @Override
    public List<HangKhachHangResponse> getAll() {
        return hangKhachHangRepo.findAllResponse();
    }

    @Override
    public Page<HangKhachHangResponse> getPage(int page, String searchText, Integer status) {
        return hangKhachHangRepo.getPageResponse(searchText, status, PageRequest.of((page - 1), 5));
    }

    @Override
    public HangKhachHangResponse addHangKhachHang(HangKhachHangRequest hangKhachHangRequest) {
        checkWhenInsert(hangKhachHangRequest);
        HangKhachHang hangKhachHang = getHangKhachHang(new HangKhachHang(), hangKhachHangRequest);
        return new HangKhachHangResponse(hangKhachHangRepo.save(hangKhachHang));
    }

    @Override
    public HangKhachHangResponse updateHangKhachHang(Long id, HangKhachHangRequest hangKhachHangRequest) {
        HangKhachHang hangKhachHang;
        if (id == null) {
            throw new NullException();
        } else {
            hangKhachHang = hangKhachHangRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        }

        checkWhenUpdate(hangKhachHangRequest);
        hangKhachHang = getHangKhachHang(hangKhachHang, hangKhachHangRequest);
        return new HangKhachHangResponse(hangKhachHangRepo.save(hangKhachHang));
    }

    @Override
    public HangKhachHangResponse findById(Long id) {
        return new HangKhachHangResponse(hangKhachHangRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

    private void checkWhenInsert(HangKhachHangRequest hangKhachHangRequest) {
        if (hangKhachHangRepo.existsByTenHang(hangKhachHangRequest.getTenHang())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }

    private void checkWhenUpdate(HangKhachHangRequest hangKhachHangRequest) {
        if (hangKhachHangRepo.existsByTenHangAndIdNot(hangKhachHangRequest.getTenHang(), hangKhachHangRequest.getId())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }

    private HangKhachHang getHangKhachHang(HangKhachHang hangKhachHang, HangKhachHangRequest hangKhachHangRequest) {
        hangKhachHang.setTenHang(hangKhachHangRequest.getTenHang());
        hangKhachHang.setDieuKien(hangKhachHangRequest.getDieuKien());
        hangKhachHang.setUuDai(hangKhachHangRequest.getUuDai());
        hangKhachHang.setMoTa(hangKhachHangRequest.getMoTa());
        hangKhachHang.setTrangThai(hangKhachHangRequest.getTrangThai() == null || hangKhachHangRequest.getTrangThai() == 0 ? 0 : 1);
        return hangKhachHang;
    }
}
