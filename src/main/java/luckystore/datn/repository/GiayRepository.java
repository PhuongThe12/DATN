package luckystore.datn.repository;

import luckystore.datn.entity.Giay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GiayRepository extends JpaRepository<Giay, Long> {
}
