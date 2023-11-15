package luckystore.datn.repository;

import luckystore.datn.entity.Giay;
import luckystore.datn.model.request.GiaySearch;
import luckystore.datn.model.response.GiayResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface GiayRepository extends JpaRepository<Giay, Long> {

    @Query("select new luckystore.datn.model.response.GiayResponse(g) from Giay g  where g.trangThai = 1")
    Page<GiayResponse> findAllByTrangThai(Pageable pageable);

    @Query("select new luckystore.datn.model.response.GiayResponse(g) from Giay g  where g.id = :id")
    GiayResponse findResponseById(Long id);

    @Query("select new luckystore.datn.model.response.GiayResponse(g) from Giay g  where g.trangThai = 1")
    List<GiayResponse> findAllByTrangThai(Integer trangThai);

    @Query("select distinct new luckystore.datn.model.response.GiayResponse(g) from Giay g " +
            "left join g.lstBienTheGiay bienThe " +
            "where (:#{#giaySearch.ten} is null or g.ten like %:#{#giaySearch.ten}%) " +
            "and (:#{#giaySearch.giaTu} is null or bienThe.giaBan >= :#{#giaySearch.giaTu}) " +
            "and (:#{#giaySearch.giaDen} is null or bienThe.giaBan <= :#{#giaySearch.giaDen}) " +
            "and (:#{#giaySearch.tenThuongHieu} is null or g.thuongHieu.ten like %:#{#giaySearch.tenThuongHieu}%) " +
            "and (:#{#giaySearch.thuongHieuIds} is null or g.thuongHieu.id in :#{#giaySearch.thuongHieuIds}) " +
            "and (:#{#giaySearch.trangThai} is null or g.trangThai = :#{#giaySearch.trangThai}) " +
            "order by g.id desc"
    )
    Page<GiayResponse> findPageForList(GiaySearch giaySearch, Pageable pageable);

//    @Query("select new luckystore.datn.model.response.GiayResponse(g.id, g.ten, b, anh.link) " +
//            "from Giay g left join g.lstBienTheGiay b left join g.lstAnh anh " +
//            "where g.id in :lstId and anh.uuTien = 1"
//    )
//    List<GiayResponse> findListByInList(List<Long> lstId);


//    @Query("select distinct new luckystore.datn.model.response.GiayResponse(g.id) from Giay g " +
//            "left join g.lstBienTheGiay bienThe " +
//            "where (:#{#giaySearch.ten} is null or g.ten like %:#{#giaySearch.ten}%) " +
//            "and (:#{#giaySearch.giaTu} is null or bienThe.giaBan >= :#{#giaySearch.giaTu}) " +
//            "and (:#{#giaySearch.giaDen} is null or bienThe.giaBan <= :#{#giaySearch.giaDen}) " +
//            "and (:#{#giaySearch.tenThuongHieu} is null or g.thuongHieu.ten like %:#{#giaySearch.tenThuongHieu}%) " +
//            "and (:#{#giaySearch.thuongHieuIds} is null or g.thuongHieu.id in :#{#giaySearch.thuongHieuIds}) " +
//            "and (:#{#giaySearch.trangThai} is null or g.trangThai = :#{#giaySearch.trangThai}) "
//    )
//    Page<GiayResponse> findGiayBySearchForList(GiaySearch giaySearch, Pageable pageable);


    @Query("select distinct new luckystore.datn.model.response.GiayResponse(g.id, g.ten, anh.link) from Giay g " +
            "inner join g.lstAnh anh " +
            "inner join g.lstBienTheGiay bienThe " +
            "where anh.uuTien = 1 " +
            "and (:#{#giaySearch.ten} is null or g.ten like %:#{#giaySearch.ten}%) " +
            "and (:#{#giaySearch.giaTu} is null or bienThe.giaBan >= :#{#giaySearch.giaTu}) " +
            "and (:#{#giaySearch.giaDen} is null or bienThe.giaBan <= :#{#giaySearch.giaDen}) " +
            "and (:#{#giaySearch.tenThuongHieu} is null or g.thuongHieu.ten like %:#{#giaySearch.tenThuongHieu}%) "
    )
    Page<GiayResponse> findPageForSearch(GiaySearch giaySearch, Pageable pageable);//Hàm này sử dụng cho người dùng


//    @Query("select new luckystore.datn.model.response.GiayResponse(g.id, g.ten, anh.link, bienThe) from Giay g " +
//            "left join g.lstAnh anh " +
//            "left join g.lstBienTheGiay bienThe " +
//            "where (:#{#giaySearch.ten} is null or g.ten like %:#{#giaySearch.ten}%) " +
//            "and (:#{#giaySearch.giaTu} is null or bienThe.giaBan >= :#{#giaySearch.giaTu}) " +
//            "and (:#{#giaySearch.giaDen} is null or bienThe.giaBan <= :#{#giaySearch.giaDen}) " +
//            "and (:#{#giaySearch.tenThuongHieu} is null or g.thuongHieu.ten like %:#{#giaySearch.tenThuongHieu}%) " +
//            "and (:#{#giaySearch.thuongHieuIds} is null or g.thuongHieu.id in :#{#giaySearch.thuongHieuIds}) " +
//            "and (:#{#giaySearch.trangThai} is null or g.trangThai = :#{#giaySearch.trangThai}) " +
//            "order by g.id desc"
//    )
//    Page<GiayResponse> findPageForList(GiaySearch giaySearch, Pageable pageable);

    @Query("select new luckystore.datn.model.response.GiayResponse(g.id, g.ten, anh.link, bienThe) " +
            "from Giay g " +
            "left join g.lstAnh anh " +
            "inner join g.lstBienTheGiay bienThe " +
            "where g.id in :ids and bienThe.trangThai = 1 and (anh.link = null or anh.uuTien = 1)")
    List<GiayResponse> findAllContains(List<Long> ids);

    Boolean existsByTen(String ten);

    Boolean existsByTenAndIdNot(String ten, Long id);

    @Query("select new luckystore.datn.model.response.GiayResponse(g.id, g.ten, anh.link) from Giay g inner join g.lstAnh anh where g.trangThai = 1 and anh.uuTien = 1")
    List<GiayResponse> findAllGiay();


    @Query("select distinct new luckystore.datn.model.response.GiayResponse(g.id, g.ten, anh.link, min(bienThe.giaBan), max(bienThe.giaBan)) from Giay g " +
            "left join g.lstAnh anh " +
            "inner join g.lstBienTheGiay bienThe " +
            "where (anh.link = null or anh.uuTien = 1) and g.trangThai = 1 " +
            "and (:#{#giaySearch.ten} is null or g.ten like %:#{#giaySearch.ten}%) " +
            "and (:#{#giaySearch.giaTu} is null or bienThe.giaBan >= :#{#giaySearch.giaTu}) " +
            "and (:#{#giaySearch.giaDen} is null or bienThe.giaBan <= :#{#giaySearch.giaDen}) " +
            "and (:#{#giaySearch.thuongHieuIds} is null or g.thuongHieu.id in :#{#giaySearch.thuongHieuIds}) " +
            "group by g.id, g.ten, anh.link"
    )
    List<GiayResponse> findAllBySearch(GiaySearch giaySearch);

    @Query("select new luckystore.datn.model.response.GiayResponse(g.id, g.ten) from Giay g where g.ten in :names")
    List<GiayResponse> getIdsByName(Set<String> names);

}
