package luckystore.datn.service.impl;

import luckystore.datn.model.response.DistrictResponse;
import luckystore.datn.repository.DistrictRepository;
import luckystore.datn.service.DistrictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DistrictServiceImpl implements DistrictService {
    @Autowired
    private DistrictRepository districtRepo;
    @Override
    public List<DistrictResponse> getAll() {
        return districtRepo.getAll();
    }

    @Override
    public List<DistrictResponse> getById(String id) {
        return districtRepo.findDistrictsByProvinceId(id);
    }
}
