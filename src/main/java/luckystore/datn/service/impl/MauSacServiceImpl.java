package luckystore.datn.service.impl;

import luckystore.datn.constraints.ErrorMessage;
import luckystore.datn.entity.MauSac;
import luckystore.datn.exception.DuplicateException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.exception.NullException;
import luckystore.datn.model.request.MauSacRequest;
import luckystore.datn.model.response.MauSacResponse;
import luckystore.datn.repository.MauSacRepository;
import luckystore.datn.service.MauSacService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MauSacServiceImpl implements MauSacService {

    @Autowired
    private MauSacRepository mauSacRepo;

    @Override
    public List<MauSacResponse> getAll() {
        return mauSacRepo.findAllResponse();
    }

    @Override
    public Page<MauSacResponse> getPage(int page, String searchText, Integer status) {
        return mauSacRepo.getPageResponse(searchText, status, PageRequest.of((page - 1), 5));
    }

    @Override
    public MauSacResponse addMauSac(MauSacRequest mauSacRequest) {
        checkWhenInsert(mauSacRequest);
        MauSac mauSac = getMauSac(new MauSac(), mauSacRequest);
        return new MauSacResponse(mauSacRepo.save(mauSac));
    }

    @Override
    public MauSacResponse updateMauSac(Long id, MauSacRequest mauSacRequest) {
        MauSac mauSac;
        if (id == null) {
            throw new NullException();
        } else {
            mauSac = mauSacRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        }

        checkWhenUpdate(mauSacRequest);
        mauSac = getMauSac(mauSac, mauSacRequest);
        return new MauSacResponse(mauSacRepo.save(mauSac));
    }

    @Override
    public MauSacResponse findById(Long id) {
        return new MauSacResponse(mauSacRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

    private MauSac getMauSac(MauSac mauSac, MauSacRequest mauSacRequest) {
        mauSac.setTen(mauSacRequest.getTen());
        mauSac.setMoTa(mauSacRequest.getMoTa());
        mauSac.setMaMau(mauSacRequest.getMaMau());
        mauSac.setTrangThai(mauSacRequest.getTrangThai() == null || mauSacRequest.getTrangThai() == 0 ? 0 : 1);
        return mauSac;
    }

    private void checkWhenInsert(MauSacRequest mauSacRequest) {
        String error = "";
        if (mauSacRepo.existsByTen(mauSacRequest.getTen())) {
            error += JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
        }
        if (mauSacRepo.existsByMaMau(mauSacRequest.getMaMau())) {
            error += ", " + JsonString.errorToJsonObject("maMau", "Màu sắc đã tồn tại");
        }
        if (!error.isBlank()) {
            throw new DuplicateException(JsonString.stringToJson(error));
        }
    }

    private void checkWhenUpdate(MauSacRequest mauSacRequest) {

        String error = "";
        if (mauSacRepo.existsByTenAndIdNot(mauSacRequest.getTen(), mauSacRequest.getId())) {
            error += JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
        }
        if (mauSacRepo.existsByMaMauAndIdNot(mauSacRequest.getMaMau(), mauSacRequest.getId())) {
            error += ", " + JsonString.errorToJsonObject("maMau", "Màu sắc đã tồn tại");
        }
        if (!error.isBlank()) {
            throw new DuplicateException(JsonString.stringToJson(error));
        }
    }
}
