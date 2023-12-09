package luckystore.datn.service.impl;


import luckystore.datn.infrastructure.constraints.ErrorMessage;
import luckystore.datn.entity.MuiGiay;
import luckystore.datn.exception.DuplicateException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.exception.NullException;
import luckystore.datn.model.request.MuiGiayRequest;
import luckystore.datn.model.response.MuiGiayResponse;
import luckystore.datn.repository.MuiGiayRepository;
import luckystore.datn.service.MuiGiayService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MuiGiayServiceImpl implements MuiGiayService {

    @Autowired
    private MuiGiayRepository muiGiayRepo;

    @Override
    public List<MuiGiayResponse> getAll() {
        return muiGiayRepo.findAllActive();
    }

    @Override
    public Page<MuiGiayResponse> getPage(int page, String searchText, Integer status) {
        return muiGiayRepo.getPageResponse(searchText, status, PageRequest.of((page - 1), 5));
    }

    @Override
    public MuiGiayResponse addMuiGiay(MuiGiayRequest muiGiayRequest) {
        checkWhenInsert(muiGiayRequest);
        MuiGiay muiGiay = getMuiGiay(new MuiGiay(), muiGiayRequest);
        return new MuiGiayResponse(muiGiayRepo.save(muiGiay));
    }

    @Override
    public MuiGiayResponse updateMuiGiay(Long id, MuiGiayRequest muiGiayRequest) {
        MuiGiay muiGiay;
        if (id == null) {
            throw new NullException();
        } else {
            muiGiay = muiGiayRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        }

        checkWhenUpdate(muiGiayRequest);
        muiGiay = getMuiGiay(muiGiay, muiGiayRequest);
        return new MuiGiayResponse(muiGiayRepo.save(muiGiay));
    }

    @Override
    public MuiGiayResponse findById(Long id) {
        return new MuiGiayResponse(muiGiayRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

    private MuiGiay getMuiGiay(MuiGiay muiGiay, MuiGiayRequest muiGiayRequest) {
        muiGiay.setTen(muiGiayRequest.getTen());
        muiGiay.setMoTa(muiGiayRequest.getMoTa());
        muiGiay.setTrangThai(muiGiayRequest.getTrangThai() == null || muiGiayRequest.getTrangThai() == 0 ? 0 : 1);
        return muiGiay;
    }

    private void checkWhenInsert(MuiGiayRequest muiGiayRequest) {
        if (muiGiayRepo.existsByTen(muiGiayRequest.getTen())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }

    private void checkWhenUpdate(MuiGiayRequest muiGiayRequest) {
        if (muiGiayRepo.existsByTenAndIdNot(muiGiayRequest.getTen(), muiGiayRequest.getId())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }
}
