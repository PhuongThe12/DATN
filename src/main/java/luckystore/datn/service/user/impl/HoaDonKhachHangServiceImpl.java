package luckystore.datn.service.user.impl;

import luckystore.datn.entity.*;
import luckystore.datn.exception.InvalidIdException;
import luckystore.datn.model.request.*;
import luckystore.datn.model.response.GioHangResponse;
import luckystore.datn.model.response.HoaDonResponse;
import luckystore.datn.repository.*;
import luckystore.datn.service.user.HoaDonKhachHangService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
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

    @Override
    public HoaDonResponse addHoaDon(GioHangThanhToanRequest gioHangThanhToanRequest) {

        GioHang gioHang = gioHangRepository.findById(gioHangThanhToanRequest.getId()).get();
//        if (gioHang.getTrangThai() == 1) {
            HoaDon hoaDonSaved = hoaDonRepository.save(getHoaDon(new HoaDon(), gioHangThanhToanRequest));
            Set<HoaDonChiTiet> hoaDonChiTiets = getBienTheGiay(gioHangThanhToanRequest.getBienTheGiayRequests(), hoaDonSaved);
            hoaDonChiTietRepository.saveAll(hoaDonChiTiets);

            GioHangResponse gioHangResponse = gioHangRepository.getGioHangByIdKhachHang(hoaDonSaved.getKhachHang().getId());

            GioHang gioHangOld = getGioHang(gioHangResponse, new GioHang());
            gioHangOld.setTrangThai(2);
            gioHangRepository.save(gioHangOld);

            GioHang gioHangNew = new GioHang();
            gioHangNew.setKhachHang(gioHangOld.getKhachHang());
            gioHangNew.setNgayTao(LocalDateTime.now());
            gioHangNew.setTrangThai(1);
            gioHangNew.setGhiChu("abc");
            gioHangRepository.save(gioHangNew);
            return new HoaDonResponse(hoaDonSaved);
//        }
//        return new HoaDonResponse();
    }

    private HoaDon getHoaDon(HoaDon hoaDon, GioHangThanhToanRequest gioHangThanhToanRequest) {

        KhachHang khachHang = khachHangRepository.findById(gioHangThanhToanRequest.getKhachHang().getId()).get();

        hoaDon.setKhachHang(khachHang);
        hoaDon.setId(gioHangThanhToanRequest.getId());
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
        hoaDon.setChiTietThanhToans(null);
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
}

