package luckystore.datn.repository;

import luckystore.datn.entity.Districts;
import luckystore.datn.model.response.DistrictResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DistrictRepository extends JpaRepository<Districts,String> {
    @Query("select new luckystore.datn.model.response.DistrictResponse (d) from Districts d  " +
            "WHERE d.provinces.id= :provinceId")
    List<DistrictResponse> findDistrictsByProvinceId(String provinceId);

    @Query("select new luckystore.datn.model.response.DistrictResponse (d) from Districts d JOIN Provinces p ON d.provinces.id = p.id ")
    List<DistrictResponse> getAll();

}
