package luckystore.datn.repository;

import luckystore.datn.entity.LyDo;
import luckystore.datn.model.response.LyDoResponse;
import luckystore.datn.model.response.MauSacResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LyDoRepository extends JpaRepository<LyDo, Long> {

    @Query("select new luckystore.datn.model.response.LyDoResponse(ld) from LyDo ld " +
            "where ld.trangThai = 1 or ld.trangThai = 2 order by ld.id ASC")
    List<LyDoResponse> findAllActive();

    Boolean existsByTen(String ten);

    Boolean existsByTenAndIdNot(String ten, Long id);

    @Query("select new luckystore.datn.model.response.LyDoResponse(ld, count(ycct)) " +
            "from YeuCauChiTiet ycct " +
            "join ycct.lyDo ld " +
            "group by ld " +
            "order by count(ycct) desc")
    Page<LyDoResponse> findTopReasons(Pageable pageable);

    @Query("select new luckystore.datn.model.response.LyDoResponse(ld, count(ycct)) " +
            "from YeuCauChiTiet ycct " +
            "join ycct.lyDo ld " +
            "group by ld " +
            "order by count(ycct) desc")
    Page<LyDoResponse> findReasonsForReturn(Pageable pageable);

    @Query("select count(ycct) from YeuCauChiTiet ycct")
    long getTotalRequestCount();
}
