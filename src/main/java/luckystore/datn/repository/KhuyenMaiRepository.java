package luckystore.datn.repository;

import luckystore.datn.entity.KhuyenMai;
import luckystore.datn.model.request.KhuyenMaiSearch;
import luckystore.datn.model.response.ChiTietKhuyenMaiResponse;
import luckystore.datn.model.response.KhuyenMaiChiTietResponse;
import luckystore.datn.model.response.KhuyenMaiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhuyenMaiRepository extends JpaRepository<KhuyenMai, Long> {

    @Query("select new luckystore.datn.model.response.KhuyenMaiResponse(km) from KhuyenMai km")
    List<KhuyenMaiResponse> findAllResponse();

    @Query("select new luckystore.datn.model.response.KhuyenMaiResponse(km) from KhuyenMai km " +
            "WHERE (:searchText IS NULL OR km.ten LIKE %:searchText%) AND (:status IS NULL OR km.trangThai = :status)")
    Page<KhuyenMaiResponse> getPageResponse(String searchText, Integer status, Pageable pageable);

    List<KhuyenMai> findAllByTrangThai(int i);

    @Query("select  distinct kmct.bienTheGiay.giay.id from KhuyenMai km" +
            " inner join km.khuyenMaiChiTiets kmct " +
            " where kmct.bienTheGiay.id in :#{#kmSearch.bienTheIds} " +
            " and (:#{#kmSearch.ngayBatDau} between km.ngayBatDau and km.ngayKetThuc " +
            " or :#{#kmSearch.ngayKetThuc} between km.ngayBatDau and km.ngayKetThuc " +
            " or (:#{#kmSearch.ngayBatDau} <= km.ngayBatDau and :#{#kmSearch.ngayKetThuc} >= km.ngayKetThuc))")
    List<Long> getDaTonTaiKhuyenMai(KhuyenMaiSearch kmSearch);

    @Query("select distinct kmct.bienTheGiay.giay.id from KhuyenMai km" +
            " inner join km.khuyenMaiChiTiets kmct " +
            " where km.id != :#{#kmSearch.id} and kmct.bienTheGiay.id in :#{#kmSearch.bienTheIds} " +
            " and (:#{#kmSearch.ngayBatDau} between km.ngayBatDau and km.ngayKetThuc " +
            " or :#{#kmSearch.ngayKetThuc} between km.ngayBatDau and km.ngayKetThuc " +
            " or (:#{#kmSearch.ngayBatDau} <= km.ngayBatDau and :#{#kmSearch.ngayKetThuc} >= km.ngayKetThuc))")
    List<Long> getDaTonTaiKhuyenMaiAndIdNot(KhuyenMaiSearch kmSearch);

    @Query("select new luckystore.datn.model.response.ChiTietKhuyenMaiResponse(km.id, km.ten, km.ngayBatDau, km.ngayKetThuc, km.trangThai, km.ghiChu) from KhuyenMai km where km.id = :id")
    ChiTietKhuyenMaiResponse getKhuyenMaiById(Long id);

    @Query("select new luckystore.datn.model.response.KhuyenMaiResponse(km) from KhuyenMai km " +
            "where (:#{#kmSearch.ten} is null or km.ten like %:#{#kmSearch.ten}%) " +
            "and (:#{#kmSearch.ngayBatDau} is null or km.ngayBatDau >= :#{#kmSearch.ngayBatDau}) " +
            "and (:#{#kmSearch.ngayKetThuc} is null or km.ngayKetThuc <= :#{#kmSearch.ngayKetThuc}) " +
            "and km.trangThai = 1 order by km.ngayBatDau desc")
    Page<KhuyenMaiResponse> getSearchingKhuyenMai(KhuyenMaiSearch kmSearch, Pageable pageable);

    @Query("select new luckystore.datn.model.response.KhuyenMaiResponse(km) from KhuyenMai km " +
            "where (:#{#kmSearch.ten} is null or km.ten like %:#{#kmSearch.ten}%) " +
            "and (:#{#kmSearch.ngayBatDau} is null or km.ngayBatDau >= :#{#kmSearch.ngayBatDau}) " +
            "and (:#{#kmSearch.ngayKetThuc} is null or km.ngayKetThuc <= :#{#kmSearch.ngayKetThuc}) " +
            "and km.trangThai = 0 order by km.ngayBatDau desc")
    Page<KhuyenMaiResponse> getSearchingKhuyenMaiDaAn(KhuyenMaiSearch kmSearch, Pageable pageable);

    @Query("select new luckystore.datn.model.response.KhuyenMaiResponse(km) from KhuyenMai km " +
            "where (:#{#kmSearch.ten} is null or km.ten like %:#{#kmSearch.ten}%) " +
            "and (:#{#kmSearch.ngayBatDau} is null or km.ngayBatDau >= :#{#kmSearch.ngayBatDau}) " +
            "and (:#{#kmSearch.ngayKetThuc} is null or km.ngayKetThuc <= :#{#kmSearch.ngayKetThuc}) " +
            " and km.ngayKetThuc < current_date " +
            "and km.trangThai = 1 order by km.ngayBatDau desc")
    Page<KhuyenMaiResponse> getSearchingKhuyenMaiDaKetThuc(KhuyenMaiSearch kmSearch, Pageable pageable);

    @Query("select new luckystore.datn.model.response.KhuyenMaiResponse(km) from KhuyenMai km " +
            "where (:#{#kmSearch.ten} is null or km.ten like %:#{#kmSearch.ten}%) " +
            "and (:#{#kmSearch.ngayBatDau} is null or km.ngayBatDau >= :#{#kmSearch.ngayBatDau}) " +
            "and (:#{#kmSearch.ngayKetThuc} is null or km.ngayKetThuc <= :#{#kmSearch.ngayKetThuc}) " +
            "and km.ngayBatDau > current_date " +
            "and km.trangThai = 1 order by km.ngayBatDau desc")
    Page<KhuyenMaiResponse> getSearchingKhuyenMaiSapDienRa(KhuyenMaiSearch kmSearch, Pageable pageable);

    @Query("select new luckystore.datn.model.response.KhuyenMaiResponse(km) from KhuyenMai km " +
            "where (:#{#kmSearch.ten} is null or km.ten like %:#{#kmSearch.ten}%) " +
            "and (:#{#kmSearch.ngayBatDau} is null or km.ngayBatDau >= :#{#kmSearch.ngayBatDau}) " +
            "and (:#{#kmSearch.ngayKetThuc} is null or km.ngayKetThuc <= :#{#kmSearch.ngayKetThuc}) " +
            "and km.ngayBatDau <= current_date and km.ngayKetThuc >= current_date " +
            "and km.trangThai = 1 order by km.ngayBatDau desc")
    Page<KhuyenMaiResponse> getSearchingKhuyenMaiDangDienRa(KhuyenMaiSearch kmSearch, Pageable pageable);
//
//    Boolean existsByTen(String ten);
//
//    Boolean existsByTenAndIdNot(String ten, Long id);
}
