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
    
    @Query("select new luckystore.datn.model.response.BienTheGiayResponse(bt.id, bt.kichThuoc.ten, bt.mauSac.ten, bt.soLuong, bt.giaBan, bt.giaNhap) " +
            "  from BienTheGiay bt where bt.giay.id = :idGiay")
    List<BienTheGiayResponse> getSimpleByIdGiay(Long idGiay);

}
