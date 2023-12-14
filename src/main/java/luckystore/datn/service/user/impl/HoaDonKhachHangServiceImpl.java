package luckystore.datn.service.user.impl;

import jakarta.mail.MessagingException;
import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.entity.ChiTietThanhToan;
import luckystore.datn.entity.DieuKien;
import luckystore.datn.entity.GioHang;
import luckystore.datn.entity.HangKhachHang;
import luckystore.datn.entity.HoaDon;
import luckystore.datn.entity.HoaDonChiTiet;
import luckystore.datn.entity.KhachHang;
import luckystore.datn.entity.PhieuGiamGia;
import luckystore.datn.exception.ConflictException;
import luckystore.datn.exception.InvalidIdException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.infrastructure.constraints.TrangThaiHoaDon;
import luckystore.datn.model.request.BienTheGiayGioHangRequest;
import luckystore.datn.model.request.GioHangThanhToanRequest;
import luckystore.datn.model.request.HoaDonThanhToanTaiQuayRequest;
import luckystore.datn.model.response.BienTheGiayResponse;
import luckystore.datn.model.response.GioHangChiTietResponse;
import luckystore.datn.model.response.GioHangResponse;
import luckystore.datn.model.response.HoaDonResponse;
import luckystore.datn.repository.BienTheGiayRepository;
import luckystore.datn.repository.DieuKienRepository;
import luckystore.datn.repository.GioHangChiTietRepository;
import luckystore.datn.repository.GioHangRepository;
import luckystore.datn.repository.HangKhachHangRepository;
import luckystore.datn.repository.HoaDonChiTietRepository;
import luckystore.datn.repository.HoaDonRepository;
import luckystore.datn.repository.KhachHangRepository;
import luckystore.datn.repository.KichThuocRepository;
import luckystore.datn.repository.MauSacRepository;
import luckystore.datn.repository.PhieuGiamGiaRepository;
import luckystore.datn.service.impl.EmailSenderService;
import luckystore.datn.service.user.HoaDonKhachHangService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
public class HoaDonKhachHangServiceImpl implements HoaDonKhachHangService {

    @Autowired
    HoaDonRepository hoaDonRepository;

    @Autowired
    KhachHangRepository khachHangRepository;

    @Autowired
    BienTheGiayRepository bienTheGiayRepository;

    @Autowired
    HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    MauSacRepository mauSacRepository;

    @Autowired
    KichThuocRepository kichThuocRepository;

    @Autowired
    GioHangRepository gioHangRepository;

    @Autowired
    GioHangChiTietRepository gioHangChiTietRepository;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    DieuKienRepository dieuKienRepository;

    @Autowired
    HangKhachHangRepository hangKhachHangRepository;

    @Autowired
    PhieuGiamGiaRepository phieuGiamGiaRepository;

    private static GioHangChiTietResponse getObjectWithId(List<GioHangChiTietResponse> list, Long id) {
        for (GioHangChiTietResponse obj : list) {
            if (Objects.equals(obj.getBienTheGiay().getId(), id)) {
                return obj;
            }
        }
        return null;
    }

    @Transactional
    @Override
    public HoaDonResponse addHoaDon(GioHangThanhToanRequest gioHangThanhToanRequest) throws MessagingException {

        List<GioHangChiTietResponse> gioHangChiTietResponseList = gioHangChiTietRepository.findGioHangChiTietByIdGioHang(gioHangThanhToanRequest.getId());
//câu này k làm gì thì xóa đi
        // Chưa có tiền giảm cho hóa đơn, tiền giảm = tiền giảm đợt giamrg giá + ưu đãi khách hàng + giảm voucher

        HoaDon hoaDon = getHoaDon(new HoaDon(), gioHangThanhToanRequest);
        Set<HoaDonChiTiet> hoaDonChiTiets = getBienTheGiay(gioHangThanhToanRequest.getBienTheGiayRequests(), hoaDon);
        hoaDon.setListHoaDonChiTiet(hoaDonChiTiets);

        setChiTietThanhToan(hoaDon, gioHangThanhToanRequest);

        if (gioHangThanhToanRequest.getKhachHang() == null) {
            subtractSoLuongGiay(hoaDonChiTiets);
            return new HoaDonResponse(hoaDonRepository.save(hoaDon));
        } else {
            checkKhuyenMaiSanPham(gioHangThanhToanRequest);
            checkKhuyenMaiKhachHang(gioHangThanhToanRequest);
            if (gioHangThanhToanRequest.getPhieuGiamGia() != null) {
                checkKhuyenMaiPhieuGiamGia(gioHangThanhToanRequest);
            }
//            checkSoLuong(gioHangThanhToanRequest.getBienTheGiayRequests()); // phafa nào ông comment tôi k biết làm gì thì giữ lại

            subtractSoLuongGiay(hoaDonChiTiets);
            hoaDon = hoaDonRepository.save(hoaDon);

//            emailSenderService.sendEmailOrder("quanchun11022@gmail.com","abc",generateHtmlTable(hoaDonChiTiets),null);

            List<GioHangChiTietResponse> bienTheGiayResponseList = gioHangChiTietRepository.findGioHangChiTietByIdGioHang(gioHangThanhToanRequest.getId());
            for (BienTheGiayGioHangRequest bienTheGiayRequest : gioHangThanhToanRequest.getBienTheGiayRequests()) {
                GioHangChiTietResponse gioHangChiTietResponse = getObjectWithId(bienTheGiayResponseList, bienTheGiayRequest.getId());
                if (gioHangChiTietResponse != null) {
                    gioHangChiTietRepository.deleteById(gioHangChiTietResponse.getId());
                }
            }

            return new HoaDonResponse(hoaDon);
        }
    }

    private void subtractSoLuongGiay(Set<HoaDonChiTiet> hoaDonChiTiets) {
        List<Long> idsBienThe = hoaDonChiTiets.stream().map(hdct -> hdct.getBienTheGiay().getId()).toList();
        List<BienTheGiay> bienTheGiays = bienTheGiayRepository.getAllByIds(idsBienThe);
        hoaDonChiTiets.forEach(hdct -> {
            bienTheGiays.forEach(bienThe -> {
                if (Objects.equals(hdct.getBienTheGiay().getId(), bienThe.getId())) {
                    bienThe.setSoLuong(bienThe.getSoLuong() - hdct.getSoLuong());
                    if (bienThe.getSoLuong() < 0) {
                        throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Số lượng sản phẩm không đủ, vui lòng kiểm tra lại")));
                    }
                }
            });
        });

        bienTheGiayRepository.saveAll(bienTheGiays);
    }

    @Override
    public void cancelBankingOrder(Long id) {
        HoaDon hoaDon = hoaDonRepository.findById(id).orElseThrow(()
                -> new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn nào"))));

        if (hoaDon.getTrangThai() == TrangThaiHoaDon.CHUA_THANH_TOAN) {
            hoaDon.setTrangThai(TrangThaiHoaDon.DA_HUY);
            hoaDon.setGhiChu("Hóa đơn bị huỷ do khách hàng chưa hoàn tất thanh toán");
        } else {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Hóa đơn đã được xử lý")));
        }

        List<BienTheGiay> lstBienThe = new ArrayList<>();
        hoaDon.getListHoaDonChiTiet().forEach(hdct -> {
            BienTheGiay bienThe = new BienTheGiay();
            bienThe.setId(hdct.getBienTheGiay().getId());
            bienThe.setSoLuong(hdct.getSoLuong());
            lstBienThe.add(bienThe);
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
        hoaDonRepository.save(hoaDon);

    }

    @Override
    public Long hoanTatThanhToan(HoaDonThanhToanTaiQuayRequest request) {
        HoaDon hoaDon = hoaDonRepository.findById(Long.valueOf(request.getIdHoaDon()))
                .orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không tìm thấy hóa đơn"))));

        if (hoaDon.getTrangThai() != 0) {
            throw new ConflictException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Hóa đơn đã được xử lý vui lòng kiểm tra lại")));
        }

        hoaDon.setTrangThai(TrangThaiHoaDon.CHO_XAC_NHAN);
        hoaDon.getChiTietThanhToans().forEach(chiTiet -> {
            chiTiet.setTrangThai(1);
        });
        hoaDonRepository.save(hoaDon);
        return hoaDon.getId();
    }

    private HoaDon getHoaDon(HoaDon hoaDon, GioHangThanhToanRequest gioHangThanhToanRequest) {

        if (gioHangThanhToanRequest.getDieuKien() == null) {
            hoaDon.setDieuKien(null);
        } else {
            DieuKien dieuKien = dieuKienRepository.findById(gioHangThanhToanRequest.getDieuKien().getId())
                    .orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Đợt giảm giá không tồn tại"))));
            if (dieuKien.getDotGiamGia().getTrangThai() != 1) {
                throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("dieuKienError", "Đợt giảm giá đã hết hạn")));
            }

            if (dieuKien.getDotGiamGia().getNgayKetThuc().isBefore(LocalDateTime.now())) {
                throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("dieuKienError", "Đợt giảm giá đã hết hạn")));
            }

            if (dieuKien.getDotGiamGia().getNgayBatDau().isAfter(LocalDateTime.now())) {
                throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("dieuKienError", "Đợt giảm giá chưa diễn ra")));
            }

            if (dieuKien.getTongHoaDon().toBigInteger().compareTo(gioHangThanhToanRequest.getDieuKien().getTongHoaDon().toBigInteger()) != 0) {
                throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("dieuKienError", "Đợt giảm giá vừa được cập nhật , Vui lòng kiểm tra lại !")));
            }

            BigDecimal tongTienDieuKien = BigDecimal.ZERO;
            System.out.println(tongTienDieuKien.toBigInteger() + " - " + gioHangThanhToanRequest.getTongTienChuongTrinhGiamGia().toBigInteger());
            tongTienDieuKien = gioHangThanhToanRequest.getTongTien().subtract((gioHangThanhToanRequest.getTongTien().multiply(BigDecimal.valueOf(dieuKien.getPhanTramGiam()))).divide(BigDecimal.valueOf(100)));
            if (gioHangThanhToanRequest.getTongTienChuongTrinhGiamGia().toBigInteger().compareTo(tongTienDieuKien.toBigInteger()) != 0) {
                throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("dieuKienError", "Đợt giảm giá vừa được cập nhật lại khuyến mại, Vui lòng kiểm tra")));
            }
            hoaDon.setDieuKien(dieuKien);
        }

        KhachHang khachHang;
        if (gioHangThanhToanRequest.getKhachHang() == null) {
            khachHang = null;
        } else {
            khachHang = khachHangRepository.findById(gioHangThanhToanRequest.getKhachHang().getId()).get();

        }
        hoaDon.setKhachHang(khachHang);
//        hoaDon.setId(gioHangThanhToanRequest.getId());
        hoaDon.setNgayTao(LocalDateTime.now());
        hoaDon.setNgayShip(gioHangThanhToanRequest.getNgayShip());
        hoaDon.setNgayNhan(gioHangThanhToanRequest.getNgayNhan());
        hoaDon.setNgayThanhToan(gioHangThanhToanRequest.getNgayThanhToan());
        hoaDon.setKenhBan(gioHangThanhToanRequest.getKenhBan());
        hoaDon.setLoaiHoaDon(gioHangThanhToanRequest.getLoaiHoaDon());
        hoaDon.setMaVanDon(gioHangThanhToanRequest.getMaVanDon());
        hoaDon.setEmail(gioHangThanhToanRequest.getEmail());
        hoaDon.setPhiShip(gioHangThanhToanRequest.getPhiShip());
        hoaDon.setSoDienThoaiNhan(gioHangThanhToanRequest.getSoDienThoaiNhan());
        hoaDon.setDiaChiNhan(gioHangThanhToanRequest.getDiaChiNhan());
        hoaDon.setTrangThai(gioHangThanhToanRequest.getTrangThai());
        hoaDon.setGhiChu(gioHangThanhToanRequest.getGhiChu());
        hoaDon.setKenhBan(2);

        return hoaDon;
    }

    private BigDecimal checkKhuyenMaiSanPham(GioHangThanhToanRequest gioHangThanhToanRequest) {
        List<GioHangChiTietResponse> gioHangChiTietResponseList = gioHangChiTietRepository.findGioHangChiTietByIdGioHang(gioHangThanhToanRequest.getId());
        List<Long> ids = new ArrayList<>();
        for (GioHangChiTietResponse b : gioHangChiTietResponseList) {
            ids.add(b.getBienTheGiay().getId());
        }
        BigDecimal tongTien = BigDecimal.ZERO;
        List<BienTheGiayResponse> bienTheGiayResponsesKhuyenMai = bienTheGiayRepository.bienTheGiay(ids);
        for (GioHangChiTietResponse gioHangChiTietResponse : gioHangChiTietResponseList) {
            boolean tonTai = false;
            for (BienTheGiayResponse bienTheGiayResponseKhuyenmai : bienTheGiayResponsesKhuyenMai) {
                if (Objects.equals(gioHangChiTietResponse.getBienTheGiay().getId(), bienTheGiayResponseKhuyenmai.getId())) {
                    tonTai = true;

                    BigDecimal discountAmount = bienTheGiayResponseKhuyenmai.getGiaBan()
                            .multiply(new BigDecimal(bienTheGiayResponseKhuyenmai.getKhuyenMai()))
                            .divide(new BigDecimal(100));
                    tongTien = tongTien.add((bienTheGiayResponseKhuyenmai.getGiaBan().subtract(discountAmount)).multiply(BigDecimal.valueOf(gioHangChiTietResponse.getSoLuong())));
                }
            }
            if (tonTai == false) {
                tongTien = tongTien.add((gioHangChiTietResponse.getBienTheGiay().getGiaBan().multiply(BigDecimal.valueOf(gioHangChiTietResponse.getSoLuong()))));
            }
        }
        System.out.println(gioHangThanhToanRequest.getTongTien().toBigInteger() + " - " + tongTien.toBigInteger());
        if (gioHangThanhToanRequest.getTongTien().toBigInteger().compareTo(tongTien.toBigInteger()) != 0) {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("khuyenMaiError", "Một số sản phẩm vừa được cập nhật lại khuyến mại, hãy kiểm tra lại !")));
        }
        return tongTien;
    }

    private void checkKhuyenMaiKhachHang(GioHangThanhToanRequest gioHangThanhToanRequest) {

        KhachHang khachHang = khachHangRepository.findById(gioHangThanhToanRequest.getKhachHang().getId()).orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Khách Hàng không tồn tại"))));
        hangKhachHangRepository.findById(khachHang.getHangKhachHang().getId()).orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Hạng khách hàng không tồn tại !"))));
        BigDecimal tongTienHangKhachHang = gioHangThanhToanRequest.getTongTien().subtract((gioHangThanhToanRequest.getTongTien().multiply(BigDecimal.valueOf(khachHang.getHangKhachHang().getUuDai()))).divide(BigDecimal.valueOf(100)));
        System.out.println(tongTienHangKhachHang.toBigInteger()+ " - "+gioHangThanhToanRequest.getTongTienHangKhachHang().toBigInteger());
        if (tongTienHangKhachHang.toBigInteger().compareTo(gioHangThanhToanRequest.getTongTienHangKhachHang().toBigInteger()) != 0) {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("khuyenMaiHangKhachHangError", "Hạng khách hàng vừa được cập nhật, hãy kiểm tra lại !")));
        }

        if (!gioHangThanhToanRequest.getKhachHang().getHangKhachHang().getUuDai().equals(khachHang.getHangKhachHang().getUuDai())) {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("khuyenMaiHangKhachHangError", "Hạng khách hàng vừa được cập nhật lại, hãy kiểm tra lại !")));
        }
    }

    private void checkKhuyenMaiPhieuGiamGia(GioHangThanhToanRequest gioHangThanhToanRequest) {


        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(gioHangThanhToanRequest.getPhieuGiamGia().getId()).orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Phiếu Giảm Giá không tồn tại !"))));
        long currentSeconds = (System.currentTimeMillis() / 1000) * 1000;
        if (phieuGiamGia.getNgayBatDau() > currentSeconds || phieuGiamGia.getNgayKetThuc() < currentSeconds) {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("phieuGiamGiaError", "Phiếu giảm giá vừa được cập nhật lại , vui lòng kiểm tra lại !")));
        }
        if (phieuGiamGia.getSoLuongPhieu() == 0 || phieuGiamGia.getTrangThai() != 1) {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("phieuGiamGiaError", "Phiếu giảm giá đã được sử dụng hết !")));
        }

        PhieuGiamGia phieuGiamGiaComp = gioHangThanhToanRequest.getPhieuGiamGia();
        if (phieuGiamGia.getGiaTriDonToiThieu().toBigInteger().compareTo(phieuGiamGiaComp.getGiaTriDonToiThieu().toBigInteger()) > 0 ||
                phieuGiamGia.getGiaTriGiamToiDa().toBigInteger().compareTo(phieuGiamGiaComp.getGiaTriGiamToiDa().toBigInteger()) != 0 ||
                !phieuGiamGia.getPhanTramGiam().equals(phieuGiamGiaComp.getPhanTramGiam())) {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("phieuGiamGiaError", "Phiếu giảm giá vừa được cập nhật lại , vui lòng kiểm tra lại !")));
        }
        HangKhachHang hangKhachHang = hangKhachHangRepository.findById(gioHangThanhToanRequest.getKhachHang()
                        .getHangKhachHang().getId())
                .orElseThrow(() -> new NotFoundException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Hạng khách hàng không tồn tại !"))));
        System.out.println(hangKhachHang.getId() + " - " + gioHangThanhToanRequest.getKhachHang().getHangKhachHang().getId());
        if (!gioHangThanhToanRequest.getKhachHang().getId().equals(phieuGiamGia.getDoiTuongApDung().getId())) {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("phieuGiamGiaError", "Hạng khách hàng không phù hợp , vui lòng kiểm tra lại !")));
        }

    }

    private void setChiTietThanhToan(HoaDon hoaDon, GioHangThanhToanRequest gioHangThanhToanRequest) {
        Set<ChiTietThanhToan> chiTietThanhToans = hoaDon.getChiTietThanhToans();
        if (chiTietThanhToans == null) {
            chiTietThanhToans = new HashSet<>();
        }
        chiTietThanhToans.removeIf(item -> item.getId() != null);

        ChiTietThanhToan chiTietThanhToan = new ChiTietThanhToan();
        chiTietThanhToan.setHoaDon(hoaDon);
        chiTietThanhToan.setTienThanhToan(gioHangThanhToanRequest.getTongTienThanhToan());
        if (gioHangThanhToanRequest.getPhuongThuc() == 2) {
            chiTietThanhToan.setHinhThucThanhToan(2);
            chiTietThanhToan.setTrangThai(0);

            hoaDon.setTrangThai(TrangThaiHoaDon.CHUA_THANH_TOAN);
            hoaDon.setNgayThanhToan(LocalDateTime.now().plusMinutes(10));

        } else {
            chiTietThanhToan.setHinhThucThanhToan(1);
            chiTietThanhToan.setTrangThai(0);
        }
        chiTietThanhToans.add(chiTietThanhToan);
        hoaDon.setChiTietThanhToans(chiTietThanhToans);
    }

    private Set<HoaDonChiTiet> getBienTheGiay(Set<BienTheGiayGioHangRequest> bienTheGiayRequests, HoaDon hoaDon) {
        Set<HoaDonChiTiet> hoaDonChiTiets = new HashSet<>();
        for (BienTheGiayGioHangRequest h : bienTheGiayRequests) {
            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            BienTheGiay bienTheGiay = bienTheGiayRepository.findById(h.getId()).get();
            hoaDonChiTiet.setBienTheGiay(bienTheGiay);
            hoaDonChiTiet.setHoaDon(hoaDon);
            hoaDonChiTiet.setSoLuong(h.getSoLuongMua());
            hoaDonChiTiet.setTrangThai(1);
            hoaDonChiTiet.setDonGia(h.getGiaSauKhuyenMai());
            hoaDonChiTiets.add(hoaDonChiTiet);
        }
        return hoaDonChiTiets;
    }

    private GioHang getGioHang(GioHangResponse gioHangResponse, GioHang gioHang) {
        gioHang.setId(gioHangResponse.getId());
        gioHang.setKhachHang(khachHangRepository.findById(gioHangResponse.getKhachHang().getId()).get());
        gioHang.setTrangThai(gioHangResponse.getTrangThai());
        gioHang.setNgayTao(gioHangResponse.getNgayTao());
        gioHang.setGhiChu(gioHangResponse.getGhiChu());
        return gioHang;
    }

    private void checkSoLuong(Set<BienTheGiayGioHangRequest> list) {
        List<String> errors = new ArrayList<>();
        for (BienTheGiayGioHangRequest bienTheGiayRequest : list) {
            String error = "";
            if (bienTheGiayRepository.getSoLuong(bienTheGiayRequest.getId()) == 0) {
                error = "" + bienTheGiayRequest.getId() + ": 1";
            } else {
                error = "" + bienTheGiayRequest.getId() + ": 2";
            }
            errors.add(error);
        }
        if (!errors.isEmpty()) {
            throw new ConflictException(errors);
        }
    }

    public String generateHtmlTable(Set<HoaDonChiTiet> hoaDonChiTiets) {
        StringBuilder htmlTable = new StringBuilder();

        htmlTable.append("<table border='1'>");

        htmlTable.append("<tr>");
        htmlTable.append("<th>Tên giày</th>");
        htmlTable.append("<th>Đơn giá</th>");
        htmlTable.append("<th>Số lượng</th>");
        htmlTable.append("</tr>");

        for (HoaDonChiTiet hoaDonChiTiet : hoaDonChiTiets) {
            htmlTable.append("<tr>");
            htmlTable.append("<td>").append(hoaDonChiTiet.getBienTheGiay().getGiay().getTen()
                    + "( " + hoaDonChiTiet.getBienTheGiay().getKichThuoc().getTen() + " - "
                    + hoaDonChiTiet.getBienTheGiay().getMauSac().getTen() + " )").append("</td>");
            htmlTable.append("<td>").append(hoaDonChiTiet.getDonGia()).append("</td>");
            htmlTable.append("<td>").append(hoaDonChiTiet.getSoLuong()).append("</td>");
            htmlTable.append("</tr>");
        }

        htmlTable.append("</table>");
        return htmlTable.toString();
    }

}

