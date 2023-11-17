package luckystore.datn.repository;

import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.model.response.BienTheGiayResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BienTheGiayRepository extends JpaRepository<BienTheGiay, Long> {

    Boolean existsByHinhAnh(String link);
    
    @Query("select new luckystore.datn.model.response.BienTheGiayResponse(bt.id, bt.kichThuoc.ten, bt.mauSac.ten, bt.soLuong, bt.giaBan) " +
            "  from BienTheGiay bt where bt.giay.id = :idGiay order by bt.mauSac.ten, bt.kichThuoc.ten")
    List<BienTheGiayResponse> getSimpleByIdGiay(Long idGiay);

    @Query("select new luckystore.datn.model.response.BienTheGiayResponse(bt.id, bt.mauSac.id, bt.kichThuoc.id, bt.barCode) from BienTheGiay bt where bt.barCode in :lstBarCode")
    List<BienTheGiayResponse> getBienTheGiayByListBarCode(List<String> lstBarCode);

    @Query("select count(bt.barCode) > 0 " +
            "from BienTheGiay bt where bt.barCode = :barCode and bt.id != :id")
    Boolean getBienTheGiayByBarCodeUpdate(String barCode, Long id);

    @Query("select bt.soLuong from BienTheGiay bt where bt.id = :id")
    Integer getSoLuong(Long id);

    @Query("select bt from BienTheGiay bt where bt.id in :id")
    List<BienTheGiay> findByIdContains(List<Long> id);
}
