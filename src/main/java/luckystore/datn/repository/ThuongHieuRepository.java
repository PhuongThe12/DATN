package luckystore.datn.repository;

import luckystore.datn.entity.ThuongHieu;
import luckystore.datn.model.response.ThuongHieuResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ThuongHieuRepository extends JpaRepository<ThuongHieu, Long> {

    @Query("select new luckystore.datn.model.response.ThuongHieuResponse(th) from ThuongHieu th")
    List<ThuongHieuResponse> findAllResponse();

    @Query("select new luckystore.datn.model.response.ThuongHieuResponse(th) from ThuongHieu th " +
            "WHERE (:searchText IS NULL OR th.ten LIKE %:searchText%) AND (:status IS NULL OR th.trangThai = :status)")
    Page<ThuongHieuResponse> getPageResponse(String searchText, Integer status, Pageable pageable);


    @Query("select new luckystore.datn.model.response.ThuongHieuResponse(th) from ThuongHieu th " +
            "where th.trangThai = 1 order by th.id desc ")
    List<ThuongHieuResponse> findAllActive();


    Boolean existsByTen(String ten);

    Boolean existsByTenAndIdNot(String ten, Long id);

    @Query("select new luckystore.datn.model.response.ThuongHieuResponse(g.id, g.ten) from ThuongHieu g where g.ten in :names")
    List<ThuongHieuResponse> getIdsByName(Set<String> names);

    Optional<ThuongHieu> findByTen(String ten);

    @Query("select lg.ten from ThuongHieu lg")
    String[] getAllTen();
}
