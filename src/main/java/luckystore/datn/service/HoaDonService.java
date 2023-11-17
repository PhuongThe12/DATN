package luckystore.datn.service;

import luckystore.datn.model.request.AddOrderProcuctRequest;
import luckystore.datn.model.request.HoaDonSearch;
import luckystore.datn.model.response.HoaDonBanHangResponse;
import luckystore.datn.model.response.HoaDonResponse;
import luckystore.datn.model.response.HoaDonYeuCauRespone;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HoaDonService {

    List<HoaDonResponse> getAll();

    Page<HoaDonResponse> getPage(int page, String searchText, Integer status);

    Page<HoaDonYeuCauRespone> getPageHoaDonYeuCau(int page, HoaDonSearch hoaDonSearch);

    HoaDonResponse findById(Long id);

    List<HoaDonBanHangResponse> getAllChuaThanhToan();

    HoaDonBanHangResponse createNewHoaDon();

    HoaDonBanHangResponse addProduct(AddOrderProcuctRequest addOrderProcuctRequest);

    String deleteHoaDon(Long id);

    void deleteAllHoaDonChiTiet(Long idHd);
}
