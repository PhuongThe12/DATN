package luckystore.datn.service;

import luckystore.datn.entity.Wards;
import luckystore.datn.model.response.ProvinceResponse;
import luckystore.datn.model.response.WardResponse;

import java.util.List;

public interface WardsService {
    List<WardResponse> getAll();

    List<WardResponse> getById(String id);
}
