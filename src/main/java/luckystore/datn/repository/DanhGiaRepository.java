package luckystore.datn.repository;

import luckystore.datn.entity.DanhGia;
import luckystore.datn.model.response.DanhGiaResponse;
import luckystore.datn.model.response.KhuyenMaiResponse;
import luckystore.datn.model.response.MauSacResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DanhGiaRepository extends JpaRepository<DanhGia, Long> {

    @Query("select new luckystore.datn.model.response.DanhGiaResponse(dg) from DanhGia dg " +
            "WHERE (:star IS NULL OR dg.saoDanhGia = :star) order by dg.id desc")
    Page<DanhGiaResponse> getPageResponse(Integer star, Pageable pageable);

    @Query("select new luckystore.datn.model.response.DanhGiaResponse(dg) from DanhGia dg" +
            " WHERE dg.khachHang.id = :idKhachHang and dg.giay.id = :idGiay")
    DanhGiaResponse findByIdKhAndIdGiay(Long idKhachHang, Long idGiay);

    @Query("select new luckystore.datn.model.response.DanhGiaResponse(dg.id,dg.saoDanhGia,dg.binhLuan,dg.trangThai,dg.thoiGian,dg.ngayTao,giay.id,giay.ten,anh.link,dg.khachHang) " +
            "from DanhGia dg inner join dg.giay giay inner join dg.khachHang khachHang left join giay.lstAnh anh" +
            " WHERE dg.khachHang.id = :idKhachHang ")
    List<DanhGiaResponse> findByIdKhachHang(Long idKhachHang);


    boolean existsByKhachHangIdAndGiayId(Long idKhachHang, Long idGiay);
}
