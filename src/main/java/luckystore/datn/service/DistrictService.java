package luckystore.datn.service;

import luckystore.datn.model.response.DistrictResponse;
import luckystore.datn.model.response.KhachHangResponse;

import java.util.List;

public interface DistrictService {
    List<DistrictResponse> getAll();

    List<DistrictResponse> getById(String id);
}
