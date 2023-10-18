package luckystore.datn.repository;

import luckystore.datn.entity.MuiGiay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MuiGiayRepository extends JpaRepository<MuiGiay, Long> {
}
