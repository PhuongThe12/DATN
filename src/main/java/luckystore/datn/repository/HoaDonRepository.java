package luckystore.datn.repository;

import luckystore.datn.entity.HoaDon;
import luckystore.datn.model.request.HoaDonSearch;
import luckystore.datn.model.response.HashTagResponse;
import luckystore.datn.model.response.HoaDonBanHangResponse;
import luckystore.datn.model.response.HoaDonResponse;
import luckystore.datn.model.response.HoaDonYeuCauRespone;
import luckystore.datn.model.response.MuiGiayResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon,Long> {

    @Query("select new luckystore.datn.model.response.HoaDonResponse(hd) from HoaDon hd")
    List<HoaDonResponse> findAllResponse();

    @Query("select new luckystore.datn.model.response.HoaDonResponse(hd) from HoaDon hd " +
            "WHERE (:searchText IS NULL OR hd.ghiChu LIKE %:searchText%) AND (:status IS NULL OR hd.trangThai = :status)")
    Page<HoaDonResponse> getPageResponse(String searchText, Integer status, Pageable pageable);

    @Query("SELECT new luckystore.datn.model.response.HoaDonYeuCauRespone(hd, hd.hoaDonGoc.id)" +
            "FROM HoaDon hd " +
            "left JOIN hd.khachHang kh on hd.khachHang.id = kh.id " +
            "left JOIN hd.nhanVien nv on hd.nhanVien.id = nv.id " +
            "WHERE (hd.loaiHoaDon = 1 OR hd.loaiHoaDon = 2) "+
            "AND (:#{#hoaDonSearch.idHoaDon} IS NULL OR hd.id  = :#{#hoaDonSearch.idHoaDon}) "+
            "AND (:#{#hoaDonSearch.loaiHoaDon} IS NULL OR hd.loaiHoaDon = :#{#hoaDonSearch.loaiHoaDon}) "+
            "AND (:#{#hoaDonSearch.email} IS NULL OR hd.email like %:#{#hoaDonSearch.email}%) "+
            "AND (:#{#hoaDonSearch.soDienThoaiKhacHang} IS NULL OR kh.soDienThoai like %:#{#hoaDonSearch.soDienThoaiKhacHang}%) "+
            "AND (:#{#hoaDonSearch.kenhBan} IS NULL OR hd.kenhBan = :#{#hoaDonSearch.kenhBan}) "+
            "AND (:#{#hoaDonSearch.trangThai} IS NULL OR hd.trangThai = :#{#hoaDonSearch.trangThai}) "+
            "AND (:#{#hoaDonSearch.khachHang} IS NULL OR kh.hoTen like %:#{#hoaDonSearch.khachHang}%) "+
            "AND (:#{#hoaDonSearch.nhanVien} IS NULL OR nv.hoTen like %:#{#hoaDonSearch.nhanVien}%) " +
            "AND (:#{#hoaDonSearch.ngayBatDau} IS NULL OR hd.ngayTao >= :#{#hoaDonSearch.ngayBatDau}) "+
            "AND (:#{#hoaDonSearch.ngayKetThuc} IS NULL OR hd.ngayTao <= :#{#hoaDonSearch.ngayKetThuc}) "
    )
    Page<HoaDonYeuCauRespone> getPageHoaDonYeuCauResponse(HoaDonSearch hoaDonSearch, Pageable pageable);

    @Query("SELECT new luckystore.datn.model.response.HoaDonBanHangResponse(hd.id)  FROM HoaDon hd " +
            "where hd.trangThai = " + TrangThaiHoaDon.CHUA_THANH_TOAN +
        // " and hd.nhanVien != null "
            " order by hd.ngayTao desc")
    List<HoaDonBanHangResponse> getAllChuaThanhToanBanHang();

    @Query("SELECT new luckystore.datn.model.response.HoaDonYeuCauRespone(hd) FROM HoaDon hd WHERE hd.id = :id")
    HoaDonYeuCauRespone getOneHoaDonYeuCau(Long id);

    @Query("SELECT new luckystore.datn.model.response.HoaDonBanHangResponse(hd.id, hdct, hd.trangThai)  FROM HoaDon hd " +
            "left join hd.listHoaDonChiTiet hdct " +
            "where hd.id = :id ")
    List<HoaDonBanHangResponse> getAllById(Long id);

    @Query("SELECT new luckystore.datn.model.response.HoaDonBanHangResponse(hd.id)  FROM HoaDon hd " +
            "where hd.trangThai = " + TrangThaiHoaDon.CHUA_THANH_TOAN +
            " order by hd.ngayTao desc")
    List<HoaDonBanHangResponse> getAllChuaThanhToan();

    @Query("SELECT new luckystore.datn.model.response.HoaDonBanHangResponse(hd.id)  FROM HoaDon hd " +
            "where hd.trangThai = " + TrangThaiHoaDon.DA_HUY +
            " order by hd.ngayTao desc")
    List<HoaDonBanHangResponse> getAllDaHuy();

    @Query("SELECT new luckystore.datn.model.response.HoaDonBanHangResponse(hd.id)  FROM HoaDon hd " +
            "where hd.trangThai = " + TrangThaiHoaDon.DA_THANH_TOAN +
            " order by hd.ngayTao desc")
    List<HoaDonBanHangResponse> getAllDaThanhToan();

    @Query("SELECT new luckystore.datn.model.response.HoaDonBanHangResponse(hd.id)  FROM HoaDon hd " +
            "where hd.trangThai = " + TrangThaiHoaDon.CHO_GIAO_HANG +
            " order by hd.ngayTao desc")
    List<HoaDonBanHangResponse> getAllChoGiaoHang();

    @Query("SELECT new luckystore.datn.model.response.HoaDonBanHangResponse(hd.id)  FROM HoaDon hd " +
            "where hd.trangThai = " + TrangThaiHoaDon.CHO_XAC_NHAN +
            " order by hd.ngayTao desc")
    List<HoaDonBanHangResponse> getAllChoXacNhan();

    @Query("SELECT new luckystore.datn.model.response.HoaDonBanHangResponse(hd.id)  FROM HoaDon hd " +
            "where hd.trangThai = " + TrangThaiHoaDon.HOAN_HANG +
            " order by hd.ngayTao desc")
    List<HoaDonBanHangResponse> getAllHoanHang();

    @Query("SELECT new luckystore.datn.model.response.HoaDonBanHangResponse(hd.id)  FROM HoaDon hd " +
            "where hd.trangThai = " + TrangThaiHoaDon.DA_HOAN_HANG +
            " order by hd.ngayTao desc")
    List<HoaDonBanHangResponse> getAllDaHoanHang();

    @Query("SELECT hd.id FROM HoaDon hd where hd.id = :id")
    Long getIdById(Long id);

    @Query("select new luckystore.datn.model.response.HoaDonYeuCauRespone(hd, hd.hoaDonGoc.id) from HoaDon hd " +
            "WHERE hd.id = :id")
    HoaDonYeuCauRespone getHoaDonYeuCauResponse(Long id);

    @Query("select new luckystore.datn.model.response.HoaDonResponse(hd) from HoaDon hd " +
            "WHERE (:searchText IS NULL OR hd.ghiChu LIKE %:searchText%) AND (:status IS NULL OR hd.trangThai = :status)" +
            "AND hd.khachHang.id = :idKhachHang")
    Page<HoaDonResponse> getPageResponseByIdKhachHang(String searchText, Integer status, Pageable pageable, Long idKhachHang);

    @Query("select new luckystore.datn.model.response.print.HoaDonPrintResponse(hd) from HoaDon hd where hd.id = :id")
    HoaDonPrintResponse getPrint(Long id);
}


