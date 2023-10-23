package luckystore.datn.repository;

import luckystore.datn.entity.KichThuoc;
import luckystore.datn.model.response.KichThuocResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KichThuocRepository extends JpaRepository<KichThuoc, Long> {

    @Query("select new luckystore.datn.model.response.KichThuocResponse(kt) from KichThuoc kt")
    List<KichThuocResponse> findAllResponse();

    @Query("select new luckystore.datn.model.response.KichThuocResponse(kt) from KichThuoc kt " +
            "WHERE (:searchText IS NULL OR kt.ten LIKE %:searchText%) AND (:status IS NULL OR kt.trangThai = :status)")
    Page<KichThuocResponse> getPageResponse(String searchText, Integer status, Pageable pageable);

    Boolean existsByTen(String ten);

    Boolean existsByTenAndIdNot(String ten, Long id);
}
