package luckystore.datn.service;

import luckystore.datn.model.request.LotGiayRequest;
import luckystore.datn.model.response.LotGiayResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LotGiayService {
    List<LotGiayResponse> getAll();

    Page<LotGiayResponse> getPage(int page, String searchText, Integer status);

    LotGiayResponse addLotGiay(LotGiayRequest lotGiayRequest);

    LotGiayResponse updateLotGiay(Long id, LotGiayRequest lotGiayRequest);

    LotGiayResponse findById(Long id);
}

