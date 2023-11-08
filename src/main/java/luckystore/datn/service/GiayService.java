package luckystore.datn.service;

import luckystore.datn.model.request.GiayRequest;
import luckystore.datn.model.request.GiaySearch;
import luckystore.datn.model.response.GiayResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GiayService {

    List<GiayResponse> getAllActive();

    List<GiayResponse> getAllContains(List<Long> ids);

    GiayResponse getResponseById(Long id);

    GiayResponse addGiay(GiayRequest giayRequest);

    Page<GiayResponse> findAllForList(GiaySearch giaySearch);

    Page<GiayResponse>  getPage();

    GiayResponse updateSoLuong(GiayRequest giayRequest);

    GiayResponse updateGia(GiayRequest giayRequest);

    GiayResponse updateGiay(Long id, GiayRequest giayRequest);
}
