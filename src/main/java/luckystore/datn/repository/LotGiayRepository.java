package luckystore.datn.repository;

import luckystore.datn.entity.LotGiay;
import luckystore.datn.model.response.LotGiayResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface LotGiayRepository extends JpaRepository<LotGiay, Long> {
    @Query("select new luckystore.datn.model.response.LotGiayResponse(lg) from LotGiay lg")
    List<LotGiayResponse> findAllResponse();

    @Query("select new luckystore.datn.model.response.LotGiayResponse(lg) from LotGiay lg " +
            "WHERE (:searchText IS NULL OR lg.ten LIKE %:searchText%) AND (:status IS NULL OR lg.trangThai = :status)")
    Page<LotGiayResponse> getPageResponse(String searchText, Integer status, Pageable pageable);


    @Query("select new luckystore.datn.model.response.LotGiayResponse(lg) from LotGiay lg " +
            "where lg.trangThai = 1 order by lg.id desc ")
    List<LotGiayResponse> findAllActive();


    Boolean existsByTen(String ten);

    Boolean existsByTenAndIdNot(String ten, Long id);

    @Query("select new luckystore.datn.model.response.LotGiayResponse(g.id, g.ten) from LotGiay g where g.ten in :names")
    List<LotGiayResponse> getIdsByName(Set<String> names);

    Optional<LotGiay> findByTen(String ten);

    @Query("select lg.ten from LotGiay lg")
    String[] getAllTen();
}
