package luckystore.datn.repository;

import luckystore.datn.entity.DiaChiNhanHang;
import luckystore.datn.model.response.DiaChiNhanHangResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiaChiNhanHangRepository extends JpaRepository<DiaChiNhanHang, Long> {
    @Modifying
    @Query("UPDATE DiaChiNhanHang d SET d.trangThai = :newTrangThai WHERE d.trangThai = :oldTrangThai")
    void updateTrangThaiForAllWithTrangThai(@Param("oldTrangThai") int oldTrangThai, @Param("newTrangThai") int newTrangThai);

    @Query("SELECT new luckystore.datn.model.response.DiaChiNhanHangResponse(dcn) " +
            "FROM DiaChiNhanHang dcn " +
            "WHERE (:searchText IS NULL OR dcn.hoTen LIKE %:searchText%) " +
            "AND (:status IS NULL OR dcn.trangThai = :status) " +
            "AND dcn.idKhachHang.id = 5 " +
            "ORDER BY CASE WHEN dcn.trangThai = 1 THEN 0 ELSE 1 END, dcn.ngayTao DESC")
    Page<DiaChiNhanHangResponse> getPageResponse(String searchText, Integer status, Pageable pageable);

    List<DiaChiNhanHang> findByTrangThaiNot(int trangThai);

    Boolean existsByDiaChiNhan(String ten);

    Boolean existsByDiaChiNhanAndIdNot(String ten, Long id);


}
