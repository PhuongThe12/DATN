package luckystore.datn.service.impl;

import lombok.RequiredArgsConstructor;
import luckystore.datn.entity.*;
import luckystore.datn.exception.ConflictException;
import luckystore.datn.exception.InvalidIdException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.infrastructure.constraints.Config;
import luckystore.datn.infrastructure.constraints.ErrorMessage;
import luckystore.datn.infrastructure.constraints.TrangThaiHoaDon;
import luckystore.datn.infrastructure.security.session.SessionService;
import luckystore.datn.model.request.*;
import luckystore.datn.model.response.*;
import luckystore.datn.model.response.print.HoaDonPrintResponse;
import luckystore.datn.repository.*;
import luckystore.datn.service.HoaDonService;
import luckystore.datn.util.JsonString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
public class HoaDonServiceImpl implements HoaDonService {

    private final HoaDonRepository hoaDonRepository;

    private final NhanVienRepository nhanVienRepository;

    private final KhachHangRepository khachHangRepository;

    private final BienTheGiayRepository bienTheGiayRepository;

    private final KhuyenMaiChiTietRepository khuyenMaiChiTietRepository;

    private final HoaDonChiTietRepository hoaDonChiTietRepository;

    private final DieuKienRepository dieuKienRepository;

    private final HangKhachHangRepository hangKhachHangRepository;

    private final SessionService sessionService;

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
        if(hoaDonSearch.getNgayKetThuc()!=null){
            hoaDonSearch.setNgayKetThuc(hoaDonSearch.getNgayKetThuc().withHour(23).withMinute(59).withSecond(59));
        }
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
    public void updateListHoaDon(List<HoaDonRequest> hoaDonRequestList) {

        List<HoaDon> hoaDonList = new ArrayList<>();
        for (HoaDonRequest hoaDonRequest : hoaDonRequestList) {
            HoaDon hoaDon = new HoaDon();
            hoaDon = getHoaDon(hoaDon, hoaDonRequest);

            hoaDonList.add(hoaDon);
        }
        hoaDonRepository.saveAll(hoaDonList);
    }

    private HoaDon getHoaDon(HoaDon hoaDon, HoaDonRequest hoaDonRequest) {

        NhanVien nhanVien = nhanVienRepository.findById(hoaDonRequest.getNhanVien().getId()).orElseThrow(() -> new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("nhanVien", "Không tồn tại nhân viên này"))));
        KhachHang khachHang = khachHangRepository.findById(hoaDonRequest.getKhachHang().getId()).orElseThrow(() -> new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("khachHang", "Không tồn tại khách hàng này"))));
        hoaDon.setId(hoaDonRequest.getId());
//        hoaDon.setHoaDonGoc(hoaDonRequest.getHoaDonGoc());
        hoaDon.setKhachHang(khachHang);
        hoaDon.setNhanVien(nhanVien);
        hoaDon.setNgayTao(hoaDonRequest.getNgayTao());
        hoaDon.setNgayShip(hoaDonRequest.getNgayShip());
        hoaDon.setNgayNhan(hoaDonRequest.getNgayNhan());
        hoaDon.setNgayThanhToan(hoaDonRequest.getNgayThanhToan());
        hoaDon.setKenhBan(hoaDonRequest.getKenhBan());
        hoaDon.setLoaiHoaDon(hoaDonRequest.getLoaiHoaDon());
        hoaDon.setMaVanDon(hoaDonRequest.getMaVanDon());
        hoaDon.setEmail(hoaDonRequest.getEmail());
        hoaDon.setPhiShip(hoaDonRequest.getPhiShip());
        hoaDon.setSoDienThoaiNhan(hoaDonRequest.getSoDienThoaiNhan());
        hoaDon.setDiaChiNhan(hoaDonRequest.getDiaChiNhan());
        hoaDon.setTrangThai(hoaDonRequest.getTrangThai());
        hoaDon.setGhiChu(hoaDonRequest.getGhiChu());
        hoaDon.setListHoaDonChiTiet(getHoaDonChiTiet(hoaDonRequest.getListHoaDonChiTiet(), hoaDon));

        return hoaDon;
    }

    private HoaDon checkHoaDonChuaThanhToan(Long idHd) {
        HoaDon hoaDon = hoaDonRepository.findById(idHd).orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn"))));
        if (hoaDon.getTrangThai() != 0) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Hóa đơn đã được xử lý. Không hợp lệ")));
        }

        if (hoaDon.getNgayThanhToan() != null && hoaDon.getNgayThanhToan().isAfter(LocalDateTime.now())) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Hóa đơn đang được thanh toán tại một nơi khác vui lòng thử lại")));
        }

        return hoaDon;
    }

    private Set<HoaDonChiTiet> getHoaDonChiTiet(Set<HoaDonChiTietRequest> hoaDonChiTietRequests, HoaDon hoaDon) {
        Set<HoaDonChiTiet> hoaDonChiTiets = new HashSet<>();
        for (HoaDonChiTietRequest h : hoaDonChiTietRequests) {
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            hoaDonChiTiet.setId(h.getId());
            hoaDonChiTiet.setHoaDon(hoaDon);

            BienTheGiay bienTheGiay = bienTheGiayRepository.findById(h.getBienTheGiay().getId()).orElseThrow(() -> new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("bienTheGiay", "Không tồn tại biến thể giày này"))));
            System.out.println(bienTheGiay.toString());
            hoaDonChiTiet.setBienTheGiay(bienTheGiay);
            hoaDonChiTiet.setDonGia(h.getDonGia());
            hoaDonChiTiet.setSoLuong(h.getSoLuong());
            hoaDonChiTiet.setSoLuongTra(h.getSoLuongTra());
            hoaDonChiTiet.setTrangThai(h.getTrangThai());
            hoaDonChiTiet.setGhiChu(h.getGhiChu());
        }
        return hoaDonChiTiets;
    }

    @Override
    public List<HoaDonBanHangResponse> getAllChuaThanhToanBanHang() {
        return hoaDonRepository.getAllChuaThanhToanBanHang();
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
        hoaDon.setTrangThai(TrangThaiHoaDon.CHUA_THANH_TOAN);
        hoaDon.setNgayTao(LocalDateTime.now());

        setNhanVienToHoaDon(hoaDon);

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
            if (hdct.getHoaDon().getTrangThai() != 0) {
                throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Hóa đơn đã được xử lý. Không hợp lệ")));
            }
            if (hdct.getHoaDon().getNgayThanhToan() != null && hdct.getHoaDon().getNgayThanhToan().isAfter(LocalDateTime.now())) {
                throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Hóa đơn đang được thanh toán vui lòng thử lại")));
            }
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

        hdct.setTrangThai(1);
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
        if (hoaDon.getTrangThai() != 0) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Hóa đơn đã được xử lý. Không hợp lệ")));
        }
        if (hoaDon.getNgayThanhToan() != null && hoaDon.getNgayThanhToan().isAfter(LocalDateTime.now())) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Hóa đơn đang được thanh toán vui lòng thử lại")));
        }
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
        hdct.setTrangThai(1);
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
        HoaDon hoaDon = checkHoaDonChuaThanhToan(id);

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
        HoaDon hoaDon = checkHoaDonChuaThanhToan(idHd);

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

        hoaDon.getListHoaDonChiTiet().removeIf(item -> item.getId() != null);
        bienTheGiayRepository.saveAll(bienTheGiays);
        hoaDonRepository.save(hoaDon);
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

    @Transactional
    @Override
    public Long thanhToanHoaDonTaiQuay(HoaDonThanhToanTaiQuayRequest request) {
        HoaDon hoaDon = checkHoaDonChuaThanhToan(Long.valueOf(request.getIdHoaDon()));

        if (hoaDon.getListHoaDonChiTiet().isEmpty()) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Chưa có sản phẩm trong hóa đơn")));
        }
        setNhanVienToHoaDon(hoaDon);
        if (request.getIdDieuKien() == null) {
            hoaDon.setDieuKien(null);
        } else {
            DieuKien dieuKien = dieuKienRepository.findById(request.getIdDieuKien())
                    .orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Đợt giảm giá không tồn tại"))));
            if (dieuKien.getDotGiamGia().getTrangThai() != 1) {
                throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("dieuKienError", "Đợt giảm giá đã hết hạn")));
            }

            if (dieuKien.getDotGiamGia().getNgayKetThuc().isBefore(LocalDateTime.now())) {
                throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("dieuKienError", "Đợt giảm giá đã hết hạn")));
            }

            if(dieuKien.getDotGiamGia().getNgayBatDau().isAfter(LocalDateTime.now())) {
                throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("dieuKienError", "Đợt giảm giá chưa diễn ra")));
            }

            hoaDon.setDieuKien(dieuKien);
        }

        hoaDon.setTienGiam(request.getTienGiam());

        Set<ChiTietThanhToan> chiTietThanhToans = hoaDon.getChiTietThanhToans();
        if (chiTietThanhToans == null) {
            chiTietThanhToans = new HashSet<>();
        } else {
            chiTietThanhToans.removeIf(item -> item.getId() != null);
        }
        if (request.getPhuongThuc() == 1) {
            ChiTietThanhToan chiTietThanhToan = ChiTietThanhToan.builder()
                    .hoaDon(hoaDon)
                    .hinhThucThanhToan(1)
                    .tienThanhToan(request.getTienMat())
                    .trangThai(1)
                    .build();

            chiTietThanhToans.add(chiTietThanhToan);
            hoaDon.setNgayThanhToan(LocalDateTime.now());
        } else if (request.getPhuongThuc() == 3) {
            ChiTietThanhToan chiTietThanhToan = ChiTietThanhToan.builder()
                    .hoaDon(hoaDon)
                    .hinhThucThanhToan(1)
                    .tienThanhToan(request.getTienMat())
                    .trangThai(0)
                    .build();

            chiTietThanhToans.add(chiTietThanhToan);
            hoaDon.setNgayThanhToan(LocalDateTime.now().plusMinutes(10));
        } else if (request.getPhuongThuc() == 2) {
            hoaDon.setNgayThanhToan(LocalDateTime.now().plusMinutes(10));
        }
        if (request.getPhuongThuc() == 1) {
            hoaDon.setTrangThai(TrangThaiHoaDon.DA_THANH_TOAN);
            setUuDai(hoaDon);
            tangHang(hoaDon.getKhachHang(), request.getTienMat());
        }

        hoaDon.setKenhBan(1);
        hoaDon.setChiTietThanhToans(chiTietThanhToans);
        hoaDon.setGhiChu(request.getGhiChu());
        List<Long> idsBienThe = hoaDon.getListHoaDonChiTiet().stream().map(hdct -> hdct.getBienTheGiay().getId()).toList();

        List<BienTheGiayResponse> bienTheGiayResponses = bienTheGiayRepository.bienTheGiay(idsBienThe);
        BigDecimal tongTien = BigDecimal.ZERO;

        for (HoaDonChiTiet hdct : hoaDon.getListHoaDonChiTiet()) {
            boolean tonTai = false;
            for (BienTheGiayResponse btR : bienTheGiayResponses) {
                if (Objects.equals(btR.getId(), hdct.getBienTheGiay().getId())) {
                    hdct.setDonGia(btR.getGiaBan().subtract(btR.getGiaBan().multiply(BigDecimal.valueOf(btR.getKhuyenMai() == null ? 0 : btR.getKhuyenMai()).divide(BigDecimal.valueOf(100)))));
                    tonTai = true;
                }
            }
            if (!tonTai) {
                hdct.setDonGia(hdct.getBienTheGiay().getGiaBan());
            }
            tongTien = tongTien.add(hdct.getDonGia().multiply(BigDecimal.valueOf(hdct.getSoLuong())));
        }

        BigDecimal tienThanhToan = BigDecimal.ZERO;
        if (request.getTienMat() != null) {
            tienThanhToan = tienThanhToan.add(request.getTienMat());
        }
        if (request.getTienChuyenKhoan() != null) {
            tienThanhToan = tienThanhToan.add(request.getTienChuyenKhoan());
        }

        if (tienThanhToan.toBigInteger().compareTo(tongTien.subtract(hoaDon.getTienGiam()).toBigInteger()) != 0 && tongTien.subtract(hoaDon.getTienGiam()).compareTo(BigDecimal.ZERO) >  0) {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("khuyenMaiError", "Một số khuyến mại đã thay đổi vui lòng thử lại")));
        }


        hoaDonRepository.save(hoaDon);

        return hoaDon.getId();
    }

    @Override
    public Long thanhToanHoaDonTaiQuayBanking(HoaDonThanhToanTaiQuayRequest request) {
        HoaDon hoaDon = hoaDonRepository.findById(Long.valueOf(request.getIdHoaDon()))
                .orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn"))));
        if (hoaDon.getTrangThai() != 0) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Hóa đơn đã được thanh toán")));
        }

        setNhanVienToHoaDon(hoaDon);
        Set<ChiTietThanhToan> chiTietThanhToans = hoaDon.getChiTietThanhToans();
        BigDecimal tongTien = BigDecimal.ZERO;
        if (request.getPhuongThuc() == 3) {
            for (ChiTietThanhToan chiTietThanhToan : chiTietThanhToans) {
                if (chiTietThanhToan.getHinhThucThanhToan() == 1) {
                    chiTietThanhToan.setTrangThai(1);
                    tongTien = tongTien.add(chiTietThanhToan.getTienThanhToan());
                }
            }
        }

        ChiTietThanhToan chiTietThanhToan = ChiTietThanhToan.builder()
                .hoaDon(hoaDon)
                .hinhThucThanhToan(2)
                .tienThanhToan(request.getTienChuyenKhoan())
                .maGiaoDich(request.getMaGiaoDich())
                .trangThai(1)
                .build();

        chiTietThanhToans.add(chiTietThanhToan);

        tongTien = tongTien.add(chiTietThanhToan.getTienThanhToan());
        setUuDai(hoaDon);
        tangHang(hoaDon.getKhachHang(), tongTien);

        hoaDon.setTrangThai(TrangThaiHoaDon.DA_THANH_TOAN);
        hoaDon.setChiTietThanhToans(chiTietThanhToans);
        hoaDonRepository.save(hoaDon);

        return hoaDon.getId();
    }

    private void setUuDai(HoaDon hoaDon) {
        if (hoaDon.getKhachHang() != null) {
            if (hoaDon.getKhachHang().getHangKhachHang() != null) {
                hoaDon.setUuDai(hoaDon.getKhachHang().getHangKhachHang().getUuDai());
            }
        }
    }

    private void tangHang(KhachHang khachHang, BigDecimal tongTien) {
        if (khachHang != null) {
            int diemTang = tongTien.divide(Config.moneyPerOnePoint).intValue();
            if (diemTang > 0) {
                int diemHienTai = khachHang.getDiemTichLuy() == null ? 0 : khachHang.getDiemTichLuy();
                khachHang.setDiemTichLuy(diemTang + diemHienTai);
                HangKhachHang hang = hangKhachHangRepository.getMaxByDiemTichLuy(diemTang + diemHienTai, PageRequest.of(0, 1)).get(0);
                khachHang.setHangKhachHang(hang);
                khachHangRepository.save(khachHang);
            }
        }
    }

    @Transactional
    @Override
    public Long datHangTaiQuay(DatHangTaiQuayRequest request) {
        HoaDon hoaDon = checkHoaDonChuaThanhToan(Long.valueOf(request.getId()));

        if (hoaDon.getListHoaDonChiTiet().isEmpty()) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Chưa có sản phẩm trong hóa đơn")));
        }

        setNhanVienToHoaDon(hoaDon);
        Set<ChiTietThanhToan> chiTietThanhToans = hoaDon.getChiTietThanhToans();
        if (chiTietThanhToans == null) {
            chiTietThanhToans = new HashSet<>();
        } else {
            chiTietThanhToans.removeIf(item -> item.getId() != null);
        }
        if (request.getPhuongThuc() == 1) {
            ChiTietThanhToan chiTietThanhToan = ChiTietThanhToan.builder()
                    .hoaDon(hoaDon)
                    .hinhThucThanhToan(1)
                    .tienThanhToan(request.getTongTien())
                    .trangThai(1)
                    .build();

            chiTietThanhToans.add(chiTietThanhToan);
            hoaDon.setNgayThanhToan(LocalDateTime.now());
            hoaDon.setTrangThai(TrangThaiHoaDon.CHO_GIAO_HANG);
        } else {
            ChiTietThanhToan chiTietThanhToan = ChiTietThanhToan.builder()
                    .hoaDon(hoaDon)
                    .hinhThucThanhToan(2)
                    .tienThanhToan(request.getTongTien())
                    .trangThai(0)
                    .build();

            chiTietThanhToans.add(chiTietThanhToan);
            hoaDon.setNgayThanhToan(LocalDateTime.now().plusMinutes(10));
        }

        hoaDon.setTienGiam(request.getTienGiam());
        hoaDon.setKenhBan(1);
        hoaDon.setDiaChiNhan(request.getDiaChiNhan());
        hoaDon.setSoDienThoaiNhan(request.getSdtNhan());
        hoaDon.setPhiShip(request.getTienShip());
        hoaDon.setChiTietThanhToans(chiTietThanhToans);
        hoaDon.setGhiChu(request.getGhiChu());
        List<Long> idsBienThe = hoaDon.getListHoaDonChiTiet().stream().map(hdct -> hdct.getBienTheGiay().getId()).toList();
        List<BienTheGiayResponse> bienTheGiayResponses = bienTheGiayRepository.bienTheGiay(idsBienThe);
        BigDecimal tongTien = BigDecimal.ZERO;

        for (HoaDonChiTiet hdct : hoaDon.getListHoaDonChiTiet()) {
            boolean tonTai = false;
            for (BienTheGiayResponse btR : bienTheGiayResponses) {
                if (Objects.equals(btR.getId(), hdct.getBienTheGiay().getId())) {
                    hdct.setDonGia(btR.getGiaBan().subtract(btR.getGiaBan().multiply(BigDecimal.valueOf(btR.getKhuyenMai() == null ? 0 : btR.getKhuyenMai()).divide(BigDecimal.valueOf(100)))));
                    tonTai = true;
                }
            }
            if (!tonTai) {
                hdct.setDonGia(hdct.getBienTheGiay().getGiaBan());
            }
            tongTien = tongTien.add(hdct.getDonGia().multiply(BigDecimal.valueOf(hdct.getSoLuong())));
        }

        BigDecimal tienThanhToan = BigDecimal.ZERO;
        if (request.getTongTien() != null) {
            tienThanhToan = tienThanhToan.add(request.getTongTien());
        }


        System.out.println("tiền thanh toán: " + tienThanhToan + ": tiền ship: " + hoaDon.getPhiShip());
        System.out.println("Tổng tiền: " + tongTien + ": khuyens mại: " + hoaDon.getTienGiam());
        System.out.println("phai tra: " + (tienThanhToan.subtract(hoaDon.getPhiShip())).toBigInteger() + ": sau : " + tongTien.subtract(hoaDon.getTienGiam()).toBigInteger() + ", soSanh: " + (tienThanhToan.subtract(hoaDon.getPhiShip())).toBigInteger().compareTo(tongTien.subtract(hoaDon.getTienGiam()).toBigInteger()));
        if (tienThanhToan.toBigInteger().compareTo(tongTien.subtract(hoaDon.getTienGiam()).toBigInteger()) != 0 && tongTien.subtract(hoaDon.getTienGiam()).compareTo(BigDecimal.ZERO) >  0) {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("khuyenMaiError", "Một số khuyến mại đã thay đổi vui lòng thử lại")));
        }

        hoaDonRepository.save(hoaDon);
        return hoaDon.getId();
    }

    @Override
    public Long datHangHoaDonTaiQuayBanking(HoaDonThanhToanTaiQuayRequest request) {
        HoaDon hoaDon = hoaDonRepository.findById(Long.valueOf(request.getIdHoaDon()))
                .orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn"))));
        if (hoaDon.getTrangThai() != 0) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Hóa đơn đã được thanh toán")));
        }
        setNhanVienToHoaDon(hoaDon);
        if (hoaDon.getListHoaDonChiTiet().isEmpty()) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Chưa có sản phẩm trong hóa đơn")));
        }

        Set<ChiTietThanhToan> chiTietThanhToans = hoaDon.getChiTietThanhToans();
        if (chiTietThanhToans == null) {
            chiTietThanhToans = new HashSet<>();
        } else if (!chiTietThanhToans.isEmpty()) {
            chiTietThanhToans.removeIf(item -> item.getId() != null);
        }

        ChiTietThanhToan chiTietThanhToan = ChiTietThanhToan.builder()
                .hoaDon(hoaDon)
                .hinhThucThanhToan(2)
                .tienThanhToan(request.getTienChuyenKhoan())
                .maGiaoDich(request.getMaGiaoDich())
                .trangThai(1)
                .build();

        chiTietThanhToans.add(chiTietThanhToan);

        hoaDon.setTrangThai(TrangThaiHoaDon.CHO_GIAO_HANG);
        hoaDon.setChiTietThanhToans(chiTietThanhToans);
        hoaDonRepository.save(hoaDon);

        return hoaDon.getId();
    }

    @Override
    public void cancelBanking(Long id) {
        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn"))));

        if (hoaDon.getTrangThai() != 0) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Hóa đơn đã được thanh toán")));
        }
        setNhanVienToHoaDon(hoaDon);

        if (hoaDon.getNgayThanhToan() != null && hoaDon.getNgayThanhToan().isBefore(LocalDateTime.now())) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Hóa đơn đã được xử lý")));
        }

        hoaDon.setNgayThanhToan(LocalDateTime.now());
        hoaDonRepository.save(hoaDon);
    }

    @Override
    public HoaDonPrintResponse getPrint(Long id) {
        return hoaDonRepository.getPrint(id);
    }

    @Override
    public Page<HoaDonPrintResponse> getAllBySearch(HoaDonSearchP hoaDonSearch) {
        Pageable pageable = PageRequest.of(hoaDonSearch.getCurrentPage() - 1, hoaDonSearch.getPageSize());
        return hoaDonRepository.getAllBySearch(hoaDonSearch, pageable);
    }

    @Transactional
    @Override
    public int xacNhanDonHang(List<Long> ids) {

        if (ids.isEmpty()) {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Bạn chưa chọn hóa đơn")));
        }

        AtomicInteger count = new AtomicInteger();
        List<HoaDon> hoaDons = hoaDonRepository.getAllByIds(ids);
        if (hoaDons.isEmpty()) {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn nào")));
        }

        hoaDons.forEach(hd -> {
            setNhanVienToHoaDon(hd);
            if (hd.getTrangThai() == TrangThaiHoaDon.CHO_XAC_NHAN) {
                hd.setTrangThai(TrangThaiHoaDon.CHO_GIAO_HANG);
                count.getAndIncrement();
            }
        });

        hoaDonRepository.saveAll(hoaDons);

        return count.get();
    }

    @Transactional
    @Override
    public int xacNhanGiaoHang(List<Long> ids) {
        if (ids.isEmpty()) {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Bạn chưa chọn hóa đơn")));
        }

        AtomicInteger count = new AtomicInteger();
        List<HoaDon> hoaDons = hoaDonRepository.getAllByIds(ids);
        if (hoaDons.isEmpty()) {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn nào")));
        }

        hoaDons.forEach(hd -> {
            setNhanVienToHoaDon(hd);
            if (hd.getTrangThai() == TrangThaiHoaDon.CHO_GIAO_HANG) {
                hd.setTrangThai(TrangThaiHoaDon.DANG_GIAO_HANG);
                count.getAndIncrement();
            }
        });

        hoaDonRepository.saveAll(hoaDons);

        return count.get();
    }

    @Transactional
    @Override
    public int hoanThanhDonHang(List<Long> ids) {
        if (ids.isEmpty()) {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Bạn chưa chọn hóa đơn")));
        }

        AtomicInteger count = new AtomicInteger();
        List<HoaDon> hoaDons = hoaDonRepository.getAllByIds(ids);
        if (hoaDons.isEmpty()) {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn nào")));
        }

        hoaDons.forEach(hd -> {
            setNhanVienToHoaDon(hd);
            if (hd.getTrangThai() == TrangThaiHoaDon.DANG_GIAO_HANG) {
                hd.setTrangThai(TrangThaiHoaDon.DA_THANH_TOAN);

                setUuDai(hd);
                BigDecimal tongTien = BigDecimal.ZERO;
                hd.getChiTietThanhToans().forEach(cttt -> {
                    tongTien.add(cttt.getTienThanhToan());
                });
                tangHang(hd.getKhachHang(), tongTien);
                count.getAndIncrement();
            }
        });

        hoaDonRepository.saveAll(hoaDons);

        return count.get();
    }

    @Transactional
    @Override
    public int huyDonHang(List<HuyDonRequest> requests) {
        List<Long> ids = new ArrayList<>();

        Map<Long, String> requestMap = new HashMap<>();
        for (HuyDonRequest request : requests) {
            requestMap.put(request.getId(), request.getGhiChu());
            ids.add(request.getId());
        }

        if (ids.isEmpty()) {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Bạn chưa chọn hóa đơn")));
        }

        AtomicInteger count = new AtomicInteger();
        List<HoaDon> hoaDons = hoaDonRepository.getAllByIds(ids);
        if (hoaDons.isEmpty()) {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn nào")));
        }

        List<BienTheGiay> lstBienThe = new ArrayList<>();
        hoaDons.forEach(hd -> {
            if (hd.getTrangThai() != TrangThaiHoaDon.DA_THANH_TOAN && hd.getTrangThai() != TrangThaiHoaDon.DA_HUY) {
                hd.setTrangThai(TrangThaiHoaDon.DA_HUY);
                hd.setGhiChu(requestMap.get(hd.getId()));
                setNhanVienToHoaDon(hd);
                count.getAndIncrement();
            }
            hd.getListHoaDonChiTiet().forEach(hdct -> {
                BienTheGiay bienThe = new BienTheGiay();
                bienThe.setId(hdct.getBienTheGiay().getId());
                bienThe.setSoLuong(hdct.getSoLuong());
                lstBienThe.add(bienThe);
            });
        });

        List<Long> idsBienThe = lstBienThe.stream().map(BienTheGiay::getId).toList();
        List<BienTheGiay> bienTheGiays = bienTheGiayRepository.getAllByIds(idsBienThe);
        lstBienThe.forEach(bienTheHuy -> {
            bienTheGiays.forEach(bienThe -> {
                if (Objects.equals(bienTheHuy.getId(), bienThe.getId())) {
                    bienThe.setSoLuong(bienThe.getSoLuong() + bienTheHuy.getSoLuong());
                }
            });
        });

        bienTheGiayRepository.saveAll(bienTheGiays);
        hoaDonRepository.saveAll(hoaDons);

        return count.get();
    }

    @Override
    public Page<HoaDonPrintResponse> getAllBySearchOrderNgayShip(HoaDonSearchP hoaDonSearch) {
        Pageable pageable = PageRequest.of(hoaDonSearch.getCurrentPage() - 1, hoaDonSearch.getPageSize());
        return hoaDonRepository.getAllBySearchOrderNgayShip(hoaDonSearch, pageable);
    }

    @Override
    public Page<HoaDonPrintResponse> getAllBySearchOrderNgayThanhToan(HoaDonSearchP hoaDonSearch) {
        Pageable pageable = PageRequest.of(hoaDonSearch.getCurrentPage() - 1, hoaDonSearch.getPageSize());
        return hoaDonRepository.getAllBySearchOrderNgayThanhToan(hoaDonSearch, pageable);
    }

    @Transactional
    @Override
    public boolean traMotPhan(TraMotPhanRequest traMotPhanRequest) {

        if (traMotPhanRequest.getChiTietHoaDons().isEmpty()) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không có sản phẩm trả")));
        }

        HoaDon hoaDon = hoaDonRepository.findById(traMotPhanRequest.getIdHoaDon())
                .orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn"))));

        if (hoaDon.getTrangThai() != 3) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Hóa đơn đã được xử lý")));
        }

        setNhanVienToHoaDon(hoaDon);

        hoaDon.setTienGiam(null);
        hoaDon.setUuDai(null);
        hoaDon.setTrangThai(TrangThaiHoaDon.DA_THANH_TOAN);
        HoaDon hoaDonHoan = new HoaDon();
        Set<HoaDonChiTiet> hoaDonChiTietsHoan = new HashSet<>();

        Set<HoaDonChiTiet> hoaDonChiTiets = hoaDon.getListHoaDonChiTiet();

        hoaDon.getListHoaDonChiTiet().forEach(hdct -> {
            traMotPhanRequest.getChiTietHoaDons().forEach(hdctTra -> {
                if (Objects.equals(hdct.getId(), hdctTra.getId()) && Objects.equals(hdct.getBienTheGiay().getId(), hdctTra.getBienTheGiay().getId()) && hdct.getTrangThai() == 1) {
                    int soLuongConLai = hdct.getSoLuong() - hdctTra.getSoLuongTra();
                    hdct.setSoLuong(soLuongConLai);

                    HoaDonChiTiet newHdct = new HoaDonChiTiet();
                    newHdct.setSoLuong(hdctTra.getSoLuongTra());
                    newHdct.setBienTheGiay(hdct.getBienTheGiay());
                    newHdct.setHoaDon(hdct.getHoaDon());
                    newHdct.setTrangThai(0);
                    newHdct.setDonGia(hdct.getDonGia());
                    newHdct.setGhiChu(hdctTra.getGhiChu());
                    hoaDonChiTiets.add(newHdct);

                    HoaDonChiTiet chiTietHoan = new HoaDonChiTiet();
                    chiTietHoan.setSoLuong(hdctTra.getSoLuongTra());
                    chiTietHoan.setBienTheGiay(hdct.getBienTheGiay());
                    chiTietHoan.setHoaDon(hoaDonHoan);
                    chiTietHoan.setTrangThai(1);
                    chiTietHoan.setDonGia(hdct.getDonGia());
                    chiTietHoan.setGhiChu(hdctTra.getGhiChu());
                    hoaDonChiTietsHoan.add(chiTietHoan);
                }
            });
        });

        hoaDonChiTiets.removeIf(item -> item.getSoLuong() == 0);

        BigDecimal tienConLai = hoaDonChiTiets.stream()
                .filter(hdct -> hdct.getTrangThai() == 1)
                .map(hdct -> BigDecimal.valueOf(hdct.getSoLuong()).multiply(hdct.getDonGia()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        tangHang(hoaDon.getKhachHang(), tienConLai);

        Set<ChiTietThanhToan> chiTietThanhToans = hoaDon.getChiTietThanhToans();
        if (chiTietThanhToans == null) {
            chiTietThanhToans = new HashSet<>();
        }
        BigDecimal tienDaTra = chiTietThanhToans.stream()
                .filter(cttt -> cttt.getTrangThai() == 1)
                .map(ChiTietThanhToan::getTienThanhToan)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        chiTietThanhToans.removeIf(item -> item.getId() != null);
        ChiTietThanhToan chiTietThanhToan = new ChiTietThanhToan();
        chiTietThanhToan.setHoaDon(hoaDon);
        chiTietThanhToan.setTrangThai(1);
        chiTietThanhToan.setHinhThucThanhToan(1);
        chiTietThanhToan.setTienThanhToan(tienConLai);
        chiTietThanhToans.add(chiTietThanhToan);

        if (tienDaTra.compareTo(BigDecimal.ZERO) != 0) {
            ChiTietThanhToan chiTietThanhToanHoan = new ChiTietThanhToan();
            chiTietThanhToanHoan.setTienThanhToan(tienConLai.subtract(tienDaTra));
            chiTietThanhToanHoan.setTrangThai(0);
            chiTietThanhToanHoan.setHoaDon(hoaDonHoan);
            chiTietThanhToanHoan.setHinhThucThanhToan(1);
            hoaDonHoan.setChiTietThanhToans(Collections.singleton(chiTietThanhToanHoan));
        }

        setNhanVienToHoaDon(hoaDonHoan);
        hoaDonHoan.setKhachHang(hoaDon.getKhachHang());
        hoaDonHoan.setListHoaDonChiTiet(hoaDonChiTietsHoan);
        hoaDonHoan.setNgayTao(LocalDateTime.now());
        hoaDonHoan.setTrangThai(TrangThaiHoaDon.HOAN_HANG);
        hoaDonHoan.setGhiChu("Khách chỉ nhận một phần");
        hoaDonRepository.saveAll(Arrays.asList(hoaDonHoan, hoaDon));

        return true;
    }

    @Transactional
    @Override
    public int xacNhanHoanHang(TraMotPhanRequest request) {
        List<Long> ids = new ArrayList<>();

        HoaDon hoaDon = hoaDonRepository.findById(request.getIdHoaDon())
                .orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn"))));

        if (hoaDon.getTrangThai() != TrangThaiHoaDon.HOAN_HANG) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Hóa đơn đã được xử lý. Vui lòng chọn hóa đơn khác")));
        }

        setNhanVienToHoaDon(hoaDon);

        Set<HoaDonChiTiet> hoaDonChiTiets = hoaDon.getListHoaDonChiTiet();
        hoaDonChiTiets.forEach(hdct -> {
            ids.add(hdct.getBienTheGiay().getId());
            hoaDonChiTiets.remove(hdct);
        });

        List<BienTheGiay> bienTheGiays = bienTheGiayRepository.getAllByIds(ids);
        request.getChiTietHoaDons().forEach(hdct -> {
            bienTheGiays.forEach(bienTheGiay -> {
                if (Objects.equals(hdct.getBienTheGiay().getId(), bienTheGiay.getId())) {
                    HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
                    hoaDonChiTiet.setHoaDon(hoaDon);
                    hoaDonChiTiet.setBienTheGiay(bienTheGiay);
                    hoaDonChiTiet.setSoLuong(hdct.getSoLuong());
                    hoaDonChiTiet.setTrangThai(hdct.getTrangThai());
                    hoaDonChiTiet.setDonGia(bienTheGiay.getGiaBan());

                    if (hdct.getTrangThai() == 0) {
                        bienTheGiay.setSoLuongLoi(bienTheGiay.getSoLuongLoi() + hdct.getSoLuong());
                    } else {
                        bienTheGiay.setSoLuong(bienTheGiay.getSoLuong() + hdct.getSoLuong());
                    }

                    hoaDonChiTiets.add(hoaDonChiTiet);
                }
            });
        });

        hoaDon.setTrangThai(TrangThaiHoaDon.DA_HOAN_HANG);

        hoaDonRepository.save(hoaDon);
        bienTheGiayRepository.saveAll(bienTheGiays);

        return 0;
    }

    @Override
    public List<HoaDonResponse> getHoaDonDoiTra(Long id) {
        List<HoaDon> hoaDons = hoaDonRepository.getHoaDonDoiTra(id);
        if (hoaDons.isEmpty()) {
            return null;
        }
        return hoaDons.stream().map(HoaDonResponse::new).toList();
    }

    @Override
    public Page<HoaDonResponse> getPageByIdKhachHang(int page, String searchText, Integer status, Long idKhachHang) {
        return hoaDonRepository.getPageResponseByIdKhachHang(searchText, status, PageRequest.of((page - 1), 9999), idKhachHang);
    }

    private void setNhanVienToHoaDon(HoaDon hoaDon) {
        hoaDon.setNhanVien(sessionService.getAdmintrator());
    }

}
