package luckystore.datn.service.impl;

import jakarta.transaction.Transactional;
import luckystore.datn.constraints.ErrorMessage;
import luckystore.datn.constraints.TrangThaiHoaDon;
import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.entity.ChiTietThanhToan;
import luckystore.datn.entity.DieuKien;
import luckystore.datn.entity.HoaDon;
import luckystore.datn.entity.HoaDonChiTiet;
import luckystore.datn.entity.KhachHang;
import luckystore.datn.exception.ConflictException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.model.request.AddOrderProcuctRequest;
import luckystore.datn.model.request.HoaDonSearch;
import luckystore.datn.model.request.HoaDonThanhToanTaiQuayRequest;
import luckystore.datn.model.response.BienTheGiayResponse;
import luckystore.datn.model.response.HoaDonBanHangResponse;
import luckystore.datn.model.response.HoaDonChiTietResponse;
import luckystore.datn.model.response.HoaDonResponse;
import luckystore.datn.model.response.HoaDonYeuCauRespone;
import luckystore.datn.model.response.KhachHangResponse;
import luckystore.datn.model.response.KhuyenMaiChiTietResponse;
import luckystore.datn.repository.BienTheGiayRepository;
import luckystore.datn.repository.DieuKienRepository;
import luckystore.datn.repository.HoaDonChiTietRepository;
import luckystore.datn.repository.HoaDonRepository;
import luckystore.datn.repository.KhachHangRepository;
import luckystore.datn.repository.KhuyenMaiChiTietRepository;
import luckystore.datn.service.HoaDonService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class HoaDonServiceImpl implements HoaDonService {

    @Autowired
    HoaDonRepository hoaDonRepository;
    @Autowired
    BienTheGiayRepository bienTheGiayRepository;

    @Autowired
    KhuyenMaiChiTietRepository khuyenMaiChiTietRepository;
    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;
    @Autowired
    private KhachHangRepository khachHangRepository;
    @Autowired
    private DieuKienRepository dieuKienRepository;

    @Override
    public List<HoaDonResponse> getAll() {
        return hoaDonRepository.findAllResponse();
    }

    @Override
    public Page<HoaDonResponse> getPage(int page, String searchText, Integer status) {
        return hoaDonRepository.getPageResponse(searchText, status, PageRequest.of((page - 1), 5));
    }

    @Override
    public Page<HoaDonYeuCauRespone> getPageHoaDonYeuCau(HoaDonSearch hoaDonSearch) {
        Pageable pageable = PageRequest.of(hoaDonSearch.getCurrentPage() - 1, hoaDonSearch.getPageSize());
        return hoaDonRepository.getPageHoaDonYeuCauResponse(hoaDonSearch, pageable);
    }

    @Override
    public HoaDonYeuCauRespone getHoaDonYeuCau(Long id) {
        return hoaDonRepository.getHoaDonYeuCauResponse(id);
    }

    @Override
    public HoaDonResponse findById(Long id) {
        return new HoaDonResponse(hoaDonRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

    @Override
    public List<HoaDonBanHangResponse> getAllChuaThanhToan() {
        return hoaDonRepository.getAllChuaThanhToan();
    }

    @Override
    public HoaDonBanHangResponse getAllById(Long id) {
        List<HoaDonBanHangResponse> list = hoaDonRepository.getAllById(id);

        if (list.isEmpty()) {
            throw new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn")));
        }

        Map<Long, HoaDonBanHangResponse> responseMap = new HashMap<>();

        int count = 0;
        for (HoaDonBanHangResponse hd : list) {
            if (responseMap.containsKey(hd.getId())) {
                responseMap.get(hd.getId()).getHoaDonChiTiets().add(hd.getHoaDonChiTiets().get(0));
            } else {
                responseMap.put(hd.getId(), hd);
                count++;
            }
        }

        List<Long> idBienThes = new ArrayList<>();

        if (count > 1) {
            throw new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn")));
        }
        HoaDonBanHangResponse hoaDonBanHangResponse = new ArrayList<>(responseMap.values()).get(0);
        for (HoaDonChiTietResponse hc : hoaDonBanHangResponse.getHoaDonChiTiets()) {
            idBienThes.add(hc.getBienTheGiay().getId());
        }

        List<KhuyenMaiChiTietResponse> khuyenMaiChiTiets = khuyenMaiChiTietRepository.getAllByIdBienThe(idBienThes);
        for (KhuyenMaiChiTietResponse kmct : khuyenMaiChiTiets) {
            for (HoaDonChiTietResponse hoaDonChiTiet : hoaDonBanHangResponse.getHoaDonChiTiets()) {
                if (hoaDonChiTiet.getBienTheGiay().getId().equals(kmct.getBienTheGiayResponsel().getId())) {
                    hoaDonChiTiet.getBienTheGiay().setKhuyenMai(kmct.getPhanTramGiam());
                }
            }

        }

        KhachHang khachHang = khachHangRepository.findByHDId(id);
        hoaDonBanHangResponse.setKhachHangRestponse(new KhachHangResponse(khachHang));

        return hoaDonBanHangResponse;
    }


    @Override
    public HoaDonBanHangResponse createNewHoaDon() {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setTrangThai(0);
        hoaDon.setNgayTao(LocalDateTime.now());

        hoaDon = hoaDonRepository.save(hoaDon);
        return new HoaDonBanHangResponse(hoaDon, hoaDon.getTrangThai());
    }

    @Override
    public HoaDonChiTietResponse addProduct(AddOrderProcuctRequest addOrderProcuctRequest) {
        Optional<HoaDonChiTiet> hoaDonChiTiet = hoaDonChiTietRepository.findById(addOrderProcuctRequest.getIdHoaDon());

        BienTheGiay bienTheGiay = bienTheGiayRepository.findById(addOrderProcuctRequest.getIdGiay()).orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy giày"))));

        int soLuongCu;

        HoaDonChiTiet hdct;
        if (hoaDonChiTiet.isPresent()) {
            hdct = hoaDonChiTiet.get();
            if (addOrderProcuctRequest.getSoLuong() == 0) {
                hoaDonChiTietRepository.deleteById(hdct.getId());
                return HoaDonChiTietResponse.builder().id(-1L).build();
            } else {
                soLuongCu = hdct.getSoLuong();
                hdct.setSoLuong(addOrderProcuctRequest.getSoLuong());
            }
        } else {
            throw new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn")));
        }

        bienTheGiay.setSoLuong(bienTheGiay.getSoLuong() + soLuongCu - addOrderProcuctRequest.getSoLuong());
        if (bienTheGiay.getSoLuong() < 0) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Số lượng đã hết")));
        }
        bienTheGiayRepository.save(bienTheGiay);

        hdct = hoaDonChiTietRepository.save(hdct);

        HoaDonChiTietResponse hoaDonChiTietResponse = new HoaDonChiTietResponse(hdct);
        KhuyenMaiChiTietResponse khuyenMaiChiTiet = khuyenMaiChiTietRepository.getByIdBienThe(bienTheGiay.getId());
        if (khuyenMaiChiTiet != null) {
            hoaDonChiTietResponse.getBienTheGiay().setKhuyenMai(khuyenMaiChiTiet.getPhanTramGiam());
        }

        return hoaDonChiTietResponse;
    }

    @Override
    public HoaDonChiTietResponse addNewHDCT(AddOrderProcuctRequest addOrderProcuctRequest) {
        HoaDon hoaDon = hoaDonRepository.findById(addOrderProcuctRequest.getIdHoaDon()).orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn"))));
        BienTheGiay bienTheGiay = bienTheGiayRepository.findById(addOrderProcuctRequest.getIdGiay()).orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy giày"))));

        for (HoaDonChiTiet hoaDonChiTiet : hoaDon.getListHoaDonChiTiet()) {
            if (Objects.equals(hoaDonChiTiet.getBienTheGiay().getId(), bienTheGiay.getId())) {
                throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không hợp lệ")));
            }
        }

        HoaDonChiTiet hdct = new HoaDonChiTiet();
        hdct.setSoLuong(addOrderProcuctRequest.getSoLuong());
        bienTheGiay.setSoLuong(bienTheGiay.getSoLuong() - addOrderProcuctRequest.getSoLuong());
        if (bienTheGiay.getSoLuong() < 0) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Số lượng đã hết")));
        }
        bienTheGiayRepository.save(bienTheGiay);

        hdct.setHoaDon(hoaDon);
        hdct.setBienTheGiay(bienTheGiay);
        hdct = hoaDonChiTietRepository.save(hdct);

        HoaDonChiTietResponse hoaDonChiTietResponse = new HoaDonChiTietResponse(hdct.getId(), hoaDon.getId(), new BienTheGiayResponse(bienTheGiay), hdct.getSoLuong());
        KhuyenMaiChiTietResponse khuyenMaiChiTiet = khuyenMaiChiTietRepository.getByIdBienThe(bienTheGiay.getId());
        if (khuyenMaiChiTiet != null) {
            hoaDonChiTietResponse.getBienTheGiay().setKhuyenMai(khuyenMaiChiTiet.getPhanTramGiam());
        }

        return hoaDonChiTietResponse;
    }

    @Override
    @Transactional
    public String deleteHoaDon(Long id) {
        HoaDon hoaDon = hoaDonRepository.findById(id).orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn"))));

        Set<HoaDonChiTiet> lstHoaDonCT = hoaDon.getListHoaDonChiTiet();
        List<BienTheGiay> bienTheGiays = new ArrayList<>();
        for (HoaDonChiTiet hd : lstHoaDonCT) {
            BienTheGiay bienThe = bienTheGiayRepository.findById(hd.getBienTheGiay().getId()).orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn"))));
            bienThe.setSoLuong(bienThe.getSoLuong() + hd.getSoLuong());
            bienTheGiays.add(bienThe);
        }
        bienTheGiayRepository.saveAll(bienTheGiays);

        hoaDonRepository.delete(hoaDon);
        return "Xóa thành công";
    }

    @Override
    public void deleteAllHoaDonChiTiet(Long idHd) {
        HoaDon hoaDon = hoaDonRepository.findById(idHd).orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn"))));

        Set<HoaDonChiTiet> lstHoaDonCT = hoaDon.getListHoaDonChiTiet();
        List<BienTheGiay> bienTheGiays = new ArrayList<>();
        for (HoaDonChiTiet hd : lstHoaDonCT) {
            BienTheGiay bienThe = bienTheGiayRepository.findById(hd.getBienTheGiay().getId()).orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn"))));
            bienThe.setSoLuong(bienThe.getSoLuong() + hd.getSoLuong());
            bienTheGiays.add(bienThe);
            if (bienThe.getSoLuong() < 0) {
                throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Số lượng đã hết")));
            }
        }
        bienTheGiayRepository.saveAll(bienTheGiays);

    }

    @Override
    public KhachHangResponse addKhachHang(AddOrderProcuctRequest addOrderProcuctRequest) {
        KhachHang khachHang = khachHangRepository.findById(addOrderProcuctRequest.getIdGiay())
                .orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy khách hàng"))));

        HoaDon hoaDon = hoaDonRepository.findById(addOrderProcuctRequest.getIdHoaDon())
                .orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn"))));

        hoaDon.setKhachHang(khachHang);
        hoaDonRepository.save(hoaDon);
        return new KhachHangResponse(khachHang);
    }

    @Override
    public Long thanhToanHoaDonTaiQuay(HoaDonThanhToanTaiQuayRequest request) {
        HoaDon hoaDon = hoaDonRepository.findById(request.getIdHoaDon())
                .orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn"))));

        if (hoaDon.getTrangThai() != 0) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Hóa đơn đã được thanh toán")));
        }

        if (request.getIdDieuKien() != null) {
            DieuKien dieuKien = dieuKienRepository.findById(request.getIdDieuKien())
                    .orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Điều kiện không tồn tại"))));
            hoaDon.setDieuKien(dieuKien);
        }

        hoaDon.setTrangThai(TrangThaiHoaDon.DA_THANH_TOAN);
        hoaDon.setTienGiam(request.getTienGiam());

        Set<ChiTietThanhToan> chiTietThanhToans = new HashSet<>();
        if (request.getPhuongThuc() == 1 || request.getPhuongThuc() == 3) {
            ChiTietThanhToan chiTietThanhToan = ChiTietThanhToan.builder()
                    .hoaDon(hoaDon)
                    .hinhThucThanhToan(1)
                    .tienThanhToan(request.getTienMat())
                    .trangThai(1)
                    .build();

            chiTietThanhToans.add(chiTietThanhToan);
        } else if (request.getPhuongThuc() == 2 || request.getPhuongThuc() == 3) {
            ChiTietThanhToan chiTietThanhToan = ChiTietThanhToan.builder()
                    .hoaDon(hoaDon)
                    .hinhThucThanhToan(2)
                    .tienThanhToan(request.getTienChuyenKhoan())
                    .trangThai(1)
                    .build();

            chiTietThanhToans.add(chiTietThanhToan);

        }

        hoaDon.setKenhBan(1);
        hoaDon.setChiTietThanhToans(chiTietThanhToans);
        hoaDon.setGhiChu(request.getGhiChu());
        hoaDonRepository.save(hoaDon);

        return hoaDon.getId();
    }
}
