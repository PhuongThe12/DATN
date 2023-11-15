package luckystore.datn.repository;

import luckystore.datn.entity.ChatLieu;
import luckystore.datn.model.response.ChatLieuResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ChatLieuRepository extends JpaRepository<ChatLieu, Long> {
    @Query("select new luckystore.datn.model.response.ChatLieuResponse(cl) from ChatLieu cl")
    List<ChatLieuResponse> findAllResponse();

    @Query("select new luckystore.datn.model.response.ChatLieuResponse(cl) from ChatLieu cl " +
            "WHERE (:searchText IS NULL OR cl.ten LIKE %:searchText%) AND (:status IS NULL OR cl.trangThai = :status)")
    Page<ChatLieuResponse> getPageResponse(String searchText, Integer status, Pageable pageable);

    @Query("select new luckystore.datn.model.response.ChatLieuResponse(cl) from ChatLieu cl " +
            "where cl.trangThai = 1 order by cl.id desc")
    List<ChatLieuResponse> findAllActive();

    Boolean existsByTen(String ten);

    Boolean existsByTenAndIdNot(String ten, Long id);

    @Query("select new luckystore.datn.model.response.ChatLieuResponse(g.id, g.ten) from ChatLieu g where g.ten in :names")
    List<ChatLieuResponse> getIdsByName(Set<String> names);
}
