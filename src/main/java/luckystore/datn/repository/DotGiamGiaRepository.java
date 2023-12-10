package luckystore.datn.repository;

import luckystore.datn.entity.DotGiamGia;
import luckystore.datn.model.request.KhuyenMaiSearch;
import luckystore.datn.model.response.DotGiamGiaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DotGiamGiaRepository extends JpaRepository<DotGiamGia, Long> {

    @Query("select new luckystore.datn.model.response.DotGiamGiaResponse(dgg) from DotGiamGia dgg")
    List<DotGiamGiaResponse> findAllResponse();

    @Query("select new luckystore.datn.model.response.DotGiamGiaResponse(dgg) from DotGiamGia dgg " +
            "WHERE (:searchText IS NULL OR dgg.ten LIKE %:searchText%) AND (:status IS NULL OR dgg.trangThai = :status)")
    Page<DotGiamGiaResponse> getPageResponse(String searchText, Integer status, Pageable pageable);

    Boolean existsByTen(String ten);

    Boolean existsByTenAndIdNot(String ten, Long id);

    @Query("select new luckystore.datn.model.response.DotGiamGiaResponse(dgg) " +
            "from DotGiamGia dgg where dgg.ngayBatDau < current_date " +
            "and dgg.ngayKetThuc > current_date and dgg.trangThai = 1")
    List<DotGiamGiaResponse> getAllActive();

    @Query("select new luckystore.datn.model.response.DotGiamGiaResponse(dgg) from DotGiamGia dgg " +
            "where (:#{#kmSearch.ten} is null or dgg.ten like %:#{#kmSearch.ten}%) " +
            "and (:#{#kmSearch.ngayBatDau} is null or dgg.ngayBatDau >= :#{#kmSearch.ngayBatDau}) " +
            "and (:#{#kmSearch.ngayKetThuc} is null or dgg.ngayKetThuc <= :#{#kmSearch.ngayKetThuc}) " +
            "and dgg.trangThai = 0 order by dgg.ngayBatDau desc")
    Page<DotGiamGiaResponse> getSearchingDotGiamGiaDaAn(KhuyenMaiSearch kmSearch, Pageable pageable);

    @Query("select new luckystore.datn.model.response.DotGiamGiaResponse(dgg) from DotGiamGia dgg " +
            "where (:#{#kmSearch.ten} is null or dgg.ten like %:#{#kmSearch.ten}%) " +
            "and (:#{#kmSearch.ngayBatDau} is null or dgg.ngayBatDau >= :#{#kmSearch.ngayBatDau}) " +
            "and (:#{#kmSearch.ngayKetThuc} is null or dgg.ngayKetThuc <= :#{#kmSearch.ngayKetThuc}) " +
            "and dgg.ngayBatDau <= current_date and dgg.ngayKetThuc >= current_date " +
            "and dgg.trangThai = 1 order by dgg.ngayBatDau desc")
    Page<DotGiamGiaResponse> getSearchingDotGiamGiaDangDienRa(KhuyenMaiSearch kmSearch, Pageable pageable);

    @Query("select new luckystore.datn.model.response.DotGiamGiaResponse(dgg) from DotGiamGia dgg " +
            "where (:#{#kmSearch.ten} is null or dgg.ten like %:#{#kmSearch.ten}%) " +
            "and (:#{#kmSearch.ngayBatDau} is null or dgg.ngayBatDau >= :#{#kmSearch.ngayBatDau}) " +
            "and (:#{#kmSearch.ngayKetThuc} is null or dgg.ngayKetThuc <= :#{#kmSearch.ngayKetThuc}) " +
            "and dgg.ngayBatDau > current_date " +
            "and dgg.trangThai = 1 order by dgg.ngayBatDau desc")
    Page<DotGiamGiaResponse> getSearchingDotGiamGiaSapDienRa(KhuyenMaiSearch kmSearch, Pageable pageable);

    @Query("select new luckystore.datn.model.response.DotGiamGiaResponse(dgg) from DotGiamGia dgg " +
            "where (:#{#kmSearch.ten} is null or dgg.ten like %:#{#kmSearch.ten}%) " +
            "and (:#{#kmSearch.ngayBatDau} is null or dgg.ngayBatDau >= :#{#kmSearch.ngayBatDau}) " +
            "and (:#{#kmSearch.ngayKetThuc} is null or dgg.ngayKetThuc <= :#{#kmSearch.ngayKetThuc}) " +
            " and dgg.ngayKetThuc < current_date " +
            "and dgg.trangThai = 1 order by dgg.ngayBatDau desc")
    Page<DotGiamGiaResponse> getSearchingDotGiamGiaDaKetThuc(KhuyenMaiSearch kmSearch, Pageable pageable);
}

