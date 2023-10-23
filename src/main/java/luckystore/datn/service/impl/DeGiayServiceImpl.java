package luckystore.datn.service.impl;


import luckystore.datn.constraints.ErrorMessage;
import luckystore.datn.entity.DeGiay;
import luckystore.datn.exception.DuplicateException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.exception.NullException;
import luckystore.datn.model.request.DeGiayRequest;
import luckystore.datn.model.response.DeGiayResponse;
import luckystore.datn.repository.DeGiayRepository;
import luckystore.datn.service.DeGiayService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeGiayServiceImpl implements DeGiayService {

    @Autowired
    private DeGiayRepository deGiayRepo;

    @Override
    public List<DeGiayResponse> getAll() {
        return deGiayRepo.findAllResponse();
    }

    @Override
    public Page<DeGiayResponse> getPage(int page, String searchText, Integer status) {
        return deGiayRepo.getPageResponse(searchText, status, PageRequest.of((page - 1), 5));
    }

    @Override
    public DeGiayResponse addDeGiay(DeGiayRequest deGiayRequest) {
        checkWhenInsert(deGiayRequest);
        DeGiay deGiay = getDeGiay(new DeGiay(), deGiayRequest);
        return new DeGiayResponse(deGiayRepo.save(deGiay));
    }

    @Override
    public DeGiayResponse updateDeGiay(Long id, DeGiayRequest deGiayRequest) {
        DeGiay deGiay;
        if (id == null) {
            throw new NullException();
        } else {
            deGiay = deGiayRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        }

        checkWhenUpdate(deGiayRequest);
        deGiay = getDeGiay(deGiay, deGiayRequest);
        return new DeGiayResponse(deGiayRepo.save(deGiay));
    }

    @Override
    public DeGiayResponse findById(Long id) {
        return new DeGiayResponse(deGiayRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

    private DeGiay getDeGiay(DeGiay deGiay, DeGiayRequest deGiayRequest) {
        deGiay.setTen(deGiayRequest.getTen());
        deGiay.setMoTa(deGiayRequest.getMoTa());
        deGiay.setTrangThai(deGiayRequest.getTrangThai() == null || deGiayRequest.getTrangThai() == 0 ? 0 : 1);
        return deGiay;
    }

    private void checkWhenInsert(DeGiayRequest deGiayRequest) {
        if (deGiayRepo.existsByTen(deGiayRequest.getTen())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }

    private void checkWhenUpdate(DeGiayRequest deGiayRequest) {
        if (deGiayRepo.existsByTenAndIdNot(deGiayRequest.getTen(), deGiayRequest.getId())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }
}

