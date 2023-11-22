package luckystore.datn.repository;

import luckystore.datn.entity.HoaDonChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet,Long> {

    @Query("SELECT hd.id FROM HoaDonChiTiet hd where hd.id = :id")
    Long getIdById(Long id);

    @Query("SELECT hdct FROM HoaDonChiTiet hdct JOIN FETCH hdct.bienTheGiay WHERE hdct.id = :id")
    HoaDonChiTiet getHoaDonChiTietWithBienTheGiay(Long id);
}
