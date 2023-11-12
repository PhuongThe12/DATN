package luckystore.datn.service.impl;

import luckystore.datn.constraints.ErrorMessage;
import luckystore.datn.entity.DayGiay;
import luckystore.datn.exception.DuplicateException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.exception.NullException;
import luckystore.datn.model.request.DayGiayRequest;
import luckystore.datn.model.response.DayGiayResponse;
import luckystore.datn.repository.DayGiayRepository;
import luckystore.datn.service.DayGiayService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DayGiayServiceImpl implements DayGiayService {

    @Autowired
    private DayGiayRepository dayGiayRepo;

    @Override
    public List<DayGiayResponse> getAll() {
        return dayGiayRepo.findAllActive();
    }

    @Override
    public Page<DayGiayResponse> getPage(int page, String searchText, Integer status) {
        return dayGiayRepo.getPageResponse(searchText, status, PageRequest.of((page - 1), 5));
    }

    @Override
    public DayGiayResponse addDayGiay(DayGiayRequest dayGiayRequest) {
        checkWhenInsert(dayGiayRequest);
        DayGiay dayGiay = getDayGiay(new DayGiay(), dayGiayRequest);
        return new DayGiayResponse(dayGiayRepo.save(dayGiay));
    }

    @Override
    public DayGiayResponse updateDayGiay(Long id, DayGiayRequest dayGiayRequest) {
        DayGiay dayGiay;
        if (id == null) {
            throw new NullException();
        } else {
            dayGiay = dayGiayRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        }

        checkWhenUpdate(dayGiayRequest);
        dayGiay = getDayGiay(dayGiay, dayGiayRequest);
        return new DayGiayResponse(dayGiayRepo.save(dayGiay));
    }

    @Override
    public DayGiayResponse findById(Long id) {
        return new DayGiayResponse(dayGiayRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

    private DayGiay getDayGiay(DayGiay dayGiay, DayGiayRequest dayGiayRequest) {
        dayGiay.setTen(dayGiayRequest.getTen());
        dayGiay.setMoTa(dayGiayRequest.getMoTa());
        dayGiay.setMauSac(dayGiayRequest.getMauSac());
        dayGiay.setTrangThai(dayGiayRequest.getTrangThai() == null || dayGiayRequest.getTrangThai() == 0 ? 0 : 1);
        return dayGiay;
    }

    private void checkWhenInsert(DayGiayRequest dayGiayRequest) {
        if (dayGiayRepo.existsByTen(dayGiayRequest.getTen())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }

    private void checkWhenUpdate(DayGiayRequest dayGiayRequest) {
        if (dayGiayRepo.existsByTenAndIdNot(dayGiayRequest.getTen(), dayGiayRequest.getId())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }
}
