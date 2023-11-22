package luckystore.datn.repository;

import luckystore.datn.entity.Provinces;
import luckystore.datn.model.response.ProvinceResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProvinceRepository extends JpaRepository<Provinces,String> {
    @Query("select new luckystore.datn.model.response.ProvinceResponse(p) from Provinces p WHERE p.ten LIKE :prefix%")
    List<ProvinceResponse> findAllProvinceByTen(String prefix);

    @Query("select new luckystore.datn.model.response.ProvinceResponse(p) from Provinces p")
    List<ProvinceResponse> getAll();
}
