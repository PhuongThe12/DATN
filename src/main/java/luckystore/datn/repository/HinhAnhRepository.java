package luckystore.datn.repository;

import luckystore.datn.entity.HinhAnh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HinhAnhRepository extends JpaRepository<HinhAnh, Long> {

    @Query("select a.link from HinhAnh a where a.giay.id = :idGiay and a.uuTien = 1")
    String findThubmailByIdGiay(Long idGiay);

}
