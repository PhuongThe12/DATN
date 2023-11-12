package luckystore.datn.repository;

import luckystore.datn.entity.KhuyenMai;
import luckystore.datn.model.response.KhuyenMaiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhuyenMaiRepository extends JpaRepository<KhuyenMai, Long> {

    @Query("select new luckystore.datn.model.response.KhuyenMaiResponse(km) from KhuyenMai km")
    List<KhuyenMaiResponse> findAllResponse();

    @Query("select new luckystore.datn.model.response.KhuyenMaiResponse(km) from KhuyenMai km " +
            "WHERE (:searchText IS NULL OR km.ten LIKE %:searchText%) AND (:status IS NULL OR km.trangThai = :status)")
    Page<KhuyenMaiResponse> getPageResponse(String searchText, Integer status, Pageable pageable);
//
//    Boolean existsByTen(String ten);
//
//    Boolean existsByTenAndIdNot(String ten, Long id);
}
