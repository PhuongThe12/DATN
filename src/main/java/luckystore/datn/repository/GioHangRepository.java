package luckystore.datn.repository;

import luckystore.datn.entity.GioHang;
import luckystore.datn.model.response.GioHangResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GioHangRepository extends JpaRepository<GioHang,Long> {
    @Query("select new luckystore.datn.model.response.GioHangResponse(gh) from GioHang gh")
    List<GioHangResponse> findAllResponse();

    @Query("select new luckystore.datn.model.response.GioHangResponse(gh) from GioHang gh where gh.khachHang.id = :id")
    GioHangResponse getGioHangByIdKhachHang(Long id);
}
