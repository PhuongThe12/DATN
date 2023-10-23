package luckystore.datn.service.impl;

import luckystore.datn.model.response.GiayResponse;
import luckystore.datn.repository.GiayRepository;
import luckystore.datn.service.GiayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GiayServiceImpl implements GiayService {

    @Autowired
    private GiayRepository giayRepository;

    @Override
    public List<GiayResponse> getAllActive() {
        return giayRepository.findAllByTrangThai(1);
    }

    @Override
    public List<GiayResponse> getAllContains(List<Long> ids) {
        return giayRepository.findAllContains(ids);
    }


}
