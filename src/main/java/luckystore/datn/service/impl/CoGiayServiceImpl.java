package luckystore.datn.service.impl;

import luckystore.datn.constraints.ErrorMessage;
import luckystore.datn.entity.CoGiay;
import luckystore.datn.exception.DuplicateException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.exception.NullException;
import luckystore.datn.model.request.CoGiayRequest;
import luckystore.datn.model.response.CoGiayResponse;
import luckystore.datn.repository.CoGiayRepository;
import luckystore.datn.service.CoGiayService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoGiayServiceImpl implements CoGiayService {
    @Autowired
    private CoGiayRepository coGiayRepo;

    @Override
    public List<CoGiayResponse> getAll() {
        return coGiayRepo.findAllResponse();
    }

    @Override
    public Page<CoGiayResponse> getPage(int page, String searchText, Integer status) {
        return coGiayRepo.getPageResponse(searchText, status, PageRequest.of((page - 1), 5));
    }

    @Override
    public CoGiayResponse addCoGiay(CoGiayRequest coGiayRequest) {
        checkWhenInsert(coGiayRequest);
        CoGiay coGiay = getCoGiay(new CoGiay(), coGiayRequest);
        return new CoGiayResponse(coGiayRepo.save(coGiay));
    }

    @Override
    public CoGiayResponse updateCoGiay(Long id, CoGiayRequest coGiayRequest) {
        CoGiay coGiay;
        if (id == null) {
            throw new NullException();
        } else {
            coGiay = coGiayRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        }

        checkWhenUpdate(coGiayRequest);
        coGiay = getCoGiay(coGiay, coGiayRequest);
        return new CoGiayResponse(coGiayRepo.save(coGiay));
    }

    @Override
    public CoGiayResponse findById(Long id) {
        return new CoGiayResponse(coGiayRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

    private CoGiay getCoGiay(CoGiay coGiay, CoGiayRequest coGiayRequest) {
        coGiay.setTen(coGiayRequest.getTen());
        coGiay.setMoTa(coGiayRequest.getMoTa());
        coGiay.setTrangThai(coGiayRequest.getTrangThai() == null || coGiayRequest.getTrangThai() == 0 ? 0 : 1);
        return coGiay;
    }

    private void checkWhenInsert(CoGiayRequest coGiayRequest) {
        if (coGiayRepo.existsByTen(coGiayRequest.getTen())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }

    private void checkWhenUpdate(CoGiayRequest coGiayRequest) {
        if (coGiayRepo.existsByTenAndIdNot(coGiayRequest.getTen(), coGiayRequest.getId())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }
}
