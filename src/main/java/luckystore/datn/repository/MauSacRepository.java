package luckystore.datn.repository;

import luckystore.datn.entity.MauSac;
import luckystore.datn.model.response.MauSacResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MauSacRepository extends JpaRepository<MauSac, Long> {

    @Query("select new  luckystore.datn.model.response.MauSacResponse(ms) from MauSac ms")
    List<MauSacResponse> findAllResponse();

    Boolean existsByTen(String ten);

    Boolean existsByTenAndIdNot(String ten, Long id);

}
