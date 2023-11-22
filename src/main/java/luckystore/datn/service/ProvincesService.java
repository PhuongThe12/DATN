package luckystore.datn.service;

import luckystore.datn.model.response.DistrictResponse;
import luckystore.datn.model.response.ProvinceResponse;

import java.util.List;

public interface ProvincesService {
    List<ProvinceResponse> getAll();
}
