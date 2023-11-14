package luckystore.datn.repository;

import luckystore.datn.entity.HoaDonChiTiet;
import luckystore.datn.entity.KhachHang;
import luckystore.datn.entity.TaiKhoan;
import luckystore.datn.model.response.DiaChiNhanHangResponse;
import luckystore.datn.model.response.HoaDonChiTietResponse;
import luckystore.datn.model.response.HoaDonResponse;
import luckystore.datn.model.response.KhachHangResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet, Long> {

    @Query("select new luckystore.datn.model.response.HoaDonChiTietResponse(hdct) from HoaDonChiTiet hdct")
    List<HoaDonChiTietResponse> findAllResponse();

    @Query("select new luckystore.datn.model.response.HoaDonChiTietResponse(hdct) from HoaDonChiTiet hdct " +
            "WHERE (:searchText IS NULL OR hdct.bienTheGiay.giay.ten LIKE %:searchText%) AND (:status IS NULL OR hdct.trangThai = :status)")
    Page<HoaDonChiTietResponse> getPageResponse(String searchText, Integer status, Pageable pageable);


//    @Query(value = "SELECT btg.HINH_ANH,g.TEN,th.TEN,kh.id,hdct.SO_LUONG,hdct.DON_GIA FROM HoaDonChiTiet hdct \n" +
//            "JOIN HoaDon hd ON hdct.ID_HOA_DON = hd.ID\n" +
//            "JOIN KhachHang kh ON hd.ID_KHACH_HANG = kh.id\n" +
//            "JOIN BienTheGiay btg ON hdct.ID_BIEN_THE_GIAY = btg.ID\n" +
//            "JOIN Giay g ON btg.ID_GIAY = g.ID\n" +
//            "JOIN ThuongHieu th ON g.ID_THUONG_HIEU =th.ID\n" +
//            "WHERE hd.ID_KHACH_HANG =1", nativeQuery = true)
//    List<HoaDonChiTiet> getDonMua();

}
