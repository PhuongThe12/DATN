package luckystore.datn.repository;

import luckystore.datn.entity.KhachHang;
import luckystore.datn.model.response.KhachHangRestponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang,Long> {
    @Query("select new luckystore.datn.model.response.KhachHangRestponse(kh) from KhachHang kh")
    List<KhachHangRestponse> findAllResponse();

    @Query("select new luckystore.datn.model.response.KhachHangRestponse(kh) from KhachHang kh " +
            "WHERE (:searchText IS NULL OR kh.hoTen LIKE %:searchText%) AND (:status IS NULL OR kh.trangThai = :status)")
    Page<KhachHangRestponse> getPageResponse(String searchText, Integer status, Pageable pageable);

    Boolean existsByHoTen(String ten);

    Boolean existsByHoTenAndIdNot(String ten, Long id);

    @Query("select new luckystore.datn.model.response.KhachHangRestponse(kh) from KhachHang kh " +
            "where kh.hoTen like %:searchText% or kh.soDienThoai like %:searchText%")
    List<KhachHangRestponse> searchByName(String searchText);

    @Query("select hd.khachHang from HoaDon hd where hd.id = :id")
    KhachHang findByHDId(Long id);
}
