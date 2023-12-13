package luckystore.datn.repository;

import com.jayway.jsonpath.internal.function.numeric.Sum;
import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.model.response.BienTheGiayResponse;
import luckystore.datn.model.response.GiayResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    @Query("select distinct new luckystore.datn.model.response.BienTheGiayResponse(bt.id, bt.kichThuoc.ten, bt.mauSac.ten, bt.soLuong, bt.giaBan,bt.hinhAnh,bt.trangThai, bt.giay, km.phanTramGiam) " +
            "  from BienTheGiay bt left join bt.khuyenMaiChiTietList km  where bt.id IN  :ids" )
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

    //top x biến thể giày bán chạy trong y ngày
    @Query("select new luckystore.datn.model.response.BienTheGiayResponse(btg, g, ms, kt, sum(hdc.soLuong)) " +
            "from HoaDonChiTiet hdc " +
            "join hdc.bienTheGiay btg " +
            "join btg.giay g " +
            "join btg.mauSac ms " +
            "join btg.kichThuoc kt " +
            "join hdc.hoaDon hd " +
            "where hd.trangThai = 1 and hd.ngayTao >= :targetDateTime " +
            "group by btg, g, ms, kt " +
            "order by sum(hdc.soLuong) desc")
    Page<BienTheGiayResponse> findTopSellingShoeVariantInLastDays(LocalDateTime targetDateTime, Pageable pageable);

    //Top x Biến Thể Giày Xuất Hiện Trong Giỏ Hàng
    @Query("select new luckystore.datn.model.response.BienTheGiayResponse(btg, g, ms, kt, sum(ghct.soLuong)) " +
            "from GioHangChiTiet ghct " +
            "join ghct.bienTheGiay btg " +
            "join btg.giay g " +
            "join btg.mauSac ms " +
            "join btg.kichThuoc kt " +
            "group by btg, g, ms, kt " +
            "order by sum(ghct.soLuong) desc")
    Page<BienTheGiayResponse> findTopCartVariants(Pageable pageable);

    //Top x Biến Thể Giày có tỷ lệ đổi trả cao
    @Query("select new luckystore.datn.model.response.BienTheGiayResponse(" +
            "btg, g,ms,kt, sum(hdct.soLuong), sum(hdct.soLuongTra),(sum(hdct.soLuongTra) * 1.0 / case when sum(hdct.soLuong) > 0 then sum(hdct.soLuong) else 1 end)*100) " +
            "from HoaDonChiTiet hdct " +
            "join hdct.bienTheGiay btg " +
            "join btg.giay g " +
            "join btg.mauSac ms " +
            "join btg.kichThuoc kt " +
            "join hdct.hoaDon hd " +
            "where hd.trangThai = 1 " +
            "group by btg, g, ms, kt " +
            "order by (sum(hdct.soLuongTra) * 1.0 / case when sum(hdct.soLuong) > 0 then sum(hdct.soLuong) else 1 end)*100 desc")
    Page<BienTheGiayResponse> findVariantHighReturnRates(Pageable pageable);




}


