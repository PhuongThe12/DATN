package luckystore.datn.repository;

import luckystore.datn.entity.HoaDon;
import luckystore.datn.model.request.HoaDonSearch;
import luckystore.datn.model.response.HashTagResponse;
import luckystore.datn.model.response.HoaDonResponse;
import luckystore.datn.model.response.HoaDonYeuCauRespone;
import luckystore.datn.model.response.MuiGiayResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon,Long> {

    @Query("select new luckystore.datn.model.response.HoaDonResponse(hd) from HoaDon hd")
    List<HoaDonResponse> findAllResponse();

    @Query("select new luckystore.datn.model.response.HoaDonResponse(hd) from HoaDon hd " +
            "WHERE (:searchText IS NULL OR hd.ghiChu LIKE %:searchText%) AND (:status IS NULL OR hd.trangThai = :status)")
    Page<HoaDonResponse> getPageResponse(String searchText, Integer status, Pageable pageable);

    @Query("SELECT new luckystore.datn.model.response.HoaDonYeuCauRespone(hd, hd.hoaDonGoc.id)" +
            "FROM HoaDon hd " +
            "LEFT JOIN hd.khachHang kh " +
            "LEFT JOIN hd.nhanVien nv " +
            "INNER JOIN hd.listHoaDonChiTiet hdct " +
            "WHERE (hd.loaiHoaDon = 1 OR hd.loaiHoaDon = 2) "+
            "AND (:#{#hoaDonSearch.idHoaDon} IS NULL OR CAST(hd.id AS string) like %:#{#hoaDonSearch.idHoaDon}%) "+
            "AND (:#{#hoaDonSearch.email} IS NULL OR hd.email like %:#{#hoaDonSearch.email}%) "+
            "AND (:#{#hoaDonSearch.kenhBan} IS NULL OR hd.kenhBan = :#{#hoaDonSearch.kenhBan}) "+
            "AND (:#{#hoaDonSearch.trangThai} IS NULL OR hd.trangThai = :#{#hoaDonSearch.trangThai}) "+
            "AND (:#{#hoaDonSearch.khachHang} IS NULL OR kh.hoTen = :#{#hoaDonSearch.khachHang}) "+
            "AND (:#{#hoaDonSearch.nhanVien} IS NULL OR nv.hoTen = :#{#hoaDonSearch.nhanVien}) "+
            "AND (:#{#hoaDonSearch.giaTu} IS NULL OR (SELECT SUM(ct.donGia * ct.soLuong) FROM HoaDonChiTiet ct WHERE ct.hoaDon = hd) >= :#{#hoaDonSearch.giaTu})"+
            "AND (:#{#hoaDonSearch.giaDen} IS NULL OR (SELECT SUM(ct.donGia * ct.soLuong) FROM HoaDonChiTiet ct WHERE ct.hoaDon = hd) <= :#{#hoaDonSearch.giaDen})"+
            "order by hd.id desc ")
    Page<HoaDonYeuCauRespone> getPageHoaDonYeuCauResponse(HoaDonSearch hoaDonSearch, Pageable pageable);

}


