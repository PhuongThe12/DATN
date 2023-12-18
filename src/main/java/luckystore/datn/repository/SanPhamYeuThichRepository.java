package luckystore.datn.repository;

import luckystore.datn.entity.SanPhamYeuThich;
import luckystore.datn.model.response.SanPhamYeuThichResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamYeuThichRepository extends JpaRepository<SanPhamYeuThich, Long> {

    @Query("select new luckystore.datn.model.response.SanPhamYeuThichResponse (sp) from SanPhamYeuThich sp" +
            " WHERE (:searchText IS NULL OR sp.giay.ten LIKE %:searchText%) AND sp.khachHang.id= :idKhachHang")
    Page<SanPhamYeuThichResponse> getPageResponse(String searchText, Pageable pageable, Long idKhachHang);

    @Query("select count(*) > 0 from SanPhamYeuThich spyt where spyt.khachHang.id = :idKhachHang and spyt.giay.id = :idGiay")
    Boolean existsByIdKhachHangAndIdIdGiay(Long idKhachHang, Long idGiay);

    void deleteByKhachHang_IdAndGiay_Id(Long idKhachHang, Long idGiay);
}
