package luckystore.datn.service;

import luckystore.datn.model.request.MuiGiayRequest;
import luckystore.datn.model.response.MuiGiayResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MuiGiayService {
    List<MuiGiayResponse> getAll();

    Page<MuiGiayResponse> getPage(int page, String searchText, Integer status);

    MuiGiayResponse addMuiGiay(MuiGiayRequest muiGiayRequest);

    MuiGiayResponse updateMuiGiay(Long id, MuiGiayRequest muiGiayRequest);

    MuiGiayResponse findById(Long id);
}

