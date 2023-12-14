package luckystore.datn.service;

import luckystore.datn.entity.GioHangChiTiet;
import luckystore.datn.model.request.BienTheGiayGioHangRequest;
import luckystore.datn.model.request.GioHangChiTietRequest;
import luckystore.datn.model.request.GioHangRequest;
import luckystore.datn.model.response.GioHangChiTietResponse;
import luckystore.datn.model.response.GioHangResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
public interface GioHangService {

    public GioHangChiTietResponse addGiohangChiTiet(GioHangChiTietRequest gioHangChiTietRequest);

    public GioHangResponse getGioHangByKhachHang(Long id);

    void updateSoLuongGioHang(GioHangChiTietRequest gioHangChiTietRequest);

    public void deleteGioHangChiTiet(GioHangChiTietRequest idGioHangChiTiet);

    public void checkSoLuong(Set<BienTheGiayGioHangRequest> bienTheGiayGioHangRequestSet);

    Integer getSoLuong(Long id, Long idGioHang);

    public void deleteAllGioHangChiTiet(List<GioHangChiTietRequest> gioHangChiTietRequestList);

    GioHangResponse addGioHang(GioHangRequest gioHangRequest);

    BigDecimal getTongTienByIdGioHang(Long idGioHang);

}
