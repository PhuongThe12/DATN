package luckystore.datn.service;

import luckystore.datn.model.request.AddOrderProcuctRequest;
import luckystore.datn.model.request.HoaDonSearch;
import luckystore.datn.model.request.HoaDonThanhToanTaiQuayRequest;
import luckystore.datn.model.response.HoaDonBanHangResponse;
import luckystore.datn.model.response.HoaDonChiTietResponse;
import luckystore.datn.model.response.HoaDonResponse;
import luckystore.datn.model.response.HoaDonYeuCauRespone;
import luckystore.datn.model.response.KhachHangResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HoaDonService {

    List<HoaDonResponse> getAll();

    Page<HoaDonResponse> getPage(int page, String searchText, Integer status);

    Page<HoaDonYeuCauRespone> getPageHoaDonYeuCau(HoaDonSearch hoaDonSearch);
    HoaDonYeuCauRespone getHoaDonYeuCau(Long id);

    HoaDonResponse findById(Long id);

    List<HoaDonBanHangResponse> getAllChuaThanhToan();

    HoaDonBanHangResponse getAllById(Long id);

    HoaDonBanHangResponse createNewHoaDon();

    HoaDonChiTietResponse addProduct(AddOrderProcuctRequest addOrderProcuctRequest);

    HoaDonChiTietResponse addNewHDCT(AddOrderProcuctRequest addOrderProcuctRequest);

    String deleteHoaDon(Long id);

    void deleteAllHoaDonChiTiet(Long idHd);

    KhachHangResponse addKhachHang(AddOrderProcuctRequest addOrderProcuctRequest);

    Long thanhToanHoaDonTaiQuay(HoaDonThanhToanTaiQuayRequest request);
}
