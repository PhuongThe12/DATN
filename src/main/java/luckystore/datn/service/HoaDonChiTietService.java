package luckystore.datn.service;
import luckystore.datn.model.response.DonMuaResponse;
import luckystore.datn.model.response.HoaDonChiTietResponse;
import org.springframework.data.domain.Page;

public interface HoaDonChiTietService {
    Page<DonMuaResponse> getAll(int page, Integer status);

}
