package luckystore.datn.repository;

import luckystore.datn.entity.Wards;
import luckystore.datn.model.response.WardResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface WardRepository extends JpaRepository<Wards,String> {
    @Query("select new luckystore.datn.model.response.WardResponse (w) from Wards w  " +
            "WHERE w.districts.id= :districtId")
    List<WardResponse> findWardByProvinceId(String districtId);

    @Query("select new luckystore.datn.model.response.WardResponse(w) from Wards w")
    List<WardResponse> getAll();
}
