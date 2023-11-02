package luckystore.datn.service;

import luckystore.datn.model.request.GiayRequest;
import luckystore.datn.model.response.GiayResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GiayService {

    List<GiayResponse> getAllActive();

    List<GiayResponse> getAllContains(List<Long> ids);

    GiayResponse addGiay(GiayRequest giayRequest);

}
