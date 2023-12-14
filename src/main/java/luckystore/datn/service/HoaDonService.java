package luckystore.datn.service;

import luckystore.datn.entity.HoaDon;
import luckystore.datn.model.request.AddOrderProcuctRequest;
import luckystore.datn.model.request.DatHangTaiQuayRequest;
import luckystore.datn.model.request.HoaDonRequest;
import luckystore.datn.model.request.HoaDonSearch;
import luckystore.datn.model.request.HoaDonSearchP;
import luckystore.datn.model.request.HoaDonThanhToanTaiQuayRequest;
import luckystore.datn.model.request.HuyDonRequest;
import luckystore.datn.model.request.TraMotPhanRequest;
import luckystore.datn.model.response.HoaDonBanHangResponse;
import luckystore.datn.model.response.HoaDonChiTietResponse;
import luckystore.datn.model.response.HoaDonResponse;
import luckystore.datn.model.response.HoaDonYeuCauRespone;
import luckystore.datn.model.response.KhachHangResponse;
import luckystore.datn.model.response.print.HoaDonPrintResponse;
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

    void updateListHoaDon(List<HoaDonRequest> hoaDonRequestList);
    List<HoaDonBanHangResponse> getAllChuaThanhToanBanHang();

    HoaDonBanHangResponse getAllById(Long id);

    HoaDonBanHangResponse createNewHoaDon();

    HoaDonChiTietResponse addProduct(AddOrderProcuctRequest addOrderProcuctRequest);

    HoaDonChiTietResponse addNewHDCT(AddOrderProcuctRequest addOrderProcuctRequest);

    String deleteHoaDon(Long id);

    void deleteAllHoaDonChiTiet(Long idHd);

    KhachHangResponse addKhachHang(AddOrderProcuctRequest addOrderProcuctRequest);

    Long thanhToanHoaDonTaiQuay(HoaDonThanhToanTaiQuayRequest request);

    Long thanhToanHoaDonTaiQuayBanking(HoaDonThanhToanTaiQuayRequest request);

    Page<HoaDonResponse> getPageByIdKhachHang(int page, String searchText, Integer status, Long idKhachHang);

    Long datHangTaiQuay(DatHangTaiQuayRequest request);

    Long datHangHoaDonTaiQuayBanking(HoaDonThanhToanTaiQuayRequest request);

    void cancelBanking(Long id);

    HoaDonPrintResponse getPrint(Long id);

    Page<HoaDonPrintResponse> getAllBySearch(HoaDonSearchP hoaDonSearch);

    int xacNhanDonHang(List<Long> ids);

    int xacNhanGiaoHang(List<Long> ids);

    int hoanThanhDonHang(List<Long> ids);

    int huyDonHang(List<HuyDonRequest> requests);

    Page<HoaDonPrintResponse> getAllBySearchOrderNgayShip(HoaDonSearchP hoaDonSearch);

    Page<HoaDonPrintResponse> getAllBySearchOrderNgayThanhToan(HoaDonSearchP hoaDonSearch);

    boolean traMotPhan(TraMotPhanRequest traMotPhanRequest);

    int xacNhanHoanHang(TraMotPhanRequest request);

    List<HoaDonResponse> getHoaDonDoiTra(Long id);

    HoaDonPrintResponse getTraCuuDon(Long maHD, String sdt);
}
