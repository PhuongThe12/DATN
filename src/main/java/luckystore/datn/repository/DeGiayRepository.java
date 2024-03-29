package luckystore.datn.repository;

import luckystore.datn.entity.DeGiay;
import luckystore.datn.model.response.DeGiayResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    @Query("select new luckystore.datn.model.response.DeGiayResponse(g.id, g.ten) from DeGiay g where g.ten in :names")
    List<DeGiayResponse> getIdsByName(Set<String> names);

    Optional<DeGiay> findByTen(String ten);

    @Query("select lg.ten from DeGiay lg")
    String[] getAllTen();
}

