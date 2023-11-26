package luckystore.datn.service.impl;

import luckystore.datn.model.response.ProvinceResponse;
import luckystore.datn.repository.ProvinceRepository;
import luckystore.datn.service.ProvincesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProvincesImpl implements ProvincesService {
    @Autowired
    private ProvinceRepository provinceRepo;
    @Override
    public List<ProvinceResponse> getAll() {
        return provinceRepo.getAll();
    }
}
