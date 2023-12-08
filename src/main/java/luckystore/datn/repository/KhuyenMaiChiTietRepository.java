package luckystore.datn.repository;

import luckystore.datn.entity.KhuyenMaiChiTiet;
import luckystore.datn.model.response.KhuyenMaiChiTietResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface KhuyenMaiChiTietRepository extends JpaRepository<KhuyenMaiChiTiet, Long> {

    @Query("select new luckystore.datn.model.response.KhuyenMaiChiTietResponse(kmct.id, kmct.bienTheGiay.id, kmct.phanTramGiam) from KhuyenMaiChiTiet kmct " +
            "inner join kmct.khuyenMai km " +
            "where kmct.bienTheGiay.id in :idBienThes " +
            "and km.trangThai = 1 " +
            "and km.ngayKetThuc > current_date " +
            "and km.ngayBatDau < current_date ")
    List<KhuyenMaiChiTietResponse> getAllByIdBienThe(List<Long> idBienThes);

    @Query("select new luckystore.datn.model.response.KhuyenMaiChiTietResponse(kmct.id, kmct.bienTheGiay.id, kmct.phanTramGiam) from KhuyenMaiChiTiet kmct " +
            "inner join kmct.khuyenMai km " +
            "where kmct.bienTheGiay.id = :idBienThe " +
            "and km.trangThai = 1 " +
            "and km.ngayKetThuc > current_date " +
            "and km.ngayBatDau < current_date ")
    KhuyenMaiChiTietResponse getByIdBienThe(Long idBienThe);

    @Query("select new luckystore.datn.model.response.KhuyenMaiChiTietResponse(kmct.id, kmct.bienTheGiay.id, kmct.phanTramGiam, kmct.bienTheGiay.giay.id) from KhuyenMaiChiTiet kmct " +
            "inner join kmct.khuyenMai km " +
            "where km.id = :idKhuyenMai ")
    List<KhuyenMaiChiTietResponse> getAllByIdKhuyenMai(Long idKhuyenMai);



}
