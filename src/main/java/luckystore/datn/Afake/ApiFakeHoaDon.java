package luckystore.datn.Afake;

import lombok.RequiredArgsConstructor;
import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.entity.ChiTietThanhToan;
import luckystore.datn.entity.HoaDon;
import luckystore.datn.entity.HoaDonChiTiet;
import luckystore.datn.entity.KhachHang;
import luckystore.datn.entity.NhanVien;
import luckystore.datn.repository.BienTheGiayRepository;
import luckystore.datn.repository.HoaDonRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@RestController
@RequestMapping("/fake")
@RequiredArgsConstructor
public class ApiFakeHoaDon {

    private final HoaDonRepository hoaDonRepository;

    private final BienTheGiayRepository bienTheGiayRepository;

    @PostMapping
    @Transactional
    public void fakeHoaDon(@RequestBody HoaDonFake hoaDonFake) {

        List<HoaDon> lstHoaDon = new ArrayList<>();

        LocalDate date = LocalDate.of(hoaDonFake.getNam(), hoaDonFake.getThang(), hoaDonFake.getNgay());
        Random random = new Random();

        List<BienTheGiay> lstBienTheGiay = bienTheGiayRepository.getAllByGiayIds(hoaDonFake.getIdsGiay());

        for (int i = 0; i < hoaDonFake.getSoLuongHoaDon(); i++) {

            HoaDon hoaDon = new HoaDon();
            int hour = random.nextInt(24);
            int minute = random.nextInt(60);
            int second = random.nextInt(60);

            LocalDateTime localDateTime = LocalDateTime.of(date, LocalTime.of(hour, minute, second));
            hoaDon.setNgayThanhToan(localDateTime);
            hoaDon.setNgayTao(localDateTime);
            hoaDon.setNgayNhan(localDateTime);
            hoaDon.setNgayShip(localDateTime);
            hoaDon.setTienGiam(BigDecimal.ZERO);
            hoaDon.setTrangThai(hoaDonFake.getTrangThai());

            NhanVien nhanVien = null;
            if (hoaDonFake.getIdNhanVien() != null) {
                nhanVien = NhanVien.builder().id(hoaDonFake.getIdNhanVien()).build();
            }
            hoaDon.setNhanVien(nhanVien);

            KhachHang khachHang = null;
            if (hoaDonFake.getIdKhachHang() != null) {
                khachHang = KhachHang.builder().id(hoaDonFake.getIdKhachHang()).build();
            }
            hoaDon.setKhachHang(khachHang);

            int soLuong = random.nextInt(lstBienTheGiay.size()) + 1;
            Collections.shuffle(lstBienTheGiay);
            BigDecimal tongTien = BigDecimal.ZERO;
            Set<HoaDonChiTiet> hoaDonChiTiets = new HashSet<>();

            for (int j = 0; j < 4 && j < soLuong; j++) {
                HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
                hoaDonChiTiet.setDonGia(lstBienTheGiay.get(j).getGiaBan());
                hoaDonChiTiet.setBienTheGiay(lstBienTheGiay.get(j));
                hoaDonChiTiet.setTrangThai(1);
                hoaDonChiTiet.setSoLuong(random.nextInt(5) + 1);
                hoaDonChiTiet.setHoaDon(hoaDon);
                hoaDonChiTiets.add(hoaDonChiTiet);

                tongTien = tongTien.add(hoaDonChiTiet.getDonGia().multiply(BigDecimal.valueOf(hoaDonChiTiet.getSoLuong())));

            }
            hoaDon.setListHoaDonChiTiet(hoaDonChiTiets);

            Set<ChiTietThanhToan> chiTietThanhToans = new HashSet<>();
            ChiTietThanhToan chiTietThanhToan = ChiTietThanhToan.builder()
                    .tienThanhToan(tongTien)
                    .trangThai(1)
                    .hinhThucThanhToan(1)
                    .hoaDon(hoaDon)
                    .build();

            chiTietThanhToans.add(chiTietThanhToan);
            hoaDon.setChiTietThanhToans(chiTietThanhToans);

            lstHoaDon.add(hoaDon);
        }

        hoaDonRepository.saveAll(lstHoaDon);

    }

}
