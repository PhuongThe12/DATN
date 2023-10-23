package luckystore.datn.service;

import luckystore.datn.model.request.DeGiayRequest;
import luckystore.datn.model.response.DeGiayResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DeGiayService {
    List<DeGiayResponse> getAll();

    Page<DeGiayResponse> getPage(int page, String searchText, Integer status);

    DeGiayResponse addDeGiay(DeGiayRequest deGiayRequest);

    DeGiayResponse updateDeGiay(Long id, DeGiayRequest deGiayRequest);

    DeGiayResponse findById(Long id);
}

