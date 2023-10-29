package luckystore.datn.repository;



import luckystore.datn.entity.YeuCau;
import luckystore.datn.model.response.MauSacResponse;
import luckystore.datn.model.response.YeuCauResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface YeuCauRepository extends JpaRepository<YeuCau,Long> {
    @Query("select new luckystore.datn.model.response.YeuCauResponse(yc) from YeuCau yc")
    List<YeuCauResponse> finAllResponse();

    @Query("SELECT new luckystore.datn.model.response.YeuCauResponse(yc) FROM YeuCau yc " +
            "WHERE (:ngayBatDau is NULL or yc.ngayTao >= :ngayBatDau) " +
            "AND (:ngayKetThuc is NULL or yc.ngayTao <= :ngayKetThuc)" +
            "AND (:loaiYeuCau is NULL or yc.loaiYeuCau = :loaiYeuCau) " +
            "AND (:trangThai is NULL or yc.trangThai = :trangThai) " +
            "AND (:searchText IS NULL OR yc.id = :searchText) " +
            "ORDER BY yc.ngayTao DESC" )
    Page<YeuCauResponse> getPageResponse(Date ngayBatDau,String searchText,Date ngayKetThuc, Integer loaiYeuCau, Integer trangThai,Pageable pageable);
}
