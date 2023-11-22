package luckystore.datn.repository;

import luckystore.datn.entity.HoaDonChiTiet;
import luckystore.datn.model.response.BienTheGiayResponse;
import luckystore.datn.model.response.DonMuaResponse;
import luckystore.datn.model.response.HoaDonChiTietResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet,Long> {
<<<<<<< HEAD
    @Query("select new luckystore.datn.model.response.HoaDonChiTietResponse(hdct) from HoaDonChiTiet hdct")
    List<HoaDonChiTietResponse> findAllResponse();


    @Query(value = "SELECT HoaDon.ID as[ID_HOA_DON], BienTheGiay.HINH_ANH, Giay.TEN as [TEN_GIAY], " +
        "ThuongHieu.TEN as[TEN_THUONG_HIEU], HoaDonChiTiet.ID_BIEN_THE_GIAY, HoaDon.ID_KHACH_HANG, " +
        "HoaDon.PHI_SHIP, HoaDonChiTiet.SO_LUONG, HoaDonChiTiet.DON_GIA, HoaDonChiTiet.TRANG_THAI, " +
        "SUM(HoaDonChiTiet.SO_LUONG * HoaDonChiTiet.DON_GIA) AS [TONG_TIEN]\n" +
        "FROM Giay\n" +
        "JOIN BienTheGiay ON Giay.ID = BienTheGiay.ID_GIAY\n" +
        "JOIN ThuongHieu ON Giay.ID_THUONG_HIEU = ThuongHieu.ID\n" +
        "JOIN HoaDonChiTiet ON BienTheGiay.ID = HoaDonChiTiet.ID_BIEN_THE_GIAY\n" +
        "JOIN HoaDon ON HoaDonChiTiet.ID_HOA_DON = HoaDon.ID\n" +
        "WHERE HoaDon.ID_KHACH_HANG = 1 " +
        "AND (:status IS NULL OR HoaDonChiTiet.TRANG_THAI = :status)\n" +
        "GROUP BY BienTheGiay.HINH_ANH, Giay.TEN, ThuongHieu.TEN, HoaDonChiTiet.ID_BIEN_THE_GIAY, " +
        "HoaDon.ID_KHACH_HANG, HoaDonChiTiet.SO_LUONG, HoaDonChiTiet.DON_GIA, HoaDonChiTiet.TRANG_THAI, " +
        "HoaDon.PHI_SHIP, HoaDon.ID",
        countQuery = "SELECT COUNT(*) FROM (SELECT DISTINCT HoaDon.ID\n" +
                "FROM Giay\n" +
                "JOIN BienTheGiay ON Giay.ID = BienTheGiay.ID_GIAY\n" +
                "JOIN ThuongHieu ON Giay.ID_THUONG_HIEU = ThuongHieu.ID\n" +
                "JOIN HoaDonChiTiet ON BienTheGiay.ID = HoaDonChiTiet.ID_BIEN_THE_GIAY\n" +
                "JOIN HoaDon ON HoaDonChiTiet.ID_HOA_DON = HoaDon.ID\n" +
                "WHERE HoaDon.ID_KHACH_HANG = 1 " +
                "AND (:status IS NULL OR HoaDonChiTiet.TRANG_THAI = :status)) AS total",
        nativeQuery = true)
    Page<DonMuaResponse> donMua(Integer status, Pageable pageable);
=======

    @Query("SELECT hd.id FROM HoaDonChiTiet hd where hd.id = :id")
    Long getIdById(Long id);
>>>>>>> 2ef72cb438619f0a75df9dfc3d7dfeef7b8e9fec

}
