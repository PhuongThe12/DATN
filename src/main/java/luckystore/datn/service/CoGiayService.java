package luckystore.datn.service;

import luckystore.datn.model.request.CoGiayRequest;
import luckystore.datn.model.response.CoGiayResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CoGiayService {
    List<CoGiayResponse> getAll();

    Page<CoGiayResponse> getPage(int page, String searchText, Integer status);

    CoGiayResponse addCoGiay(CoGiayRequest coGiayRequest);

    CoGiayResponse updateCoGiay(Long id, CoGiayRequest coGiayRequest);

    CoGiayResponse findById(Long id);
}
