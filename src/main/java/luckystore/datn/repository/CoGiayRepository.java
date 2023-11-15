package luckystore.datn.repository;

import luckystore.datn.entity.CoGiay;
import luckystore.datn.model.response.CoGiayResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface CoGiayRepository extends JpaRepository<CoGiay, Long> {
    @Query("select new luckystore.datn.model.response.CoGiayResponse(cg) from CoGiay cg")
    List<CoGiayResponse> findAllResponse();

    @Query("select new luckystore.datn.model.response.CoGiayResponse(cg) from CoGiay cg " + "WHERE (:searchText IS NULL OR cg.ten LIKE %:searchText%) AND (:status IS NULL OR cg.trangThai = :status)")
    Page<CoGiayResponse> getPageResponse(String searchText, Integer status, Pageable pageable);

    @Query("select new luckystore.datn.model.response.CoGiayResponse(cg) from CoGiay cg " + "where cg.trangThai = 1 order by cg.id desc ")
    List<CoGiayResponse> findAllActive();

    Boolean existsByTen(String ten);

    Boolean existsByTenAndIdNot(String ten, Long id);

    @Query("select new luckystore.datn.model.response.CoGiayResponse(g.id, g.ten) from CoGiay g where g.ten in :names")
    List<CoGiayResponse> getIdsByName(Set<String> names);
}
