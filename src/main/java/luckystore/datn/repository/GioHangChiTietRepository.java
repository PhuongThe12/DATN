package luckystore.datn.repository;

import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.entity.GioHangChiTiet;
import luckystore.datn.model.response.BienTheGiayResponse;
import luckystore.datn.model.response.GioHangChiTietResponse;
import luckystore.datn.model.response.GioHangResponse;
import luckystore.datn.service.GioHangService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface GioHangChiTietRepository extends JpaRepository<GioHangChiTiet, Long> {

    @Modifying
    @Query(value = "UPDATE GioHangChiTiet ghct SET ghct.soLuong = :soLuong where ghct.id = :id")
    void updateSoLuongGioHangChiTiet(Integer soLuong, Long id);

    @Modifying
    @Query(value = "Delete GioHangChiTiet ghct where ghct.gioHang.id = :idGioHang")
    void deleteAllGioHangChiTietByIdGioHang(Long idGioHang);

    @Query(value = "select new luckystore.datn.model.response.GioHangChiTietResponse(g) from GioHangChiTiet g where g.gioHang.id = :idGioHang and g.bienTheGiay.id = :idBienTheGiay")
    GioHangChiTietResponse findByGioHangAndBienTheGiay(Long idGioHang , Long idBienTheGiay);

    @Query(value = "select new luckystore.datn.model.response.GioHangChiTietResponse(g) from GioHangChiTiet g where g.gioHang.id = :idGioHang")
    List<GioHangChiTietResponse> findGioHangChiTietByIdGioHang(Long idGioHang);

    @Query("select ghct.soLuong from GioHangChiTiet ghct where ghct.bienTheGiay.id = :id and ghct.gioHang.id = :idGioHang")
    Integer getSoLuong(Long id, Long idGioHang);

    @Query("select new luckystore.datn.model.response.BienTheGiayResponse(ghct.bienTheGiay.id,ghct.bienTheGiay.soLuong,ghct.bienTheGiay.giaBan) from GioHangChiTiet ghct where ghct.gioHang.id = :idGioHang")
    List<BienTheGiayResponse> getBienTheGiayByIdGioHang(Long idGioHang);

//    @Query("delete from GioHangChiTiet ghct where ghct.gioHang.id = :idGiohang and ghct.bienTheGiay.id = :idBientheGiay")
//    void deleteGioHangChiTietByIdGioHangAndIdBienThe(Long idGioHang, Long idBienTheGiay);

}
