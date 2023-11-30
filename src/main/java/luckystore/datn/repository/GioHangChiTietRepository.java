package luckystore.datn.repository;

import luckystore.datn.entity.GioHangChiTiet;
import luckystore.datn.model.response.GioHangChiTietResponse;
import luckystore.datn.model.response.GioHangResponse;
import luckystore.datn.service.GioHangService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GioHangChiTietRepository extends JpaRepository<GioHangChiTiet, Long> {

    @Modifying
    @Query(value = "UPDATE GioHangChiTiet ghct SET ghct.soLuong = :soLuong where ghct.id = :id")
    void updateSoLuongGioHangChiTiet(Integer soLuong, Long id);

    @Query(value = "select new luckystore.datn.model.response.GioHangChiTietResponse(g) from GioHangChiTiet g where g.gioHang.id = :idGioHang and g.bienTheGiay.id = :idBienTheGiay")
    GioHangChiTietResponse findByGioHangAndBienTheGiay(Long idGioHang , Long idBienTheGiay);
}
