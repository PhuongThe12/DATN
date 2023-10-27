package luckystore.datn.repository;

import luckystore.datn.entity.MauSac;
import luckystore.datn.model.response.MauSacResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MauSacRepository extends JpaRepository<MauSac, Long> {

    @Query("select new luckystore.datn.model.response.MauSacResponse(ms) from MauSac ms")
    List<MauSacResponse> findAllResponse();

    @Query("select new luckystore.datn.model.response.MauSacResponse(ms) from MauSac ms " +
            "WHERE (:searchText IS NULL OR ms.ten LIKE %:searchText%) AND (:status IS NULL OR ms.trangThai = :status)")
    Page<MauSacResponse> getPageResponse(String searchText, Integer status, Pageable pageable);

    Boolean existsByTen(String ten);

    Boolean existsByMaMau(String maMau);

    Boolean existsByTenAndIdNot(String ten, Long id);

    Boolean existsByMaMauAndIdNot(String maMau, Long id);

}
