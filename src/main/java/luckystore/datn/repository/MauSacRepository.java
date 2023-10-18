package luckystore.datn.repository;

import luckystore.datn.entity.MauSac;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MauSacRepository extends JpaRepository<MauSac, Long> {

    Boolean existsByTen(String ten);

    Boolean existsByTenAndIdNot(String ten, Long id);

}
