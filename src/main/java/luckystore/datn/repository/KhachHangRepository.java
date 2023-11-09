package luckystore.datn.repository;

import luckystore.datn.entity.DiaChiNhanHang;
import luckystore.datn.entity.HangKhachHang;
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
}
