package luckystore.datn.repository;

import luckystore.datn.entity.NhanVien;
import luckystore.datn.model.response.MauSacResponse;
import luckystore.datn.model.response.NhanVienResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NhanVienRepository extends JpaRepository<NhanVien, Long> {
    @Query("select new luckystore.datn.model.response.NhanVienResponse(nv) from NhanVien nv")
    List<NhanVienResponse> findAllResponse();

    @Query("select new luckystore.datn.model.response.NhanVienResponse(nv) from NhanVien nv " +
            "WHERE (:searchText IS NULL OR nv.hoTen LIKE %:searchText%) AND (:status IS NULL OR nv.trangThai = :status) AND(:chucVu IS NULL OR nv.chucVu = :chucVu)")
    Page<NhanVienResponse> getPageResponse(String searchText, Integer status, Integer chucVu, Pageable pageable);

    @Query(value = "SELECT * FROM NhanVien WHERE ID_TAI_KHOAN = ?1",nativeQuery = true)
    NhanVien findNhanVienByIdTaiKhoan(Long id);
    Boolean existsByHoTen(String ten);

    Boolean existsBySoDienThoai(String soDienThoai);

    Boolean existsByEmail(String email);

    Boolean existsByHoTenAndIdNot(String ten, Long id);

    NhanVien findNhanVienBySoDienThoai(String soDienThoai);

    NhanVien findNhanVienByEmail(String email);
}
