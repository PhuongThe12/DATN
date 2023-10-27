package luckystore.datn.repository;

import luckystore.datn.entity.BienTheGiay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BienTheGiayRepository extends JpaRepository<BienTheGiay, Long> {

    Boolean existsByHinhAnh(String link);

}
