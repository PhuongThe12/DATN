package luckystore.datn.repository;

import luckystore.datn.entity.Giay;
import luckystore.datn.model.response.GiayResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GiayRepository extends JpaRepository<Giay, Long> {

    @Query("select new luckystore.datn.model.response.GiayResponse(g) from Giay g  where g.trangThai = 1")
    Page<GiayResponse> findAllByTrangThai(Pageable pageable);

    @Query("select new luckystore.datn.model.response.GiayResponse(g) from Giay g  where g.trangThai = 1")
    List<GiayResponse> findAllByTrangThai(Integer trangThai);

//    @Query("select new luckystore.datn.model.response.GiayResponse(g.id, g.ten) from Giay g  where g.trangThai = 1")
//    List<GiayResponse> findAllActive();

    @Query("select new luckystore.datn.model.response.GiayResponse(g.id, g.ten, anh.link) from Giay g " +
            "inner join g.lstAnh anh " +
            "where anh.uuTien = 1")
    Page<GiayResponse> findAllForList(Pageable pageable);

    @Query("select new luckystore.datn.model.response.GiayResponse(g) from Giay g where g.id in :ids")
    List<GiayResponse> findAllContains(List<Long> ids);

    Boolean existsByTen(String ten);

    Boolean existsByTenAndIdNot(String ten, Long id);

    @Query("select new luckystore.datn.model.response.GiayResponse(g.id, g.ten, anh.link) from Giay g inner join g.lstAnh anh where g.trangThai = 1 and anh.uuTien = 1")
    List<GiayResponse> findAllGiay();
}
