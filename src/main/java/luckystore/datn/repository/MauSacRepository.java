package luckystore.datn.repository;

import luckystore.datn.entity.MauSac;
import luckystore.datn.model.response.MauSacResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MauSacRepository extends JpaRepository<MauSac, Long> {

    @Query("select new luckystore.datn.model.response.MauSacResponse(ms) from MauSac ms")
    List<MauSacResponse> findAllResponse();

    @Query("select new luckystore.datn.model.response.MauSacResponse(ms) from MauSac ms " +
            "WHERE (:searchText IS NULL OR ms.ten LIKE %:searchText%) AND (:status IS NULL OR ms.trangThai = :status) order by ms.id desc")
    Page<MauSacResponse> getPageResponse(String searchText, Integer status, Pageable pageable);

    @Query("select new luckystore.datn.model.response.MauSacResponse(ms) from MauSac ms " +
            "where ms.trangThai = 1 order by ms.id desc ")
    List<MauSacResponse> findAllActive();


    Boolean existsByTen(String ten);

    Boolean existsByMaMau(String maMau);

    Boolean existsByTenAndIdNot(String ten, Long id);

    Boolean existsByMaMauAndIdNot(String maMau, Long id);

    @Query("select new luckystore.datn.model.response.MauSacResponse(g.id, g.ten) from MauSac g where g.ten in :names")
    List<MauSacResponse> getIdsByName(Set<String> names);

}
