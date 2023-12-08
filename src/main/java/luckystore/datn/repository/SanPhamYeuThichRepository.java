package luckystore.datn.repository;

import luckystore.datn.entity.SanPhamYeuThich;
import luckystore.datn.model.response.SanPhamYeuThichResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamYeuThichRepository extends JpaRepository<SanPhamYeuThich,Long> {

    @Query("select new luckystore.datn.model.response.SanPhamYeuThichResponse (sp) from SanPhamYeuThich sp" +
            " WHERE (:searchText IS NULL OR sp.giay.ten LIKE %:searchText%) AND sp.khachHang.id=5")
    Page<SanPhamYeuThichResponse> getPageResponse(String searchText, Pageable pageable);
}
