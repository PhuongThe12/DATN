package luckystore.datn.repository;

import luckystore.datn.entity.Kichthuoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KichThuocRepository extends JpaRepository<Kichthuoc, Long> {
}
