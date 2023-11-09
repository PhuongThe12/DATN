package luckystore.datn.repository;

import luckystore.datn.entity.HoaDon;
import luckystore.datn.model.response.HashTagResponse;
import luckystore.datn.model.response.HoaDonResponse;
import luckystore.datn.model.response.MuiGiayResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon,Long> {

    @Query("select new luckystore.datn.model.response.HoaDonResponse(hd) from HoaDon hd")
    List<HoaDonResponse> findAllResponse();

    @Query("select new luckystore.datn.model.response.HoaDonResponse(hd) from HoaDon hd " +
            "WHERE (:searchText IS NULL OR hd.ghiChu LIKE %:searchText%) AND (:status IS NULL OR hd.trangThai = :status)")
    Page<HoaDonResponse> getPageResponse(String searchText, Integer status, Pageable pageable);
}


