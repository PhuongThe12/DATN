package luckystore.datn.service;

import luckystore.datn.model.request.SanPhamYeuThichRequest;
import luckystore.datn.model.response.SanPhamYeuThichResponse;

import java.util.List;

public interface SanPhamYeuThichService {

    List<SanPhamYeuThichResponse> getAll();

    SanPhamYeuThichResponse addSanPhamYeuThich(SanPhamYeuThichRequest sanPhamYeuThichRequest);

    void deleteSanPhamYeuThick(Long id);
}
