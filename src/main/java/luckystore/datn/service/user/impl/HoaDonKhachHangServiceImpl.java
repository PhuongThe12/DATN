package luckystore.datn.service.user.impl;

import jakarta.mail.MessagingException;
import luckystore.datn.constraints.ErrorMessage;
import luckystore.datn.entity.*;
import luckystore.datn.exception.ConflictException;
import luckystore.datn.exception.ExcelException;
import luckystore.datn.exception.InvalidIdException;
import luckystore.datn.model.request.*;
import luckystore.datn.model.response.GioHangChiTietResponse;
import luckystore.datn.model.response.GioHangResponse;
import luckystore.datn.model.response.HoaDonResponse;
import luckystore.datn.repository.*;
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

    @Transactional
    @Override
    public HoaDonResponse addHoaDon(GioHangThanhToanRequest gioHangThanhToanRequest) throws MessagingException {

        List<GioHangChiTietResponse> gioHangChiTietResponseList = gioHangChiTietRepository.findGioHangChiTietByIdGioHang(gioHangThanhToanRequest.getId());

        if (gioHangThanhToanRequest.getKhachHang() == null) {
            HoaDon hoaDonSaved = hoaDonRepository.save(getHoaDon(new HoaDon(), gioHangThanhToanRequest));
            Set<HoaDonChiTiet> hoaDonChiTiets = getBienTheGiay(gioHangThanhToanRequest.getBienTheGiayRequests(), hoaDonSaved);
            hoaDonChiTietRepository.saveAll(hoaDonChiTiets);
            emailSenderService.sendEmailOrder("quanchun11022@gmail.com","abc",generateHtmlTable(hoaDonChiTiets),null);
            return new HoaDonResponse(hoaDonSaved);
        } else if (gioHangChiTietResponseList.size() != 0) {
//            checkSoLuong(gioHangThanhToanRequest.getBienTheGiayRequests());
            HoaDon hoaDonSaved = hoaDonRepository.save(getHoaDon(new HoaDon(), gioHangThanhToanRequest));
            Set<HoaDonChiTiet> hoaDonChiTiets = getBienTheGiay(gioHangThanhToanRequest.getBienTheGiayRequests(), hoaDonSaved);
            hoaDonChiTietRepository.saveAll(hoaDonChiTiets);
            emailSenderService.sendEmailOrder("quanchun11022@gmail.com","abc",generateHtmlTable(hoaDonChiTiets),null);

            gioHangChiTietRepository.deleteAllGioHangChiTietByIdGioHang(gioHangThanhToanRequest.getId());

            return new HoaDonResponse(hoaDonSaved);
        }
        throw new InvalidIdException("Giỏ hàng đã được thanh toán !");
    }

    private HoaDon getHoaDon(HoaDon hoaDon, GioHangThanhToanRequest gioHangThanhToanRequest) {
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

        return hoaDon;
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
            hoaDonChiTiet.setDonGia(h.getGiaBan());
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
                    + "( "+hoaDonChiTiet.getBienTheGiay().getKichThuoc().getTen() +" - "
                    + hoaDonChiTiet.getBienTheGiay().getMauSac().getTen()+" )").append("</td>");
            htmlTable.append("<td>").append(hoaDonChiTiet.getDonGia()).append("</td>");
            htmlTable.append("<td>").append(hoaDonChiTiet.getSoLuong()).append("</td>");
            htmlTable.append("</tr>");
        }

        htmlTable.append("</table>");
        return htmlTable.toString();
    }

}

