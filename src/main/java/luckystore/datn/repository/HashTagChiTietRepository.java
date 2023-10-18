package luckystore.datn.repository;

import luckystore.datn.entity.HashTagChiTiet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashTagChiTietRepository extends JpaRepository<HashTagChiTiet, Long> {
}
