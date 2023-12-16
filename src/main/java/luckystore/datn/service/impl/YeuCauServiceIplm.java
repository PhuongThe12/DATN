package luckystore.datn.service.impl;

import luckystore.datn.entity.*;
import luckystore.datn.infrastructure.constraints.*;
import luckystore.datn.infrastructure.security.session.SessionService;
import luckystore.datn.model.request.YeuCauChiTietRequest;
import luckystore.datn.model.request.YeuCauRequest;
import luckystore.datn.model.request.YeuCauSearch;
import luckystore.datn.model.response.HoaDonYeuCauRespone;
import luckystore.datn.model.response.YeuCauResponse;
import luckystore.datn.repository.*;
import luckystore.datn.service.ImageHubService;
import luckystore.datn.service.YeuCauChiTietService;
import luckystore.datn.service.YeuCauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Component
public class YeuCauServiceIplm implements YeuCauService {

    private final SessionService sessionService;
    private final YeuCauRepository yeuCauRepository;
    private final YeuCauChiTietRepository yeuCauChiTietRepository;
    private final HoaDonRepository hoaDonRepository;
    private final HoaDonChiTietRepository hoaDonChiTietRepository;
    private final BienTheGiayRepository bienTheGiayRepository;
    private final LyDoRepository lyDoRepository;
    private final KhachHangRepository khachHangRepository;
    private final NhanVienRepository nhanVienRepository;
    private final YeuCauChiTietService yeuCauChiTietService;
    private final ImageHubService imageHubService;

    @Autowired
    public YeuCauServiceIplm(SessionService sessionService, YeuCauRepository yeuCauRepo, YeuCauChiTietRepository yeuCauChiTietRepository, HoaDonRepository hoaDonRepository, HoaDonChiTietRepository hoaDonChiTietRepository, BienTheGiayRepository bienTheGiayRepository, LyDoRepository lyDoRepository, KhachHangRepository khachHangRepository, KhachHangRepository khachHangRepository1, NhanVienRepository nhanVienRepository, YeuCauChiTietService yeuCauChiTietService, ImageHubService imageHubService) {
        this.sessionService = sessionService;
        this.yeuCauRepository = yeuCauRepo;
        this.yeuCauChiTietRepository = yeuCauChiTietRepository;
        this.hoaDonRepository = hoaDonRepository;
        this.hoaDonChiTietRepository = hoaDonChiTietRepository;
        this.bienTheGiayRepository = bienTheGiayRepository;
        this.lyDoRepository = lyDoRepository;
        this.khachHangRepository = khachHangRepository1;
        this.nhanVienRepository = nhanVienRepository;
        this.yeuCauChiTietService = yeuCauChiTietService;
        this.imageHubService = imageHubService;
    }

    public List<YeuCauResponse> getAll() {
        return yeuCauRepository.finAllResponse();
    }

    @Override
    public YeuCauResponse addYeuCau(YeuCauRequest yeuCauRequest) {
        HoaDon hoaDon = hoaDonRepository.findById(yeuCauRequest.getHoaDon()).orElse(null);
        YeuCau yeuCauSaved = new YeuCau(yeuCauRequest, hoaDon, null, LocalDateTime.now(), LocalDateTime.now());

        //người tạo
        Long nguoiTao = sessionService.getAdmintrator().getId() == null ? null : sessionService.getAdmintrator().getId();
        yeuCauSaved.setNguoiTao(nguoiTao);

        List<YeuCauChiTiet> listYeuCauChiTiet = new ArrayList<>();
        for (YeuCauChiTietRequest ycctRequest : yeuCauRequest.getListYeuCauChiTiet()) {
            LyDo lyDo = lyDoRepository.findById(ycctRequest.getLyDo()).orElse(null);
            BienTheGiay bienTheGiay = ycctRequest.getBienTheGiay() != null ? bienTheGiayRepository.findById(ycctRequest.getBienTheGiay()).orElse(null) : null;
            if (bienTheGiay != null) {
                bienTheGiay.setSoLuong(bienTheGiay.getSoLuong() - 1);
                bienTheGiayRepository.save(bienTheGiay).getSoLuong();
            }
            HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(ycctRequest.getHoaDonChiTiet()).orElse(null);
            YeuCauChiTiet ycct = new YeuCauChiTiet(ycctRequest, hoaDonChiTiet, bienTheGiay, lyDo, yeuCauSaved);
            listYeuCauChiTiet.add(ycct);
        }
        yeuCauSaved.setListYeuCauChiTiet(listYeuCauChiTiet);
        return new YeuCauResponse(yeuCauRepository.save(yeuCauSaved));
    }

    @Override
    public YeuCauResponse confirmYeuCau(YeuCauRequest yeuCauRequest) {
        Long nguoiSuaAndThucHien = sessionService.getAdmintrator().getId() == null ? null : sessionService.getAdmintrator().getId();
        YeuCau yeuCauGoc = yeuCauRepository.findById(yeuCauRequest.getId()).orElse(null);

        YeuCau yeuCauSaved = YeuCau.builder().id(yeuCauGoc.getId()).hoaDon(yeuCauGoc.getHoaDon()).nguoiThucHien(nguoiSuaAndThucHien).trangThai(TrangThaiYeuCau.DA_XAC_NHAN).ngayTao(yeuCauGoc.getNgayTao()).ngaySua(LocalDateTime.now()).ghiChu(yeuCauRequest.getGhiChu()).thongTinNhanHang(yeuCauGoc.getThongTinNhanHang()).phiShip(yeuCauGoc.getPhiShip()).nguoiTao(yeuCauGoc.getNguoiTao()).nguoiSua(nguoiSuaAndThucHien).build();


        List<YeuCauChiTiet> listYeuCauChiTiet = new ArrayList<>();


        for (YeuCauChiTietRequest ycctRequest : yeuCauRequest.getListYeuCauChiTiet()) {

            HoaDonChiTiet hoaDonChiTietUpdate = hoaDonChiTietRepository.findById(ycctRequest.getHoaDonChiTiet()).orElse(null);
            BienTheGiay bienTheGiayTra = bienTheGiayRepository.findById(ycctRequest.getBienTheGiayTra()).orElse(null);
            BienTheGiay bienTheGiayDoi = ycctRequest.getBienTheGiay() == null ? null : bienTheGiayRepository.findById(ycctRequest.getBienTheGiay()).orElse(null);
            LyDo lyDoUpdate = lyDoRepository.findById(ycctRequest.getLyDo()).orElse(null);

            if (ycctRequest.getLoaiYeuCauChiTiet() != 3) { // Nếu không bị hủy yêu câù chi tiết nào

                //Kiểm tra giày lỗi
                if (ycctRequest.getTinhTrangSanPham() == false) { //giày không lỗi
                    bienTheGiayTra.setSoLuong(bienTheGiayTra.getSoLuong() + 1);
                    bienTheGiayRepository.save(bienTheGiayTra);
                } else if (ycctRequest.getTinhTrangSanPham() == true) { // giày lỗi
                    bienTheGiayTra.setSoLuongLoi(bienTheGiayTra.getSoLuongLoi() + 1);
                    bienTheGiayRepository.save(bienTheGiayTra);
                }

                if (ycctRequest.getLoaiYeuCauChiTiet() == 1) { // Đổi Giày
                    bienTheGiayDoi.setSoLuong(bienTheGiayDoi.getSoLuong() - 1);//trừ sản phẩm đổi trong db
                    YeuCauChiTiet yeuCauChiTietSaved = YeuCauChiTiet.builder().id(ycctRequest.getId()).yeuCau(yeuCauSaved).hoaDonChiTiet(hoaDonChiTietUpdate).bienTheGiay(bienTheGiayDoi).lyDo(lyDoUpdate).trangThai(TrangThaiYeuCauChiTiet.DEFAULT).ghiChu(ycctRequest.getGhiChu()).loaiYeuCauChiTiet(ycctRequest.getLoaiYeuCauChiTiet()).tinhTrangSanPham(ycctRequest.getTinhTrangSanPham()).tienGiam(ycctRequest.getTienGiam()).thanhTien(ycctRequest.getThanhTien()).build();
                    listYeuCauChiTiet.add(yeuCauChiTietSaved);
                } else if (ycctRequest.getLoaiYeuCauChiTiet() == 2) { //nếu trả

                    if (bienTheGiayDoi != null) { //Trả hàng - Từ chối đổi
                        YeuCauChiTiet yeuCauChiTietSaved = YeuCauChiTiet.builder().id(ycctRequest.getId()).yeuCau(yeuCauSaved).hoaDonChiTiet(hoaDonChiTietUpdate).bienTheGiay(bienTheGiayDoi).lyDo(lyDoUpdate).trangThai(TrangThaiYeuCauChiTiet.HUY_DOI).ghiChu(ycctRequest.getGhiChu()).loaiYeuCauChiTiet(ycctRequest.getLoaiYeuCauChiTiet()).tinhTrangSanPham(ycctRequest.getTinhTrangSanPham()).tienGiam(ycctRequest.getTienGiam()).thanhTien(ycctRequest.getThanhTien()).build();
                        listYeuCauChiTiet.add(yeuCauChiTietSaved);
                    } else { //Trả bình thường
                        YeuCauChiTiet yeuCauChiTietSaved = YeuCauChiTiet.builder().id(ycctRequest.getId()).yeuCau(yeuCauSaved).hoaDonChiTiet(hoaDonChiTietUpdate).bienTheGiay(null).lyDo(lyDoUpdate).trangThai(TrangThaiYeuCauChiTiet.DEFAULT).ghiChu(ycctRequest.getGhiChu()).loaiYeuCauChiTiet(ycctRequest.getLoaiYeuCauChiTiet()).tinhTrangSanPham(ycctRequest.getTinhTrangSanPham()).tienGiam(ycctRequest.getTienGiam()).thanhTien(ycctRequest.getThanhTien()).build();
                        listYeuCauChiTiet.add(yeuCauChiTietSaved);
                    }

                }
            } else { // từ chối trả = từ chối yêu cầu
                hoaDonChiTietUpdate.setSoLuongTra(hoaDonChiTietUpdate.getSoLuongTra() - 1);
                hoaDonChiTietRepository.save(hoaDonChiTietUpdate);
                YeuCauChiTiet yeuCauChiTietSaved = YeuCauChiTiet.builder().id(ycctRequest.getId()).yeuCau(yeuCauSaved).hoaDonChiTiet(hoaDonChiTietUpdate).bienTheGiay(bienTheGiayDoi).lyDo(lyDoUpdate).trangThai(TrangThaiYeuCauChiTiet.HUY_TRA).ghiChu(ycctRequest.getGhiChu()).loaiYeuCauChiTiet(ycctRequest.getLoaiYeuCauChiTiet()).tinhTrangSanPham(ycctRequest.getTinhTrangSanPham()).tienGiam(ycctRequest.getTienGiam()).thanhTien(ycctRequest.getThanhTien()).build();
                listYeuCauChiTiet.add(yeuCauChiTietSaved);
            }
        }

        yeuCauSaved.setListYeuCauChiTiet(listYeuCauChiTiet);
        YeuCau yeuCau = yeuCauRepository.save(yeuCauSaved);
        if (yeuCau != null && !yeuCau.getListYeuCauChiTiet().isEmpty()) {
            taoHoaDon(yeuCau, listYeuCauChiTiet);//đồng thời tạo hóa đơn đổi/trả
        }
        return null;
    }


    private HoaDon taoHoaDon(YeuCau yeuCau, List<YeuCauChiTiet> listYeuCauChiTiet) {
        HoaDon hoaDon = yeuCau.getHoaDon();
        KhachHang khachHang = hoaDon.getKhachHang();

        // người tạo
        Long nguoiTao = sessionService.getAdmintrator().getId() == null ? null : sessionService.getAdmintrator().getId();
        NhanVien nhanVien = nguoiTao != null ? nhanVienRepository.findById(nguoiTao).orElse(null) : null;
        LocalDateTime ngayHienTai = LocalDateTime.now();
        BigDecimal tienGiam = BigDecimal.ZERO;
        BigDecimal tienThanhToan = BigDecimal.ZERO;

        Set<HoaDonChiTiet> listHoaDonChiTietTra = new HashSet<>();
        Set<HoaDonChiTiet> listHoaDonChiTietDoi = new HashSet<>();
        Set<ChiTietThanhToan> chiTietThanhToans = new HashSet<>();

        Map<BienTheGiay, HoaDonChiTiet> mapHoaDonChiTietDoi = new HashMap<>();
        Map<BienTheGiay, HoaDonChiTiet> mapHoaDonChiTietTra = new HashMap<>();

        for (YeuCauChiTiet ycct : listYeuCauChiTiet) {

            if (ycct.getLoaiYeuCauChiTiet() == 1) { //tạo hóa đơn chi tiết đổi     loại =1

                tienGiam = tienGiam.add(ycct.getTienGiam() == null ? BigDecimal.ZERO : ycct.getTienGiam());
                BigDecimal donGia = ycct.getBienTheGiay().getGiaBan().subtract(ycct.getTienGiam() == null ? BigDecimal.ZERO : ycct.getTienGiam());

                HoaDonChiTiet hoaDonChiTietDoi = HoaDonChiTiet.builder()
                        .bienTheGiay(ycct.getBienTheGiay())
                        .donGia(donGia)
                        .soLuong(1)
                        .trangThai(1)
                        .soLuongTra(0)
                        .build();
                BienTheGiay key = hoaDonChiTietDoi.getBienTheGiay();
                if (mapHoaDonChiTietDoi.containsKey(key)) {
                    HoaDonChiTiet existing = mapHoaDonChiTietDoi.get(key);
                    existing.setSoLuong(existing.getSoLuong() + 1);
                } else {
                    mapHoaDonChiTietDoi.put(key, hoaDonChiTietDoi);
                    listHoaDonChiTietDoi.add(hoaDonChiTietDoi);
                }

                HoaDonChiTiet hoaDonChiTietTra = HoaDonChiTiet.builder()
                        .bienTheGiay(ycct.getHoaDonChiTiet().getBienTheGiay())
                        .donGia(ycct.getThanhTien())
                        .soLuong(1)
                        .trangThai(0)
                        .soLuongTra(0)
                        .build();

                key = hoaDonChiTietTra.getBienTheGiay();
                if (mapHoaDonChiTietTra.containsKey(key)) {
                    HoaDonChiTiet existing = mapHoaDonChiTietTra.get(key);
                    existing.setSoLuong(existing.getSoLuong() + 1);
                } else {
                    mapHoaDonChiTietTra.put(key, hoaDonChiTietTra);
                    listHoaDonChiTietTra.add(hoaDonChiTietTra);
                    listHoaDonChiTietDoi.add(hoaDonChiTietTra);
                }

            } else if (ycct.getLoaiYeuCauChiTiet() == 2) { // tạo hóa đơn chi tiết trả

                HoaDonChiTiet hoaDonChiTietTra = HoaDonChiTiet.builder()
                        .bienTheGiay(ycct.getHoaDonChiTiet().getBienTheGiay())
                        .donGia(ycct.getThanhTien())
                        .soLuong(1)
                        .trangThai(0)
                        .ghiChu(ycct.getLyDo().getTen())
                        .soLuongTra(0)
                        .build();

                BienTheGiay key = hoaDonChiTietTra.getBienTheGiay();
                if (mapHoaDonChiTietTra.containsKey(key)) {
                    HoaDonChiTiet existing = mapHoaDonChiTietTra.get(key);
                    existing.setSoLuong(existing.getSoLuong() + 1);
                } else {
                    mapHoaDonChiTietTra.put(key, hoaDonChiTietTra);
                    listHoaDonChiTietTra.add(hoaDonChiTietTra);
                }

            }
        }

        if (!listHoaDonChiTietTra.isEmpty()) { //tạo hóa đơn trả

            HoaDon hoaDonTra = HoaDon.builder()
                    .hoaDonGoc(hoaDon.getId()) //hóa đơn gốc
                    .khachHang(khachHang)
                    .nhanVien(nhanVien)
                    .email(khachHang.getEmail())
                    .ngayTao(ngayHienTai)
                    .kenhBan(KenhBan.ONLINE)
                    .trangThai(TrangThaiHoaDon.CHUA_THANH_TOAN)
                    .loaiHoaDon(LoaiHoaDon.HOA_DON_TRA)
                    .ghiChu("" + yeuCau.getId())
                    .uuDai(0)
                    .tienGiam(BigDecimal.ZERO)
                    .build();

            for (HoaDonChiTiet hoaDonChiTietTra : listHoaDonChiTietTra) {
                hoaDonChiTietTra.setHoaDon(hoaDonTra);
            }

            hoaDonTra.setListHoaDonChiTiet(listHoaDonChiTietTra);

            hoaDonRepository.save(hoaDonTra); // save hóa đơn trả

        }

        if (!listHoaDonChiTietDoi.isEmpty()) { //tạo hóa đơn đổi
            String[] thongTinNhanHang = yeuCau.getThongTinNhanHang().split("-");

            String tenNguoiNhan = thongTinNhanHang[0]; // Phần 1 (tên)
            String sdtNhan = thongTinNhanHang[1]; // Phần 2 (số điện thoại)
            String diaChiNhan = thongTinNhanHang[2]; // Phần 3 (địa chỉ)

            HoaDon hoaDonDoi = HoaDon.builder()
                    .hoaDonGoc(hoaDon.getId())
                    .khachHang(khachHang)
                    .nhanVien(nhanVien)
                    .email(khachHang.getEmail())
                    .ngayTao(ngayHienTai)
                    .phiShip(yeuCau.getPhiShip())
                    .soDienThoaiNhan(sdtNhan)
                    .diaChiNhan(diaChiNhan)
                    .kenhBan(KenhBan.ONLINE)
                    .trangThai(TrangThaiHoaDon.CHUA_THANH_TOAN)
                    .ghiChu("" + yeuCau.getId())
                    .loaiHoaDon(LoaiHoaDon.HOA_DON_DOI)
                    .uuDai(0)
                    .tienGiam(tienGiam)
                    .build();

            for (HoaDonChiTiet hoaDonChiTietDoi : listHoaDonChiTietDoi) {
                hoaDonChiTietDoi.setHoaDon(hoaDonDoi);
                if(hoaDonChiTietDoi.getTrangThai() == 1){
                    BigDecimal soLuong = new BigDecimal(hoaDonChiTietDoi.getSoLuong());
                    BigDecimal donGia = hoaDonChiTietDoi.getDonGia();
                    tienThanhToan = tienThanhToan.add(soLuong.multiply(donGia));}
            }

            ChiTietThanhToan chiTietThanhToan = ChiTietThanhToan.builder()
                    .hoaDon(hoaDonDoi)
                    .hinhThucThanhToan(1)
                    .tienThanhToan(tienThanhToan)
                    .trangThai(1)
                    .build();
            chiTietThanhToans.add(chiTietThanhToan);

            hoaDonDoi.setChiTietThanhToans(chiTietThanhToans);

           return hoaDonRepository.save(hoaDonDoi);
        }
        return null;
    }

    @Override
    public YeuCauResponse unConfirmYeuCau(YeuCauRequest yeuCauRequest) {
        Long nguoiThucHienAndNguoiSua = sessionService.getAdmintrator().getId() == null ? null : sessionService.getAdmintrator().getId();
        YeuCau yeuCauSaved = yeuCauRepository.findById(yeuCauRequest.getId()).orElse(null);
        //ngày từ chối
        yeuCauSaved.setNgaySua(LocalDateTime.now());
        //người sửa
        yeuCauSaved.setNguoiSua(nguoiThucHienAndNguoiSua);
        //người từ chối
        yeuCauSaved.setNguoiThucHien(nguoiThucHienAndNguoiSua);
        yeuCauSaved.setGhiChu(yeuCauRequest.getGhiChu());
        yeuCauSaved.setTrangThai(TrangThaiYeuCau.BI_HUY);

        for (YeuCauChiTietRequest ycctRequest : yeuCauRequest.getListYeuCauChiTiet()) {
            YeuCauChiTiet yeuCauChiTiet = yeuCauChiTietRepository.findById(ycctRequest.getId()).orElse(null);
            yeuCauChiTiet.setLoaiYeuCauChiTiet(ycctRequest.getLoaiYeuCauChiTiet());
            yeuCauChiTiet.setTrangThai(TrangThaiYeuCauChiTiet.HUY_TRA);
            yeuCauChiTiet.setTinhTrangSanPham(ycctRequest.getTinhTrangSanPham());
            yeuCauChiTietRepository.save(yeuCauChiTiet);
            HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(ycctRequest.getHoaDonChiTiet()).orElse(null);
            hoaDonChiTiet.setSoLuongTra(hoaDonChiTiet.getSoLuongTra() - 1);
            hoaDonChiTietRepository.save(hoaDonChiTiet);
        }

        return new YeuCauResponse(yeuCauRepository.save(yeuCauSaved));
    }

    @Override
    public YeuCauResponse updateYeuCau(YeuCauRequest yeuCauRequest) {
        Long nguoiThucHienAndNguoiSua = sessionService.getAdmintrator().getId() == null ? null : sessionService.getAdmintrator().getId();
        YeuCau yeuCauSaved = yeuCauRepository.findById(yeuCauRequest.getId()).orElse(null);

        //ngày sửa
        yeuCauSaved.setNgaySua(LocalDateTime.now());

        //người sửa
        yeuCauSaved.setNguoiSua(nguoiThucHienAndNguoiSua);

        yeuCauSaved.setGhiChu(yeuCauRequest.getGhiChu());

        for (YeuCauChiTietRequest ycctRequest : yeuCauRequest.getListYeuCauChiTiet()) {
            YeuCauChiTiet yeuCauChiTiet = yeuCauChiTietRepository.findById(ycctRequest.getId()).orElse(null);
            yeuCauChiTiet.setGhiChu(ycctRequest.getGhiChu());
            yeuCauChiTiet.setLyDo(lyDoRepository.findById(ycctRequest.getLyDo()).orElse(null));
            if (yeuCauChiTiet.getTinhTrangSanPham() == true && ycctRequest.getTinhTrangSanPham() == false) {
                BienTheGiay bienTheGiayTra = bienTheGiayRepository.findById(ycctRequest.getBienTheGiayTra()).orElse(null);
                bienTheGiayTra.setSoLuongLoi(bienTheGiayTra.getSoLuongLoi() - 1);
                bienTheGiayTra.setSoLuong(bienTheGiayTra.getSoLuong() + 1);
                bienTheGiayRepository.save(bienTheGiayTra);
            } else if (yeuCauChiTiet.getTinhTrangSanPham() == false && ycctRequest.getTinhTrangSanPham() == true) {
                BienTheGiay bienTheGiayTra = bienTheGiayRepository.findById(ycctRequest.getBienTheGiayTra()).orElse(null);
                bienTheGiayTra.setSoLuongLoi(bienTheGiayTra.getSoLuongLoi() + 1);
                bienTheGiayTra.setSoLuong(bienTheGiayTra.getSoLuong() - 1);
                bienTheGiayRepository.save(bienTheGiayTra);
            }
            yeuCauChiTiet.setTinhTrangSanPham(ycctRequest.getTinhTrangSanPham());

            yeuCauChiTietRepository.save(yeuCauChiTiet);
        }

        return new YeuCauResponse(yeuCauRepository.save(yeuCauSaved));
    }


    @Override
    public YeuCauResponse findById(Long id) {
        return new YeuCauResponse(yeuCauRepository.findById(id).orElse(null));
    }

    @Override
    public YeuCauResponse findByStatus() {
        return new YeuCauResponse(yeuCauRepository.findResponseByStatus());
    }

    @Override
    public Page<YeuCauResponse> getPage(YeuCauSearch yeuCauSearch) {
        Pageable pageable = PageRequest.of(yeuCauSearch.getCurrentPage() - 1, yeuCauSearch.getPageSize());
        if (yeuCauSearch.getNgayKetThuc() != null) {
            yeuCauSearch.setNgayKetThuc(yeuCauSearch.getNgayKetThuc().withHour(23).withMinute(59).withSecond(59));
        }
        return yeuCauRepository.getPageResponse(pageable, yeuCauSearch);
    }

    @Override
    public YeuCauResponse traHangNhanh(YeuCauRequest yeuCauRequest) {
        LocalDateTime ngayHienTai = LocalDateTime.now();
        Long nguoiThucHien = sessionService.getAdmintrator() == null ? null : sessionService.getAdmintrator().getId();
        NhanVien nhanVien = nhanVienRepository.findById(nguoiThucHien).orElse(null);
        HoaDon hoaDon = hoaDonRepository.findById(yeuCauRequest.getHoaDon()).orElse(null);
        YeuCau yeuCau = YeuCau.builder()
                .hoaDon(hoaDon)
                .nguoiThucHien(nhanVien.getId())
                .trangThai(TrangThaiYeuCau.DA_XAC_NHAN)
                .thongTinNhanHang(null)
                .phiShip(BigDecimal.ZERO)
                .ngayTao(ngayHienTai)
                .ngaySua(ngayHienTai)
                .ghiChu("Trả hàng nhanh - "+yeuCauRequest.getGhiChu())
                .nguoiTao(nhanVien.getId())
                .nguoiSua(nhanVien.getId())
                .tienKhachTra(yeuCauRequest.getTienKhachTra())
                .build();

        List<YeuCauChiTiet> listYeuCauChiTiet = new ArrayList<>();

        for (YeuCauChiTietRequest ycct : yeuCauRequest.getListYeuCauChiTiet()) {
            HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(ycct.getHoaDonChiTiet()).orElse(null);
            LyDo lyDo = lyDoRepository.findById(ycct.getLyDo()).orElse(null);
            YeuCauChiTiet yeuCauChiTiet = YeuCauChiTiet.builder()
                    .yeuCau(yeuCau)
                    .hoaDonChiTiet(hoaDonChiTiet)
                    .bienTheGiay(null)
                    .lyDo(lyDo)
                    .tienGiam(BigDecimal.ZERO)
                    .thanhTien(ycct.getThanhTien())
                    .trangThai(TrangThaiYeuCauChiTiet.DEFAULT)
                    .tinhTrangSanPham(ycct.getTinhTrangSanPham())
                    .loaiYeuCauChiTiet(2)
                    .ghiChu(ycct.getGhiChu())
                    .build();
            listYeuCauChiTiet.add(yeuCauChiTiet);
        }

        yeuCau.setListYeuCauChiTiet(listYeuCauChiTiet);

        YeuCau yeuCauSaved = yeuCauRepository.save(yeuCau);


        //tạo hóa đơn trả
        HoaDon hoaDonTra = HoaDon.builder()
                .hoaDonGoc(hoaDon.getId())
                .khachHang(hoaDon.getKhachHang())
                .nhanVien(nhanVien)
                .email(hoaDon.getKhachHang().getEmail())
                .ngayTao(ngayHienTai)
                .kenhBan(KenhBan.OFFLINE)
                .trangThai(TrangThaiHoaDon.DA_THANH_TOAN)
                .ghiChu(""+yeuCauSaved.getId())
                .loaiHoaDon(LoaiHoaDon.HOA_DON_TRA)
                .uuDai(0)
                .tienGiam(BigDecimal.ZERO)
                .build();


        Set<HoaDonChiTiet> listHoaDonChiTietTra = new HashSet<>();
        Map<BienTheGiay, HoaDonChiTiet> mapHoaDonChiTietTra = new HashMap<>();

        for (YeuCauChiTiet ycct : yeuCauSaved.getListYeuCauChiTiet()) {
            HoaDonChiTiet hoaDonChiTietTra = HoaDonChiTiet.builder()
                    .hoaDon(hoaDonTra)
                    .bienTheGiay(ycct.getHoaDonChiTiet().getBienTheGiay())
                    .donGia(ycct.getThanhTien())
                    .soLuong(1)
                    .trangThai(0)
                    .soLuongTra(0)
                    .build();
            BienTheGiay key = hoaDonChiTietTra.getBienTheGiay();
            if (mapHoaDonChiTietTra.containsKey(key)) {
                HoaDonChiTiet existing = mapHoaDonChiTietTra.get(key);
                existing.setSoLuong(existing.getSoLuong() + 1);
            } else {
                mapHoaDonChiTietTra.put(key, hoaDonChiTietTra);
                listHoaDonChiTietTra.add(hoaDonChiTietTra);
            }
        }

        hoaDonTra.setListHoaDonChiTiet(listHoaDonChiTietTra);
        hoaDonRepository.save(hoaDonTra);
        return null;
    }

    @Override
    public HoaDonYeuCauRespone getOneHoaDonYeuCauRespone(Long id) {
        return hoaDonRepository.getOneHoaDonYeuCau(id);
    }

}
