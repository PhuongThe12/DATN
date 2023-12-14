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


    boolean existsByKhachHangIdAndGiayId(Long idKhachHang, Long idGiay);
}
