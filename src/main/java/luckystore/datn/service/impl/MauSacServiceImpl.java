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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MauSacServiceImpl implements MauSacService {

    @Autowired
    private MauSacRepository mauSacRepo;

    @Override
    public List<MauSacResponse> getAll() {
        return mauSacRepo.findAllResponse();
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
        return mauSac;
    }

    private void checkWhenInsert(MauSacRequest mauSacRequest) {
        if (mauSacRepo.existsByTen(mauSacRequest.getTen())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }

    private void checkWhenUpdate(MauSacRequest mauSacRequest) {
        if (mauSacRepo.existsByTenAndIdNot(mauSacRequest.getTen(), mauSacRequest.getId())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }
}
