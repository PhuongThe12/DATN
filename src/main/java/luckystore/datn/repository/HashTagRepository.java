package luckystore.datn.repository;

import luckystore.datn.entity.HashTag;
import luckystore.datn.model.response.HashTagResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    @Query("select new luckystore.datn.model.response.HashTagResponse(ht) from HashTag ht")
    List<HashTagResponse> findAllResponse();

    @Query("select new luckystore.datn.model.response.HashTagResponse(ht) from HashTag ht " +
            "WHERE (:searchText IS NULL OR ht.ten LIKE %:searchText%) AND (:status IS NULL OR ht.trangThai = :status)")
    Page<HashTagResponse> getPageResponse(String searchText, Integer status, Pageable pageable);

    Boolean existsByTen(String ten);

    Boolean existsByTenAndIdNot(String ten, Long id);
}
