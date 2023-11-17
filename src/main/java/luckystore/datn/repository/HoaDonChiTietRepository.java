package luckystore.datn.repository;

import luckystore.datn.entity.HoaDonChiTiet;
import luckystore.datn.model.response.HoaDonChiTietResponse;
import luckystore.datn.model.response.HoaDonResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet,Long> {

    @Query("select new luckystore.datn.model.response.HoaDonChiTietResponse(hdct) from HoaDonChiTiet hdct")
    List<HoaDonChiTietResponse> findAllResponse();

    @Query("select new luckystore.datn.model.response.HoaDonChiTietResponse(hdct) from HoaDonChiTiet hdct where hdct.hoaDon.id = :id")
    List<HoaDonChiTietResponse> findAllResponse(Long id);
}
