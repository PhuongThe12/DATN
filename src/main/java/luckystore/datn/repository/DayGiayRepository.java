package luckystore.datn.repository;

import luckystore.datn.entity.DayGiay;
import luckystore.datn.model.response.DayGiayResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DayGiayRepository extends JpaRepository<DayGiay, Long> {
    @Query("select new luckystore.datn.model.response.DayGiayResponse(dg) from DayGiay dg")
    List<DayGiayResponse> findAllResponse();

    @Query("select new luckystore.datn.model.response.DayGiayResponse(dg) from DayGiay dg " +
            "WHERE (:searchText IS NULL OR dg.ten LIKE %:searchText%) AND (:status IS NULL OR dg.trangThai = :status)")
    Page<DayGiayResponse> getPageResponse(String searchText, Integer status, Pageable pageable);

    Boolean existsByTen(String ten);

    Boolean existsByTenAndIdNot(String ten, Long id);
}
