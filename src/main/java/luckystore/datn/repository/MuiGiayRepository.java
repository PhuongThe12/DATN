package luckystore.datn.repository;

import luckystore.datn.entity.MuiGiay;
import luckystore.datn.model.response.MuiGiayResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MuiGiayRepository extends JpaRepository<MuiGiay, Long> {
    @Query("select new luckystore.datn.model.response.MuiGiayResponse(mg) from MuiGiay mg")
    List<MuiGiayResponse> findAllResponse();

    @Query("select new luckystore.datn.model.response.MuiGiayResponse(mg) from MuiGiay mg " +
            "WHERE (:searchText IS NULL OR mg.ten LIKE %:searchText%) AND (:status IS NULL OR mg.trangThai = :status)")
    Page<MuiGiayResponse> getPageResponse(String searchText, Integer status, Pageable pageable);

    @Query("select new luckystore.datn.model.response.MuiGiayResponse(mg) from MuiGiay mg " +
            "where mg.trangThai = 1 order by mg.id desc ")
    List<MuiGiayResponse> findAllActive();

    Boolean existsByTen(String ten);

    Boolean existsByTenAndIdNot(String ten, Long id);

    @Query("select new luckystore.datn.model.response.MuiGiayResponse(g.id, g.ten) from MuiGiay g where g.ten in :names")
    List<MuiGiayResponse> getIdsByName(Set<String> names);
}
