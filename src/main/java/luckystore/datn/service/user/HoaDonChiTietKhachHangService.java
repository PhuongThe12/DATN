package luckystore.datn.service.user;

import luckystore.datn.model.response.HoaDonChiTietResponse;

import java.util.List;

public interface HoaDonChiTietKhachHangService {

    List<HoaDonChiTietResponse> getAllByIdHoaDon(Long id);
}
