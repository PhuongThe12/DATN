package luckystore.datn.repository;

import luckystore.datn.entity.LotGiay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LotGiayRepository extends JpaRepository<LotGiay, Long> {
}
