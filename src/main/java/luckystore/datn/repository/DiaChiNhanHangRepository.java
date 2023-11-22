package luckystore.datn.repository;

import luckystore.datn.entity.DiaChiNhanHang;
import luckystore.datn.model.response.DiaChiNhanHangResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaChiNhanHangRepository extends JpaRepository<DiaChiNhanHang, Long> {

    @Query("select new luckystore.datn.model.response.DiaChiNhanHangResponse (dcn) from DiaChiNhanHang dcn  " +
            "WHERE (:searchText IS NULL OR dcn.hoTen LIKE %:searchText%) AND (:status IS NULL OR dcn.trangThai = :status)AND dcn.idKhachHang.id=5")
    Page<DiaChiNhanHangResponse> getPageResponse(String searchText, Integer status, Pageable pageable);

    Boolean existsByDiaChiNhan(String ten);

    Boolean existsByDiaChiNhanAndIdNot(String ten, Long id);


}
