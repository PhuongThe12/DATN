package luckystore.datn.repository;

import luckystore.datn.entity.SanPhamYeuThich;
import luckystore.datn.model.response.SanPhamYeuThichResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SanPhamYeuThichRepository extends JpaRepository<SanPhamYeuThich,Long> {

    @Query("select new luckystore.datn.model.response.SanPhamYeuThichResponse (sp) from SanPhamYeuThich sp WHERE sp.khachHang.id=5")
    List<SanPhamYeuThichResponse> findAllResponse();
}
