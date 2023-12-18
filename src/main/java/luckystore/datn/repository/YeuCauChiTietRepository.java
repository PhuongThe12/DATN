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

    @Query(value = """
               SELECT COUNT(yct.ID) FROM YeuCauChiTiet yct LEFT JOIN YeuCau yc on yc.ID = yct.ID_YEU_CAU
                 WHERE CONVERT(DATE, yc.NGAY_TAO) = :ngaySua AND yc.TRANG_THAI = 2 AND (yct.TRANG_THAI = 0 OR yct.TRANG_THAI = 2)
            """, nativeQuery = true)
    Long countRequestDetailsByStatus(Date ngaySua);
}
