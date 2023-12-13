package luckystore.datn.repository;

import luckystore.datn.infrastructure.constraints.TrangThaiHoaDon;
import luckystore.datn.entity.HoaDon;
import luckystore.datn.model.request.HoaDonSearch;
import luckystore.datn.model.request.HoaDonSearchP;
import luckystore.datn.model.request.ThongKeRequest;
import luckystore.datn.model.response.HoaDonBanHangResponse;
import luckystore.datn.model.response.HoaDonResponse;
import luckystore.datn.model.response.HoaDonYeuCauRespone;
import luckystore.datn.model.response.print.HoaDonPrintResponse;
import luckystore.datn.model.response.thongKe.HoaDonThongKe;
import luckystore.datn.model.response.thongKe.ThongKeTheoNam;
import luckystore.datn.model.response.thongKe.ThongKeTongQuan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    @Query("SELECT new luckystore.datn.model.response.HoaDonYeuCauRespone(hd, 'getAllYeuCau' )" +
            "FROM HoaDon hd " +
            "left JOIN hd.khachHang kh on hd.khachHang.id = kh.id " +
            "left JOIN hd.nhanVien nv on hd.nhanVien.id = nv.id " +
            "WHERE (hd.loaiHoaDon = 1 OR hd.loaiHoaDon = 2) " +
            "AND (:#{#hoaDonSearch.idHoaDon} IS NULL OR hd.id  = :#{#hoaDonSearch.idHoaDon}) " +
            "AND (:#{#hoaDonSearch.loaiHoaDon} IS NULL OR hd.loaiHoaDon = :#{#hoaDonSearch.loaiHoaDon}) " +
            "AND (:#{#hoaDonSearch.email} IS NULL OR hd.email like %:#{#hoaDonSearch.email}%) " +
            "AND (:#{#hoaDonSearch.soDienThoaiKhacHang} IS NULL OR kh.soDienThoai like %:#{#hoaDonSearch.soDienThoaiKhacHang}%) " +
            "AND (:#{#hoaDonSearch.kenhBan} IS NULL OR hd.kenhBan = :#{#hoaDonSearch.kenhBan}) " +
            "AND (:#{#hoaDonSearch.trangThai} IS NULL OR hd.trangThai = :#{#hoaDonSearch.trangThai}) " +
            "AND (:#{#hoaDonSearch.khachHang} IS NULL OR kh.hoTen like %:#{#hoaDonSearch.khachHang}%) " +
            "AND (:#{#hoaDonSearch.nhanVien} IS NULL OR nv.hoTen like %:#{#hoaDonSearch.nhanVien}%) " +
            "AND (:#{#hoaDonSearch.ngayBatDau} IS NULL OR hd.ngayTao >= :#{#hoaDonSearch.ngayBatDau}) " +
            "AND (:#{#hoaDonSearch.ngayKetThuc} IS NULL OR hd.ngayTao <= :#{#hoaDonSearch.ngayKetThuc}) "
    )
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

    @Query(value = """
        select count(distinct hd.ID) as 'Tổng hóa đơn',
        	   sum(hdct.SO_LUONG) as 'Tổng sản phẩm' ,
        	   sum(cttt.TIEN_THANH_TOAN) as 'Tổng doanh thu' ,
        	   count(distinct yc.ID) as 'Tổng yêu cầu đổi trả'from HoaDon hd\s
        left join HoaDonChiTiet hdct on hd.ID = hdct.ID_HOA_DON
        left join ChiTietThanhToan cttt on hd.ID = cttt.ID_HOA_DON
        left join YeuCau yc on yc.ID_HOA_DON = hd.ID
        where hd.NGAY_TAO >= :#{#request.startDate} and hd.NGAY_TAO <= :#{#request.endDate}
    """, nativeQuery = true)
    ThongKeTongQuan getThongKeTongQuan(ThongKeRequest request);

    @Query(value = """
        select kh.HO_TEN, cttt.TIEN_THANH_TOAN, hd.NGAY_TAO, hd.TRANG_THAI from HoaDon hd\s
        left join KhachHang kh on kh.ID = hd.ID_KHACH_HANG
        left join HoaDonChiTiet hdct on hd.ID = hdct.ID_HOA_DON
        left join ChiTietThanhToan cttt on hd.ID = cttt.ID_HOA_DON\s
        where hd.NGAY_TAO >= :#{#request.startDate} and hd.NGAY_TAO <= :#{#request.endDate}
        order by hd.NGAY_TAO desc
    """, nativeQuery = true)
    List<HoaDonThongKe> getListHoaDonThongKe(ThongKeRequest request);

    @Query(value = """
        select MONTH(NGAY_TAO) as 'Thang', sum(hdct.SO_LUONG) as 'TONG_SAN_PHAM',\s
        sum(cttt.TIEN_THANH_TOAN) as 'TONG_TIEN' from HoaDon hd
        left join HoaDonChiTiet hdct on hd.ID = hdct.ID_HOA_DON
        left join ChiTietThanhToan cttt on hd.ID = cttt.ID_HOA_DON
        where YEAR(NGAY_TAO) = :year AND hd.TRANG_THAI = 1
        group by MONTH(NGAY_TAO)
        order by MONTH(NGAY_TAO)
    """, nativeQuery = true)
    List<ThongKeTheoNam> getThongKeTheoNam(@Param("year") Integer year);

}


