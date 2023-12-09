package luckystore.datn.repository;

import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.model.response.BienTheGiayResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @Query("select new luckystore.datn.model.response.BienTheGiayResponse(bt.id, bt.soLuong) from BienTheGiay bt where bt.barCode = :barCode")
    Optional<BienTheGiayResponse> getBienTheGiayByBarCode(String barCode);

    @Query("select new luckystore.datn.model.response.BienTheGiayResponse(bt.id, bt.kichThuoc.ten, bt.mauSac.ten, bt.soLuong, bt.giaBan,bt.hinhAnh,bt.trangThai, bt.giay) " +
            "  from BienTheGiay bt where bt.id IN  :ids")
    List<BienTheGiayResponse> findAllByIdIn(@Param("ids") List<Long> ids);

    @Query("select distinct new luckystore.datn.model.response.BienTheGiayResponse(kmct.bienTheGiay.id, kmct.bienTheGiay.giaBan, kmct.phanTramGiam, kmct.bienTheGiay.trangThai)  " +
            "from KhuyenMaiChiTiet kmct " +
            "inner join kmct.khuyenMai km " +
            "where kmct.bienTheGiay.id in :ids " +
            "and km.trangThai = 1 " +
            "and km.ngayKetThuc > current_date " +
            "and km.ngayBatDau < current_date ")
    List<BienTheGiayResponse> bienTheGiay(List<Long> ids);

    @Query("select bt from BienTheGiay bt where bt.id in :ids")
    List<BienTheGiay> getAllByIds(List<Long> ids);
}


