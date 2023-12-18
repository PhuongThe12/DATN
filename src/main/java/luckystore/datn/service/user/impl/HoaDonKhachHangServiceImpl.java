package luckystore.datn.service.user.impl;

import jakarta.mail.MessagingException;
import luckystore.datn.entity.*;
import luckystore.datn.exception.ConflictException;
import luckystore.datn.exception.InvalidIdException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.infrastructure.constraints.ErrorMessage;
import luckystore.datn.infrastructure.constraints.KenhBan;
import luckystore.datn.infrastructure.constraints.LoaiHoaDon;
import luckystore.datn.infrastructure.constraints.TrangThaiHoaDon;
import luckystore.datn.model.request.BienTheGiayGioHangRequest;
import luckystore.datn.model.request.GioHangThanhToanRequest;
import luckystore.datn.model.request.HoaDonDiaChiNhanRequest;
import luckystore.datn.model.request.HoaDonThanhToanTaiQuayRequest;
import luckystore.datn.model.response.BienTheGiayResponse;
import luckystore.datn.model.response.GioHangChiTietResponse;
import luckystore.datn.model.response.GioHangResponse;
import luckystore.datn.model.response.HoaDonResponse;
import luckystore.datn.model.response.print.HoaDonPrintResponse;
import luckystore.datn.repository.*;
import luckystore.datn.service.impl.EmailSenderService;
import luckystore.datn.service.user.HoaDonKhachHangService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

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

    @Autowired
    PhieuGiamGiaChiTietRepository phieuGiamGiaChiTietRepository;

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
        HoaDon hoaDon = getHoaDon(new HoaDon(), gioHangThanhToanRequest);
        Set<HoaDonChiTiet> hoaDonChiTiets = getBienTheGiay(gioHangThanhToanRequest.getBienTheGiayRequests(), hoaDon);
        hoaDon.setListHoaDonChiTiet(hoaDonChiTiets);
        hoaDon.setKenhBan(KenhBan.ONLINE);
        hoaDon.setLoaiHoaDon(LoaiHoaDon.HOA_DON_MUA);

        setChiTietThanhToan(hoaDon, gioHangThanhToanRequest);

        if (gioHangThanhToanRequest.getKhachHang() == null) {
            subtractSoLuongGiay(hoaDonChiTiets);
            hoaDon = hoaDonRepository.save(hoaDon);
            return new HoaDonResponse(hoaDon);
        } else {
            checkKhuyenMaiSanPham(gioHangThanhToanRequest);
            checkKhuyenMaiKhachHang(gioHangThanhToanRequest);
            if (gioHangThanhToanRequest.getPhieuGiamGia() != null) {
                checkKhuyenMaiPhieuGiamGia(gioHangThanhToanRequest);
            }
            subtractSoLuongGiay(hoaDonChiTiets);
            hoaDon = hoaDonRepository.save(hoaDon);
            if (gioHangThanhToanRequest.getPhieuGiamGia() != null) {
                PhieuGiamGiaChiTiet phieuGiamGiaChiTiet = getPhieuGiamGiaChiTiet(new PhieuGiamGiaChiTiet(), gioHangThanhToanRequest, hoaDon);
                phieuGiamGiaChiTietRepository.save(phieuGiamGiaChiTiet);

                PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(gioHangThanhToanRequest.getPhieuGiamGia().getId()).get();
                phieuGiamGia.setSoLuongPhieu(phieuGiamGia.getSoLuongPhieu() - 1);
                phieuGiamGiaRepository.save(phieuGiamGia);
            }


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

    private PhieuGiamGiaChiTiet getPhieuGiamGiaChiTiet(PhieuGiamGiaChiTiet phieuGiamGiaChiTiet, GioHangThanhToanRequest gioHangThanhToanRequest, HoaDon hoaDon) {
        phieuGiamGiaChiTiet.setIdHoaDon(hoaDon);
        phieuGiamGiaChiTiet.setIdPhieuGiamGia(gioHangThanhToanRequest.getPhieuGiamGia());
        phieuGiamGiaChiTiet.setGiaTruocGiam(gioHangThanhToanRequest.getTongTien());
        phieuGiamGiaChiTiet.setGiaSauGiam(gioHangThanhToanRequest.getTongTien().subtract(gioHangThanhToanRequest.getTongTienPhieuGiamGia()));
        phieuGiamGiaChiTiet.setNgayTao(LocalDateTime.now());
        phieuGiamGiaChiTiet.setTrangThai(1);
        return phieuGiamGiaChiTiet;
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
    public HoaDonResponse findById(Long id) {
        return new HoaDonResponse(hoaDonRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
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

    @Override
    public HoaDonResponse sendMailHoaDon(Long idHoaDon) throws MessagingException {
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon).get();
        List<HoaDonChiTiet> hoaDonChiTietList = hoaDonChiTietRepository.findAllByIdHoaDon(idHoaDon);
        Set<HoaDonChiTiet> linkedHashSet = new LinkedHashSet<>(hoaDonChiTietList);
        emailSenderService.sendEmailOrder(hoaDon.getEmail(), "Thông tin đơn hàng", generateHtmlTable(linkedHashSet, hoaDon), null);

        return new HoaDonResponse(hoaDon);
    }

    @Override
    public HoaDonResponse capNhatDiaChiNhan(HoaDonDiaChiNhanRequest hoaDonDiaChiNhanRequest) {
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonDiaChiNhanRequest.getId()).get();
        hoaDon.setSoDienThoaiNhan(hoaDonDiaChiNhanRequest.getSoDienThoaiNhan());
        hoaDon.setDiaChiNhan(hoaDonDiaChiNhanRequest.getDiaChiNhan());
        hoaDon.setPhiShip(hoaDonDiaChiNhanRequest.getPhiShip());

        return new HoaDonResponse(hoaDonRepository.save(hoaDon));
    }

    @Override
    public HoaDonPrintResponse getThanhToanChiTiet(Long idHoaDon) {
        return hoaDonRepository.getThanhToanChiTiet(idHoaDon);
    }

    private HoaDon getHoaDon(HoaDon hoaDon, GioHangThanhToanRequest gioHangThanhToanRequest) {
        BigDecimal tongTienGiam = BigDecimal.ZERO;
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
        tongTienGiam = gioHangThanhToanRequest.getTongTien().subtract(gioHangThanhToanRequest.getTongTienSauKhuyenMai());
        hoaDon.setTienGiam(tongTienGiam);

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
        System.out.println(tongTienHangKhachHang.toBigInteger() + " - " + gioHangThanhToanRequest.getTongTienHangKhachHang().toBigInteger());
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
        System.out.println(phieuGiamGia.getDoiTuongApDung().getId() + " - " + gioHangThanhToanRequest.getKhachHang().getHangKhachHang().getId());
        if (!gioHangThanhToanRequest.getKhachHang().getHangKhachHang().getId().equals(phieuGiamGia.getDoiTuongApDung().getId())) {
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
            if (h.getGiaBanSauKhuyenMai() != null) {
                hoaDonChiTiet.setDonGia(h.getGiaBanSauKhuyenMai());
            } else {
                hoaDonChiTiet.setDonGia(h.getGiaBan());
            }
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
                System.out.println("dqdqwdwq");
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

        public String generateHtmlTable(Set<HoaDonChiTiet> hoaDonChiTiets, HoaDon hoaDon) {
            StringBuilder htmlTable = new StringBuilder();

            Long idHoaDon = hoaDon.getId();
            String soDienThoai = hoaDon.getSoDienThoaiNhan().substring(hoaDon.getSoDienThoaiNhan().length() - 4);

            htmlTable.append("<p>Thông tin đơn hàng:</p>");
            htmlTable.append("<p>Mã hóa đơn: ").append(hoaDon.getId()).append("</p>");
            htmlTable.append("<p>Số điện thoại tra cứu: ").append(hoaDon.getSoDienThoaiNhan().substring(hoaDon.getSoDienThoaiNhan().length() - 4)).append("</p>");
            htmlTable.append("<p>Tra cứu đơn hàng <a href=" + "http://localhost:8080/home#/tra-cuu-don-hang?maHD=" + hoaDon.getId() + "&sdt=" + hoaDon.getSoDienThoaiNhan().substring(hoaDon.getSoDienThoaiNhan().length() - 4) + ">tại đây</a></p>");

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

