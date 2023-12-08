package luckystore.datn.service.impl;

import luckystore.datn.entity.*;
import luckystore.datn.infrastructure.constraints.KenhBan;
import luckystore.datn.infrastructure.constraints.TrangThaiHoaDon;
import luckystore.datn.infrastructure.constraints.TrangThaiHoaDonChiTiet;
import luckystore.datn.infrastructure.constraints.TrangThaiYeuCau;
import luckystore.datn.infrastructure.constraints.TrangThaiYeuCauChiTiet;
import luckystore.datn.infrastructure.security.session.SessionService;
import luckystore.datn.model.request.YeuCauChiTietRequest;
import luckystore.datn.model.request.YeuCauRequest;
import luckystore.datn.model.response.YeuCauResponse;
import luckystore.datn.repository.*;
import luckystore.datn.service.ImageHubService;
import luckystore.datn.service.YeuCauChiTietService;
import luckystore.datn.service.YeuCauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        Long nguoiSuaAndThucHien = sessionService.getAdmintrator() == null ? null : sessionService.getAdmintrator().getId();
        YeuCau yeuCauGoc = yeuCauRepository.findById(yeuCauRequest.getId()).orElse(null);

        YeuCau yeuCauSaved = YeuCau.builder().id(yeuCauGoc.getId()).hoaDon(yeuCauGoc.getHoaDon()).nguoiThucHien(nguoiSuaAndThucHien).trangThai(TrangThaiYeuCau.DA_XAC_NHAN).ngayTao(yeuCauGoc.getNgayTao()).ngaySua(LocalDateTime.now()).ghiChu(yeuCauRequest.getGhiChu()).hoaDonDoiTra(null).thongTinNhanHang(yeuCauGoc.getThongTinNhanHang()).phiShip(yeuCauGoc.getPhiShip()).nguoiTao(yeuCauGoc.getNguoiTao()).nguoiSua(nguoiSuaAndThucHien).build();


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

                hoaDonChiTietUpdate.setSoLuongTra(hoaDonChiTietUpdate.getSoLuongTra() + 1);


                if (ycctRequest.getLoaiYeuCauChiTiet() == 1) { // Đổi Giày
                    bienTheGiayDoi.setSoLuong(bienTheGiayDoi.getSoLuong() - 1);//trừ sản phẩm đổi trong db
                    YeuCauChiTiet yeuCauChiTietSaved = YeuCauChiTiet.builder().id(ycctRequest.getId()).yeuCau(yeuCauSaved).hoaDonChiTiet(hoaDonChiTietUpdate).bienTheGiay(bienTheGiayDoi).lyDo(lyDoUpdate).tienGiam(ycctRequest.getTienGiam()).thanhTien(ycctRequest.getThanhTien()).loaiYeuCauChiTiet(ycctRequest.getLoaiYeuCauChiTiet()).trangThai(TrangThaiYeuCauChiTiet.DA_XAC_NHAN_DA_XAC_NHAN).tinhTrangSanPham(ycctRequest.getTinhTrangSanPham()).ghiChu(ycctRequest.getGhiChu()).build();
                    listYeuCauChiTiet.add(yeuCauChiTietSaved);
                } else if (ycctRequest.getLoaiYeuCauChiTiet() == 2) { //nếu trả

                    if (bienTheGiayDoi != null) { //Trả hàng - Từ chối đổi
                        YeuCauChiTiet yeuCauChiTietSaved = YeuCauChiTiet.builder().id(ycctRequest.getId()).yeuCau(yeuCauSaved).hoaDonChiTiet(hoaDonChiTietUpdate).bienTheGiay(bienTheGiayDoi).lyDo(lyDoUpdate).tienGiam(ycctRequest.getTienGiam()).thanhTien(ycctRequest.getThanhTien()).loaiYeuCauChiTiet(ycctRequest.getLoaiYeuCauChiTiet()).trangThai(TrangThaiYeuCauChiTiet.DA_XAC_NHAN_HUY).tinhTrangSanPham(ycctRequest.getTinhTrangSanPham()).ghiChu(ycctRequest.getGhiChu()).build();
                        listYeuCauChiTiet.add(yeuCauChiTietSaved);
                    } else { //Trả bình thường
                        YeuCauChiTiet yeuCauChiTietSaved = YeuCauChiTiet.builder().id(ycctRequest.getId()).yeuCau(yeuCauSaved).hoaDonChiTiet(hoaDonChiTietUpdate).bienTheGiay(null).lyDo(lyDoUpdate).tienGiam(BigDecimal.ZERO).thanhTien(ycctRequest.getThanhTien()).loaiYeuCauChiTiet(ycctRequest.getLoaiYeuCauChiTiet()).trangThai(TrangThaiYeuCauChiTiet.DA_XAC_NHAN_DA_XAC_NHAN).tinhTrangSanPham(ycctRequest.getTinhTrangSanPham()).ghiChu(ycctRequest.getGhiChu()).build();
                        listYeuCauChiTiet.add(yeuCauChiTietSaved);
                    }

                }
            } else { // nếu là hủy = từ chối
                YeuCauChiTiet yeuCauChiTietSaved = YeuCauChiTiet.builder().id(ycctRequest.getId()).yeuCau(yeuCauSaved).hoaDonChiTiet(hoaDonChiTietUpdate).bienTheGiay(bienTheGiayDoi).lyDo(lyDoUpdate).tienGiam(ycctRequest.getTienGiam()).thanhTien(ycctRequest.getThanhTien()).loaiYeuCauChiTiet(ycctRequest.getLoaiYeuCauChiTiet()).trangThai(TrangThaiYeuCauChiTiet.DA_HUY_DA_HUY).tinhTrangSanPham(ycctRequest.getTinhTrangSanPham()).ghiChu(ycctRequest.getGhiChu()).build();
                listYeuCauChiTiet.add(yeuCauChiTietSaved);
            }
        }

        taoHoaDon(yeuCauSaved, listYeuCauChiTiet);//đồng thời tạo hóa đơn đổi/trả

        yeuCauSaved.setListYeuCauChiTiet(listYeuCauChiTiet);
        return new YeuCauResponse(yeuCauRepository.save(yeuCauSaved));
    }


    private void taoHoaDon(YeuCau yeuCauSaved, List<YeuCauChiTiet> listYeuCauChiTiet) {
        System.out.println("Hóa đơn: " + yeuCauSaved.getHoaDon().getId());
        HoaDon hoaDon = yeuCauSaved.getHoaDon();
        KhachHang khachHang = hoaDon.getKhachHang();

        // người tạo
        BigDecimal tienGiam = BigDecimal.ZERO;

        Set<HoaDonChiTiet> listHoaDonChiTietTra = new HashSet<>();
        Set<HoaDonChiTiet> listHoaDonChiTietDoi = new HashSet<>();
        for (YeuCauChiTiet ycct : listYeuCauChiTiet) {
            if (ycct.getLoaiYeuCauChiTiet() == 1) { //tạo hóa đơn chi tiết đổi
                tienGiam = tienGiam.add(ycct.getTienGiam() == null ? BigDecimal.ZERO : ycct.getTienGiam());

                Integer soLuongDoi = 0;
                Integer soLuongTra = 0;
                for (YeuCauChiTiet ycct1 : listYeuCauChiTiet) { //tính tổng số lượng Đổi và thành tiền
                    if (ycct1.getId() == ycct.getId()) {
                        soLuongDoi += 1;
                    }
                    if (ycct1.getHoaDonChiTiet().getBienTheGiay().getId() == ycct.getHoaDonChiTiet().getBienTheGiay().getId()) {
                        soLuongTra += 1;
                    }
                }
                BigDecimal donGia = ycct.getBienTheGiay().getGiaBan().subtract(ycct.getTienGiam() == null ? BigDecimal.ZERO : ycct.getTienGiam());
                HoaDonChiTiet hoaDonChiTietDoi = HoaDonChiTiet.builder()
                        .bienTheGiay(ycct.getBienTheGiay())
                        .donGia(donGia)
                        .soLuong(soLuongDoi)
                        .trangThai(TrangThaiHoaDonChiTiet.HOA_DON_CHI_TIET_DOI)
                        .ghiChu(ycct.getLyDo().getTen()).soLuongTra(0)
                        .build();
                listHoaDonChiTietDoi.add(hoaDonChiTietDoi);

                HoaDonChiTiet hoaDonChiTietTra = HoaDonChiTiet.builder()
                        .bienTheGiay(ycct.getHoaDonChiTiet().getBienTheGiay())
                        .donGia(ycct.getThanhTien()).soLuong(soLuongTra)
                        .trangThai(TrangThaiHoaDonChiTiet.HOA_DON_CHI_TIET_TRA)
                        .ghiChu(ycct.getLyDo().getTen())
                        .soLuongTra(0)
                        .build();
                listHoaDonChiTietTra.add(hoaDonChiTietTra);
            }else if (ycct.getLoaiYeuCauChiTiet() == 2) { // tạo hóa đơn chi tiết trả

                Integer soLuongTra = 0;
                for (YeuCauChiTiet ycct1 : listYeuCauChiTiet) { //tính tổng số lượng trả và thành tiền
                    if (ycct1.getHoaDonChiTiet().getBienTheGiay().getId() == ycct.getHoaDonChiTiet().getBienTheGiay().getId()) {
                        soLuongTra += 1;
                    }
                }
                HoaDonChiTiet hoaDonChiTietTra = HoaDonChiTiet.builder().bienTheGiay(ycct.getHoaDonChiTiet().getBienTheGiay()).donGia(ycct.getThanhTien()).soLuong(soLuongTra).trangThai(TrangThaiHoaDonChiTiet.HOA_DON_CHI_TIET_TRA).ghiChu(ycct.getLyDo().getTen()).soLuongTra(0).build();
                listHoaDonChiTietTra.add(hoaDonChiTietTra);
            }

        }

        if (listHoaDonChiTietTra.size() > 0) { //tạo hóa đơn trả

            HoaDon hoaDonTra = HoaDon.builder().hoaDonGoc(hoaDon.getId()).khachHang(khachHang).nhanVien(null).email(khachHang.getEmail()).ngayTao(LocalDateTime.now()).kenhBan(KenhBan.ONLINE).trangThai(TrangThaiHoaDon.CHUA_THANH_TOAN).ghiChu("Hóa Đơn Trả: HD" + hoaDon.getId()).loaiHoaDon(3).build();
            for (HoaDonChiTiet hoaDonChiTietTra : listHoaDonChiTietTra) {
                hoaDonChiTietTra.setHoaDon(hoaDonTra);
            }
            hoaDonTra.setListHoaDonChiTiet(listHoaDonChiTietTra);
            hoaDonRepository.save(hoaDonTra);

        }

        if (listHoaDonChiTietDoi.size() > 0) { //tạo hóa đơn đổi
            String[] phanTach = yeuCauSaved.getThongTinNhanHang().split("-", 3);
            String tenNguoiNhan = phanTach[0]; // Phần 1 (tên)
            String sdtNhan = phanTach[1]; // Phần 2 (số điện thoại)
            String diaChiNhan = phanTach[2]; // Phần 3 (địa chỉ)

            for (HoaDonChiTiet hoaDonChiTietTra : listHoaDonChiTietTra) {
                if (hoaDonChiTietTra != null) {
                    listHoaDonChiTietDoi.add(hoaDonChiTietTra);
                }
            }

            HoaDon hoaDonDoi = HoaDon.builder().hoaDonGoc(hoaDon.getId()).khachHang(khachHang).nhanVien(null).email(khachHang.getEmail()).ngayTao(LocalDateTime.now()).phiShip(yeuCauSaved.getPhiShip()).soDienThoaiNhan(sdtNhan).diaChiNhan(diaChiNhan).kenhBan(KenhBan.ONLINE).trangThai(TrangThaiHoaDon.CHUA_THANH_TOAN).ghiChu("Hóa Đơn Đổi: HD" + hoaDon.getId()).loaiHoaDon(2).tienGiam(tienGiam).build();

            for (HoaDonChiTiet hoaDonChiTietDoi : listHoaDonChiTietDoi) {
                hoaDonChiTietDoi.setHoaDon(hoaDonDoi);
            }

            hoaDonRepository.save(hoaDonDoi);
        }

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
            yeuCauChiTiet.setTrangThai(TrangThaiYeuCauChiTiet.DA_HUY_DA_HUY);
            yeuCauChiTiet.setTinhTrangSanPham(ycctRequest.getTinhTrangSanPham());
            yeuCauChiTietRepository.save(yeuCauChiTiet);
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
    public Page<YeuCauResponse> getPage(Integer page, Long searchText, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, Integer trangThai) {
        return yeuCauRepository.getPageResponse(PageRequest.of((page - 1), 5), searchText, ngayBatDau, ngayKetThuc, trangThai);
    }


    public Date fomatDate(Date date, String string) {

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String date1 = sdf1.format(date);
        //2023-10-26 + 23:59:59
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            // Chuyển đổi chuỗi thành đối tượng Date
            return sdf.parse(date1 + string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}
