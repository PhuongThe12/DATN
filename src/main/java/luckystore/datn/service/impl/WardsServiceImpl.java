package luckystore.datn.service.impl;

import luckystore.datn.model.response.WardResponse;
import luckystore.datn.repository.WardRepository;
import luckystore.datn.service.WardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class WardsServiceImpl implements WardsService {
    @Autowired
    private WardRepository wardRepo;

    @Override
    public List<WardResponse> getAll() {
        return wardRepo.getAll();
    }

    @Override
    public List<WardResponse> getById(String id) {
        return wardRepo.findWardByProvinceId(id);
    }
}
