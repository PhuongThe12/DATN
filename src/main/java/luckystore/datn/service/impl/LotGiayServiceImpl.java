package luckystore.datn.service.impl;


import luckystore.datn.infrastructure.constraints.ErrorMessage;
import luckystore.datn.entity.LotGiay;
import luckystore.datn.exception.DuplicateException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.exception.NullException;
import luckystore.datn.model.request.LotGiayRequest;
import luckystore.datn.model.response.LotGiayResponse;
import luckystore.datn.repository.LotGiayRepository;
import luckystore.datn.service.LotGiayService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LotGiayServiceImpl implements LotGiayService {

    @Autowired
    private LotGiayRepository lotGiayRepo;

    @Override
    public List<LotGiayResponse> getAll() {
        return lotGiayRepo.findAllActive();
    }

    @Override
    public Page<LotGiayResponse> getPage(int page, String searchText, Integer status) {
        return lotGiayRepo.getPageResponse(searchText, status, PageRequest.of((page - 1), 5));
    }

    @Override
    public LotGiayResponse addLotGiay(LotGiayRequest lotGiayRequest) {
        checkWhenInsert(lotGiayRequest);
        LotGiay lotGiay = getLotGiay(new LotGiay(), lotGiayRequest);
        return new LotGiayResponse(lotGiayRepo.save(lotGiay));
    }

    @Override
    public LotGiayResponse updateLotGiay(Long id, LotGiayRequest lotGiayRequest) {
        LotGiay lotGiay;
        if (id == null) {
            throw new NullException();
        } else {
            lotGiay = lotGiayRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        }

        checkWhenUpdate(lotGiayRequest);
        lotGiay = getLotGiay(lotGiay, lotGiayRequest);
        return new LotGiayResponse(lotGiayRepo.save(lotGiay));
    }

    @Override
    public LotGiayResponse findById(Long id) {
        return new LotGiayResponse(lotGiayRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

    private LotGiay getLotGiay(LotGiay lotGiay, LotGiayRequest lotGiayRequest) {
        lotGiay.setTen(lotGiayRequest.getTen());
        lotGiay.setMoTa(lotGiayRequest.getMoTa());
        lotGiay.setTrangThai(lotGiayRequest.getTrangThai() == null || lotGiayRequest.getTrangThai() == 0 ? 0 : 1);
        return lotGiay;
    }

    private void checkWhenInsert(LotGiayRequest lotGiayRequest) {
        if (lotGiayRepo.existsByTen(lotGiayRequest.getTen())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }

    private void checkWhenUpdate(LotGiayRequest lotGiayRequest) {
        if (lotGiayRepo.existsByTenAndIdNot(lotGiayRequest.getTen(), lotGiayRequest.getId())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }
}

