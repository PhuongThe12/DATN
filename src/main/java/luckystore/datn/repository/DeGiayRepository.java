package luckystore.datn.repository;

import luckystore.datn.entity.DeGiay;
import luckystore.datn.model.response.DeGiayResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeGiayRepository extends JpaRepository<DeGiay, Long> {
    @Query("select new luckystore.datn.model.response.DeGiayResponse(dg) from DeGiay dg")
    List<DeGiayResponse> findAllResponse();

    @Query("select new luckystore.datn.model.response.DeGiayResponse(dg) from DeGiay dg " +
            "WHERE (:searchText IS NULL OR dg.ten LIKE %:searchText%) AND (:status IS NULL OR dg.trangThai = :status)")
    Page<DeGiayResponse> getPageResponse(String searchText, Integer status, Pageable pageable);


    @Query("select new luckystore.datn.model.response.DeGiayResponse(dg) from DeGiay dg " +
            "where dg.trangThai = 1 order by dg.id desc ")
    List<DeGiayResponse> findAllActive();


    Boolean existsByTen(String ten);

    Boolean existsByTenAndIdNot(String ten, Long id);
}
