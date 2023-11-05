package luckystore.datn.repository;

import luckystore.datn.entity.YeuCauChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YeuCauChiTietRepository extends JpaRepository<YeuCauChiTiet,Long> {
}
