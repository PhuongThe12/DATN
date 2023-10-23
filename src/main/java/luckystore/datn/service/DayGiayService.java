package luckystore.datn.service;

import luckystore.datn.model.request.DayGiayRequest;
import luckystore.datn.model.response.DayGiayResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DayGiayService {
    List<DayGiayResponse> getAll();

    Page<DayGiayResponse> getPage(int page, String searchText, Integer status);

    DayGiayResponse addDayGiay(DayGiayRequest dayGiayRequest);

    DayGiayResponse updateDayGiay(Long id, DayGiayRequest dayGiayRequest);

    DayGiayResponse findById(Long id);
}

