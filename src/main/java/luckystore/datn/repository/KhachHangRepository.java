package luckystore.datn.repository;

import luckystore.datn.entity.KhachHang;
import luckystore.datn.model.response.KhachHangResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KhachHangRepository extends JpaRepository<KhachHang,Long> {
    @Query("select new luckystore.datn.model.response.KhachHangResponse(kh) from KhachHang kh")
    List<KhachHangResponse> findAllResponse();

    @Query("select new luckystore.datn.model.response.KhachHangResponse(kh) from KhachHang kh " +
            "WHERE (:searchText IS NULL OR kh.hoTen LIKE %:searchText%) AND (:status IS NULL OR kh.trangThai = :status)")
    Page<KhachHangResponse> getPageResponse(String searchText, Integer status, Pageable pageable);

    Boolean existsByHoTen(String ten);

    Boolean existsByHoTenAndIdNot(String ten, Long id);

    Optional<KhachHang> findByEmail(String email);

//    fig tạm khách hàng id =5
    @Query(value = "SELECT * FROM KhachHang WHERE ID=5" , nativeQuery = true)
    KhachHang findIdKH(KhachHang khachHang);

    @Query("select new luckystore.datn.model.response.KhachHangResponse(kh) from KhachHang kh " +
            "where kh.hoTen like %:searchText% or kh.soDienThoai like %:searchText% or kh.email like %:searchText%")
    List<KhachHangResponse> searchByName(String searchText);

    @Query("select hd.khachHang from HoaDon hd where hd.id = :id")
    KhachHang findByHDId(Long id);

    Boolean existsByEmail(String email);

    Boolean existsByEmailAndIdNot(String email, Long id);

    KhachHang getKhachHangByTaiKhoanId(Long idTaiKhoan);

    @Query("select count(tk.id) > 0 from TaiKhoan tk where tk.tenDangNhap = :email and tk.trangThai = 1")
    boolean existsByEmailAndTrangThai(String email);

    @Query("select kh from KhachHang kh where kh.email = :email")
    KhachHang getByEmail(String email);
    @Query("select kh from KhachHang kh where kh.email = :email and kh.soDienThoai = :sdt")
    Optional<KhachHang> findByEmailAndSdt(String email, String sdt);
}
