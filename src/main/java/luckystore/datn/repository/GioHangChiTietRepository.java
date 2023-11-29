package luckystore.datn.repository;

import luckystore.datn.entity.GioHangChiTiet;
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
}
