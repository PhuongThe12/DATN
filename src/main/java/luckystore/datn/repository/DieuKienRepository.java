package luckystore.datn.repository;

import luckystore.datn.entity.DieuKien;
import luckystore.datn.entity.DotGiamGia;
import luckystore.datn.model.response.DieuKienResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DieuKienRepository extends JpaRepository<DieuKien, Long> {

    @Query("select new luckystore.datn.model.response.DieuKienResponse(dk) from DieuKien dk")
    List<DieuKienResponse> findAllResponse();

    void deleteAllByDotGiamGia(DotGiamGia dotGiamGia);
}
