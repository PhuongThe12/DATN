package luckystore.datn.service.impl;


import luckystore.datn.constraints.ErrorMessage;
import luckystore.datn.entity.ThuongHieu;
import luckystore.datn.exception.DuplicateException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.exception.NullException;
import luckystore.datn.model.request.ThuongHieuRequest;
import luckystore.datn.model.response.ThuongHieuResponse;
import luckystore.datn.repository.ThuongHieuRepository;
import luckystore.datn.service.ThuongHieuService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThuongHieuServiceImpl implements ThuongHieuService {

    @Autowired
    private ThuongHieuRepository thuongHieuRepo;

    @Override
    public List<ThuongHieuResponse> getAll() {
        return thuongHieuRepo.findAllResponse();
    }

    @Override
    public Page<ThuongHieuResponse> getPage(int page, String searchText, Integer status) {
        return thuongHieuRepo.getPageResponse(searchText, status, PageRequest.of((page - 1), 5));
    }

    @Override
    public ThuongHieuResponse addThuongHieu(ThuongHieuRequest thuongHieuRequest) {
        checkWhenInsert(thuongHieuRequest);
        ThuongHieu thuongHieu = getThuongHieu(new ThuongHieu(), thuongHieuRequest);
        return new ThuongHieuResponse(thuongHieuRepo.save(thuongHieu));
    }

    @Override
    public ThuongHieuResponse updateThuongHieu(Long id, ThuongHieuRequest thuongHieuRequest) {
        ThuongHieu thuongHieu;
        if (id == null) {
            throw new NullException();
        } else {
            thuongHieu = thuongHieuRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        }

        checkWhenUpdate(thuongHieuRequest);
        thuongHieu = getThuongHieu(thuongHieu, thuongHieuRequest);
        return new ThuongHieuResponse(thuongHieuRepo.save(thuongHieu));
    }

    @Override
    public ThuongHieuResponse findById(Long id) {
        return new ThuongHieuResponse(thuongHieuRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

    private ThuongHieu getThuongHieu(ThuongHieu thuongHieu, ThuongHieuRequest thuongHieuRequest) {
        thuongHieu.setTen(thuongHieuRequest.getTen());
        thuongHieu.setMoTa(thuongHieuRequest.getMoTa());
        thuongHieu.setTrangThai(thuongHieuRequest.getTrangThai() == null || thuongHieuRequest.getTrangThai() == 0 ? 0 : 1);
        return thuongHieu;
    }

    private void checkWhenInsert(ThuongHieuRequest thuongHieuRequest) {
        if (thuongHieuRepo.existsByTen(thuongHieuRequest.getTen())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Thương hiệu đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }

    private void checkWhenUpdate(ThuongHieuRequest thuongHieuRequest) {
        if (thuongHieuRepo.existsByTenAndIdNot(thuongHieuRequest.getTen(), thuongHieuRequest.getId())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Thương hiệu đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }
}

