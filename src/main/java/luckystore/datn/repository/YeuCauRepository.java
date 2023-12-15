package luckystore.datn.repository;



import luckystore.datn.entity.YeuCau;
import luckystore.datn.model.request.YeuCauSearch;
import luckystore.datn.model.response.YeuCauResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface YeuCauRepository extends JpaRepository<YeuCau,Long> {
    @Query("select new luckystore.datn.model.response.YeuCauResponse(yc) from YeuCau yc")
    List<YeuCauResponse> finAllResponse();

    @Query("SELECT new luckystore.datn.model.response.YeuCauResponse(yc) FROM YeuCau yc " +
            "left JOIN yc.hoaDon hd on yc.hoaDon.id = yc.id " +
            "left JOIN hd.khachHang kh on hd.khachHang.id = kh.id " +
            "left JOIN hd.nhanVien nv on hd.nhanVien.id = nv.id " +
            "WHERE (:#{#yeuCauSearch.ngayBatDau} is NULL or yc.ngayTao >= :#{#yeuCauSearch.ngayBatDau}) " +
            "AND (:#{#yeuCauSearch.ngayKetThuc} is NULL or yc.ngayTao <= :#{#yeuCauSearch.ngayKetThuc})" +
            "AND (:#{#yeuCauSearch.trangThai} is NULL or yc.trangThai = :#{#yeuCauSearch.trangThai}) " +
            "AND (:#{#yeuCauSearch.idYeuCau} IS NULL OR yc.id = :#{#yeuCauSearch.idYeuCau}) " +
            "AND (:#{#yeuCauSearch.idNhanVien} IS NULL OR nv.id = :#{#yeuCauSearch.idNhanVien}) " +
            "AND (:#{#yeuCauSearch.tenKhachHang} IS NULL OR kh.hoTen like %:#{#yeuCauSearch.tenKhachHang}%) " +
            "AND (:#{#yeuCauSearch.soDienThoaiKhachHang} IS NULL OR kh.soDienThoai like %:#{#yeuCauSearch.soDienThoaiKhachHang}%) " +
            "AND (:#{#yeuCauSearch.email} IS NULL OR kh.email like %:#{#yeuCauSearch.email}%) " +
            "ORDER BY yc.id DESC" )
    Page<YeuCauResponse> getPageResponse(Pageable pageable, YeuCauSearch yeuCauSearch);

    @Query("select new luckystore.datn.model.response.YeuCauResponse(yc,'getListYeuCauKhachHang') from YeuCau yc where yc.hoaDon.id = :idHoaDon")
    List<YeuCauResponse> getListYeuCau(Long idHoaDon);

    @Query("select new luckystore.datn.model.response.YeuCauResponse(yc) from YeuCau yc  where yc.trangThai = 0")
    YeuCauResponse findResponseByStatus();

}
