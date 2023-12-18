package luckystore.datn.repository;

import luckystore.datn.entity.HoaDon;
import luckystore.datn.infrastructure.constraints.TrangThaiHoaDon;
import luckystore.datn.model.request.HoaDonSearch;
import luckystore.datn.model.request.HoaDonSearchP;
import luckystore.datn.model.response.HoaDonBanHangResponse;
import luckystore.datn.model.response.HoaDonResponse;
import luckystore.datn.model.response.HoaDonYeuCauRespone;
import luckystore.datn.model.response.print.HoaDonPrintResponse;
import luckystore.datn.model.response.thongKe.ThongKeByHangAndThuongHieu;
import luckystore.datn.model.response.thongKe.ThongKeHoaDon;
import luckystore.datn.model.response.thongKe.ThongKeTongQuan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Long> {

    @Query("select new luckystore.datn.model.response.HoaDonResponse(hd) from HoaDon hd")
    List<HoaDonResponse> findAllResponse();

    @Query("select new luckystore.datn.model.response.HoaDonResponse(hd) from HoaDon hd " +
            "WHERE (:searchText IS NULL OR hd.ghiChu LIKE %:searchText%) AND (:status IS NULL OR hd.trangThai = :status)")
    Page<HoaDonResponse> getPageResponse(String searchText, Integer status, Pageable pageable);

    @Query("SELECT new luckystore.datn.model.response.HoaDonYeuCauRespone(hd, 'getAllYeuCau') " +
            "FROM HoaDon hd " +
            "LEFT JOIN hd.khachHang kh on hd.khachHang.id = kh.id " +
            "LEFT JOIN hd.nhanVien nv on hd.nhanVien.id = nv.id " +
            "WHERE (hd.loaiHoaDon = 1 or hd.loaiHoaDon = 2 and hd.trangThai = 1) " +
            "AND (:#{#hoaDonSearch.idHoaDon} IS NULL OR hd.id  = :#{#hoaDonSearch.idHoaDon}) " +
            "AND (:#{#hoaDonSearch.loaiHoaDon} IS NULL OR hd.loaiHoaDon = :#{#hoaDonSearch.loaiHoaDon}) " +
            "AND (:#{#hoaDonSearch.email} IS NULL OR hd.email like %:#{#hoaDonSearch.email}%) " +
            "AND (:#{#hoaDonSearch.soDienThoaiKhachHang} IS NULL OR kh.soDienThoai like %:#{#hoaDonSearch.soDienThoaiKhachHang}%) " +
            "AND (:#{#hoaDonSearch.kenhBan} IS NULL OR hd.kenhBan = :#{#hoaDonSearch.kenhBan}) " +
            "AND (:#{#hoaDonSearch.tenKhachHang} IS NULL OR kh.hoTen like %:#{#hoaDonSearch.tenKhachHang}%) " +
            "AND (:#{#hoaDonSearch.idNhanVien} IS NULL OR nv.id = :#{#hoaDonSearch.idNhanVien}) " +
            "AND (:#{#hoaDonSearch.ngayBatDau} IS NULL OR hd.ngayTao >= :#{#hoaDonSearch.ngayBatDau}) " +
            "AND (:#{#hoaDonSearch.ngayKetThuc} IS NULL OR hd.ngayTao <= :#{#hoaDonSearch.ngayKetThuc}) " +
            "AND (:#{#hoaDonSearch.tongThanhToanMin} IS NULL OR (SELECT SUM(cttt.tienThanhToan) FROM ChiTietThanhToan cttt WHERE cttt.hoaDon.id = hd.id) >= :#{#hoaDonSearch.tongThanhToanMin}) " +
            "AND (:#{#hoaDonSearch.tongThanhToanMax} IS NULL OR (SELECT SUM(cttt.tienThanhToan) FROM ChiTietThanhToan cttt WHERE cttt.hoaDon.id = hd.id) <= :#{#hoaDonSearch.tongThanhToanMax})")
    Page<HoaDonYeuCauRespone> getPageHoaDonYeuCauResponse(HoaDonSearch hoaDonSearch, Pageable pageable);


    @Query("SELECT new luckystore.datn.model.response.HoaDonBanHangResponse(hd.id)  FROM HoaDon hd " +
            "where hd.trangThai = " + TrangThaiHoaDon.CHUA_THANH_TOAN +
            " and hd.nhanVien != null " +
            " order by hd.ngayTao desc")
    List<HoaDonBanHangResponse> getAllChuaThanhToanBanHang();

    @Query("SELECT new luckystore.datn.model.response.HoaDonYeuCauRespone(hd) FROM HoaDon hd WHERE hd.id = :id")
    HoaDonYeuCauRespone getOneHoaDonYeuCau(Long id);

    @Query("SELECT new luckystore.datn.model.response.HoaDonBanHangResponse(hd.id, hdct, hd.trangThai)  FROM HoaDon hd " +
            "left join hd.listHoaDonChiTiet hdct " +
            "where hd.id = :id ")
    List<HoaDonBanHangResponse> getAllById(Long id);

    @Query("SELECT hd.id FROM HoaDon hd where hd.id = :id")
    Long getIdById(Long id);

    @Query("select new luckystore.datn.model.response.HoaDonYeuCauRespone(hd, hd.hoaDonGoc) from HoaDon hd " +
            "WHERE hd.id = :id")
    HoaDonYeuCauRespone getHoaDonYeuCauResponse(Long id);

    @Query("select new luckystore.datn.model.response.HoaDonResponse(hd) from HoaDon hd " +
            "WHERE (:searchText IS NULL OR hd.ghiChu LIKE %:searchText%) AND (:status IS NULL OR hd.trangThai = :status)" +
            "AND hd.khachHang.id = :idKhachHang")
    Page<HoaDonResponse> getPageResponseByIdKhachHang(String searchText, Integer status, Pageable pageable, Long idKhachHang);

    @Query("select new luckystore.datn.model.response.print.HoaDonPrintResponse(hd) from HoaDon hd where hd.id = :id")
    HoaDonPrintResponse getPrint(Long id);

    @Query("select new luckystore.datn.model.response.print.HoaDonPrintResponse(hd, 2) from HoaDon hd " +
            "where (:#{#hoaDonSearch.id} is null or hd.id = :#{#hoaDonSearch.id}) " +
            "and (:#{#hoaDonSearch.denNgay} is null or hd.ngayTao <= :#{#hoaDonSearch.denNgay}) " +
            "and (:#{#hoaDonSearch.tuNgay} is null or hd.ngayTao >= :#{#hoaDonSearch.tuNgay}) " +
            "and hd.trangThai = :#{#hoaDonSearch.trangThai} order by hd.ngayTao desc")
    Page<HoaDonPrintResponse> getAllBySearch(HoaDonSearchP hoaDonSearch, Pageable pageable);

    @Query("select new luckystore.datn.model.response.print.HoaDonPrintResponse(hd, 2) from HoaDon hd " +
            "where (:#{#hoaDonSearch.id} is null or hd.id = :#{#hoaDonSearch.id}) " +
            "and (:#{#hoaDonSearch.denNgay} is null or hd.ngayShip <= :#{#hoaDonSearch.denNgay}) " +
            "and (:#{#hoaDonSearch.tuNgay} is null or hd.ngayShip >= :#{#hoaDonSearch.tuNgay}) " +
            "and hd.trangThai = :#{#hoaDonSearch.trangThai} order by hd.ngayShip desc")
    Page<HoaDonPrintResponse> getAllBySearchOrderNgayShip(HoaDonSearchP hoaDonSearch, Pageable pageable);


    @Query("select new luckystore.datn.model.response.print.HoaDonPrintResponse(hd, 2) from HoaDon hd " +
            "where (:#{#hoaDonSearch.id} is null or hd.id = :#{#hoaDonSearch.id}) " +
            "and (:#{#hoaDonSearch.denNgay} is null or hd.ngayThanhToan <= :#{#hoaDonSearch.denNgay}) " +
            "and (:#{#hoaDonSearch.tuNgay} is null or hd.ngayThanhToan >= :#{#hoaDonSearch.tuNgay}) " +
            "and hd.trangThai = :#{#hoaDonSearch.trangThai} order by hd.ngayThanhToan desc")
    Page<HoaDonPrintResponse> getAllBySearchOrderNgayThanhToan(HoaDonSearchP hoaDonSearch, Pageable pageable);


    @Query("select hd from HoaDon hd where hd.id in :ids")
    List<HoaDon> getAllByIds(List<Long> ids);

    @Query("select hd from HoaDon hd where hd.hoaDonGoc = :id")
    List<HoaDon> getHoaDonDoiTra(Long id);

    @Query("select hd.id from HoaDon hd where hd.trangThai = 0 and hd.nhanVien.id is null and hd.ngayThanhToan < :current")
    List<Long> getHoadonChuaHoanThanh(LocalDateTime current);


    @Transactional
    @Modifying
    @Query(value = "update HoaDon hd set hd.trangThai = :daHuy, hd.ghiChu = :s where hd.id = :id")
    void cancelHoaDon(@Param("id") Long id, @Param("daHuy") int daHuy, @Param("s") String s);

    @Query("select new luckystore.datn.model.response.print.HoaDonPrintResponse(hd, 2) " +
            "from HoaDon hd where hd.id = :maHD and hd.soDienThoaiNhan like %:sdt")
    HoaDonPrintResponse getTraCuuDon(Long maHD, String sdt);

    @Query(value = """
            SELECT hkh.TEN_HANG AS 'ten', SUM(sub.TIEN_THANH_TOAN) AS 'tongDoanhThu'
        FROM (
            SELECT hd.ID_KHACH_HANG, cttt.TIEN_THANH_TOAN
            FROM
            HoaDon hd
            LEFT JOIN ChiTietThanhToan cttt ON hd.ID = cttt.ID_HOA_DON
            WHERE hd.TRANG_THAI = 1 AND hd.NGAY_TAO BETWEEN '2023-12-17' AND '2023-12-19'
        ) AS sub
        LEFT JOIN
            KhachHang kh ON sub.ID_KHACH_HANG = kh.ID
        LEFT JOIN HangKhachHang hkh ON kh.ID_HANG_KHACH_HANG = hkh.ID
            GROUP BY hkh.TEN_HANG;
            """, nativeQuery = true)
    List<ThongKeByHangAndThuongHieu> getDoanhThuByHangKhachHang();

    @Query(value = """
        		SELECT th.TEN AS 'ten', SUM(cttt.TIEN_THANH_TOAN) AS 'tongDoanhThu'
        FROM HoaDon hd
        LEFT JOIN HoaDonChiTiet hdct ON hd.ID = hdct.ID_HOA_DON
        LEFT JOIN ChiTietThanhToan cttt ON hd.ID = cttt.ID_HOA_DON
        LEFT JOIN (
            SELECT hdct.ID_HOA_DON, g.ID_THUONG_HIEU
            FROM HoaDonChiTiet hdct
            LEFT JOIN BienTheGiay btg ON btg.ID = hdct.ID_BIEN_THE_GIAY
            LEFT JOIN Giay g ON g.ID = btg.ID_GIAY
        ) AS sub ON hd.ID = sub.ID_HOA_DON
        LEFT JOIN ThuongHieu th ON th.ID = sub.ID_THUONG_HIEU
        WHERE hd.TRANG_THAI = 1 AND hd.NGAY_TAO BETWEEN '2023-12-17' AND '2023-12-19'
        GROUP BY th.TEN
    """, nativeQuery = true)
    List<ThongKeByHangAndThuongHieu> getDoanhThuByThuongHieu();

    @Query(value = """
        select MONTH(NGAY_TAO) as 'ten', sum(cttt.TIEN_THANH_TOAN) as 'tongDoanhThu' from HoaDon hd
        left join HoaDonChiTiet hdct on hd.ID = hdct.ID_HOA_DON
        left join ChiTietThanhToan cttt on hd.ID = cttt.ID_HOA_DON
        where YEAR(hd.NGAY_TAO) = :year AND hd.TRANG_THAI = 1
        group by MONTH(NGAY_TAO)
        order by MONTH(NGAY_TAO)
    """, nativeQuery = true)
    List<ThongKeByHangAndThuongHieu> getThongKeTheoNam(@Param("year") Integer year);

    @Query(value = """
            select sum(cttt.TIEN_THANH_TOAN) as 'tongDoanhThu',
                   count(DISTINCT hd.ID) as 'tongHoaDon',
                   sum(hdct.SO_LUONG) as 'tongSanPham'
               from HoaDon hd
                left join HoaDonChiTiet hdct on hd.ID = hdct.ID_HOA_DON
                left join ChiTietThanhToan cttt on hd.ID = cttt.ID_HOA_DON
                where CONVERT(DATE, hd.NGAY_TAO) = :ngay1 AND hd.TRANG_THAI = 1
            """, nativeQuery = true)
    ThongKeTongQuan getThongKeTongQuan(@Param("ngay1") Date ngay1);
    @Query("select new luckystore.datn.model.response.print.HoaDonPrintResponse(hd, 2) " +
            "from HoaDon hd where hd.id = :maHD ")
    HoaDonPrintResponse getThanhToanChiTiet(Long maHD);

    @Query(value = """
        SELECT
            CAST(hd.NGAY_TAO AS DATE) AS 'ngay',
            COUNT(DISTINCT hd.ID) AS 'soLuongHoaDon',
            SUM(hdct.SO_LUONG) AS 'soLuongSanPham',
            SUM(cttt.TIEN_THANH_TOAN) AS 'tongTien',
            nv.HO_TEN
        FROM HoaDon hd
        LEFT JOIN HoaDonChiTiet hdct ON hd.ID = hdct.ID_HOA_DON
        LEFT JOIN ChiTietThanhToan cttt ON cttt.ID_HOA_DON = hd.ID
        LEFT JOIN NhanVien nv ON nv.ID = hd.ID_NHAN_VIEN
        WHERE CAST(hd.NGAY_TAO AS DATE) BETWEEN :ngay1 AND :ngay2
        GROUP BY CAST(hd.NGAY_TAO AS DATE), nv.HO_TEN
    """, nativeQuery = true)
    Page<ThongKeHoaDon> getThongKeHoaDon(Date ngay1, Date ngay2,Pageable pageable);
}


