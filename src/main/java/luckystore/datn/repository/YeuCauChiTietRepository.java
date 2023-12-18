package luckystore.datn.repository;

import luckystore.datn.entity.YeuCauChiTiet;
import luckystore.datn.model.response.YeuCauChiTietResponse;
import luckystore.datn.model.response.YeuCauResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface YeuCauChiTietRepository extends JpaRepository<YeuCauChiTiet,Long> {

    @Query("SELECT new luckystore.datn.model.response.YeuCauChiTietResponse(ycct) FROM YeuCauChiTiet ycct WHERE (ycct.yeuCau.id = :id)")
    List<YeuCauChiTietResponse> getPageResponse(Long id);

    @Query("SELECT COUNT(yct) " +
            "FROM YeuCauChiTiet yct JOIN yct.yeuCau yc " +
            "WHERE yc.ngaySua = :ngaySua " +
            "AND yc.trangThai = 2 AND (yct.trangThai = 0 OR yct.trangThai = 2)")
    Long countRequestDetailsByStatus(Date ngaySua);
}
