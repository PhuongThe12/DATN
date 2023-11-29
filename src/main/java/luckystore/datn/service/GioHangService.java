package luckystore.datn.service;

import luckystore.datn.model.request.GioHangChiTietRequest;
import luckystore.datn.model.response.GioHangChiTietResponse;
import luckystore.datn.model.response.GioHangResponse;
import org.springframework.stereotype.Service;

@Service
public interface GioHangService {

    public GioHangChiTietResponse addGiohangChiTiet(GioHangChiTietRequest gioHangChiTietRequest);

    public GioHangResponse getGioHangByKhachHang(Long id);

    void updateSoLuongGioHang(GioHangChiTietRequest gioHangChiTietRequest);

    public void deleteGioHangChiTiet(GioHangChiTietRequest idGioHangChiTiet);

}
