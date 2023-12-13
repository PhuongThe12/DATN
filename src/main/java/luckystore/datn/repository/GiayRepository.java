package luckystore.datn.repository;

import luckystore.datn.entity.Giay;
import luckystore.datn.model.request.GiaySearch;
import luckystore.datn.model.request.KhuyenMaiSearch;
import luckystore.datn.model.response.GiayResponse;
import luckystore.datn.model.response.GiayResponseI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface GiayRepository extends JpaRepository<Giay, Long> {

    @Query("select new luckystore.datn.model.response.GiayResponse(g) from Giay g  where g.trangThai = 1")
    Page<GiayResponse> findAllByTrangThai(Pageable pageable);

    @Query("select new luckystore.datn.model.response.GiayResponse(g) from Giay g where g.id = :id")
    GiayResponse findResponseById(Long id);

//    @Query("select new luckystore.datn.model.response.GiayResponse(g.id, g.ten, chatLieu, coGiay, dayGiay, deGiay) " +
//            "from Giay g " +
//            "inner join g.chatLieu chatLieu " +
//            "inner join g.coGiay coGiay " +
//            "inner join g.dayGiay dayGiay " +
//            "inner join g.deGiay deGiay " +
//            "left join g.lstBienTheGiay bienThe " +
//            "left join bienThe.khuyenMaiChiTiet kmct " +
//            "left join kmct.khuyenMai km " +
//            "where g.id = :id " +
//            "and (km is null or km.trangThai = 1) " +
//            "and (km is null or km.ngayBatDau < current_date) " +
//            "and (km is null or km.ngayKetThuc > current_date)")
//    List<GiayResponse> findGiayResponseFullById(Long id);

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
            "or (bienThe.barCode = :#{#giaySearch.ten})" +
            "order by g.id desc"
    )
    Page<GiayResponse> findPageForList(GiaySearch giaySearch, Pageable pageable);

    @Query("select distinct new luckystore.datn.model.response.GiayResponse(g.id, g.ten, anh.link) from Giay g " +
            "left join g.lstAnh anh " +
            "left join g.lstBienTheGiay bienThe " +
            "where (anh.uuTien = 1 or anh.id = null) " +
            "and (:#{#giaySearch.ten} is null or g.ten like %:#{#giaySearch.ten}%) " +
            "and (:#{#giaySearch.giaTu} is null or bienThe.giaBan >= :#{#giaySearch.giaTu}) " +
            "and (:#{#giaySearch.giaDen} is null or bienThe.giaBan <= :#{#giaySearch.giaDen}) " +
            "and (:#{#giaySearch.tenThuongHieu} is null or g.thuongHieu.ten like %:#{#giaySearch.tenThuongHieu}%) " +
            "or (bienThe.barCode = :#{#giaySearch.ten})" +
            "order by g.id desc"
    )
    Page<GiayResponse> findPageForSearch(GiaySearch giaySearch, Pageable pageable);


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
            "where g.id in :ids and bienThe.trangThai = 1 and (anh.link = null or anh.uuTien = 1) " +
            "order by g.id desc")
    List<GiayResponse> findAllContains(List<Long> ids);

    Boolean existsByTen(String ten);

    Boolean existsByTenAndIdNot(String ten, Long id);

    @Query("select new luckystore.datn.model.response.GiayResponse(g.id, g.ten, anh.link) from Giay g inner join g.lstAnh anh where g.trangThai = 1 and anh.uuTien = 1")
    List<GiayResponse> findAllGiay();


    @Query("select distinct new luckystore.datn.model.response.GiayResponse(g.id, g.ten, anh.link, min(bienThe.giaBan), max(bienThe.giaBan)) from Giay g " +
            "left join g.lstAnh anh " +
            "left join g.hashTagChiTiets hashTag " +
            "inner join g.lstBienTheGiay bienThe " +
            "where (anh is null or anh.uuTien = 1) and g.trangThai = 1 " +
            "and (:#{#giaySearch.ten} is null or g.ten like %:#{#giaySearch.ten}%) " +
            "and (:#{#giaySearch.giaTu} is null or bienThe.giaBan >= :#{#giaySearch.giaTu}) " +
            "and (:#{#giaySearch.giaDen} is null or bienThe.giaBan <= :#{#giaySearch.giaDen}) " +
            "and (:#{#giaySearch.thuongHieuIds} is null or g.thuongHieu.id in :#{#giaySearch.thuongHieuIds}) " +
            "and (:#{#giaySearch.kichThuocIds} is null or bienThe.kichThuoc.id in :#{#giaySearch.kichThuocIds}) " +
            "and (:#{#giaySearch.mauSacIds} is null or bienThe.mauSac.id in :#{#giaySearch.mauSacIds}) " +
            "and (:#{#giaySearch.hashTagIds} is null or hashTag.id in :#{#giaySearch.hashTagIds}) " +
            "group by g.id, g.ten, anh.link"
    )
    Page<GiayResponse> findAllBySearch(GiaySearch giaySearch, Pageable pageable);

    @Query("select distinct new luckystore.datn.model.response.GiayResponse(g.id, g.ten, anh.link, min(bienThe.giaBan), max(bienThe.giaBan)) " +
            "from SanPhamYeuThich spyt " +
            "inner join spyt.giay g " +
            "left join g.lstAnh anh " +
            "left join g.hashTagChiTiets hashTag " +
            "inner join g.lstBienTheGiay bienThe " +
            "where spyt.khachHang.id = :#{#giaySearch.idKhachHang} " +
            "and (anh is null or anh.uuTien = 1) and g.trangThai = 1 " +
            "and (:#{#giaySearch.ten} is null or g.ten like %:#{#giaySearch.ten}%) " +
            "and (:#{#giaySearch.giaTu} is null or bienThe.giaBan >= :#{#giaySearch.giaTu}) " +
            "and (:#{#giaySearch.giaDen} is null or bienThe.giaBan <= :#{#giaySearch.giaDen}) " +
            "and (:#{#giaySearch.thuongHieuIds} is null or g.thuongHieu.id in :#{#giaySearch.thuongHieuIds}) " +
            "and (:#{#giaySearch.kichThuocIds} is null or bienThe.kichThuoc.id in :#{#giaySearch.kichThuocIds}) " +
            "and (:#{#giaySearch.mauSacIds} is null or bienThe.mauSac.id in :#{#giaySearch.mauSacIds}) " +
            "and (:#{#giaySearch.hashTagIds} is null or hashTag.id in :#{#giaySearch.hashTagIds}) " +
            "group by g.id, g.ten, anh.link"
    )
    Page<GiayResponse> findAllByKhachHang(GiaySearch giaySearch, Pageable pageable);

    @Query("select distinct new luckystore.datn.model.response.GiayResponse(g.id, g.ten, anh.link, min(bienThe.giaBan), max(bienThe.giaBan)) from Giay g " +
            "left join g.lstAnh anh " +
            "inner join g.lstBienTheGiay bienThe " +
            "where g.id in :ids " +
            "group by g.id, g.ten, anh.link"
    )
    Set<GiayResponse> findAllBySearchIds(Set<Long> ids);

    @Query("select new luckystore.datn.model.response.GiayResponse(g.id, g.ten) from Giay g where g.ten in :names")
    List<GiayResponse> getIdsByName(Set<String> names);

    Optional<Giay> findByTen(String ten);

    @Query("select new luckystore.datn.model.response.GiayResponse(g.id, g.ten, g.thuongHieu.ten, anh.link, bienThe.id, bienThe.mauSac.ten, bienThe.kichThuoc.ten, bienThe.giaBan) " +
            " from Giay g left join g.lstBienTheGiay bienThe " +
            " left join g.lstAnh anh " +
            " where (anh is null or anh.uuTien = 1)" +
            " and g.id not in (" +
            " select bt.giay.id from KhuyenMai km " +
            " inner join km.khuyenMaiChiTiets kmct " +
            " inner join kmct.bienTheGiay bt " +
            " where (:#{#kmSearch.ngayBatDau} between km.ngayBatDau and km.ngayKetThuc " +
            " or :#{#kmSearch.ngayKetThuc} between km.ngayBatDau and km.ngayKetThuc " +
            " or (:#{#kmSearch.ngayBatDau} <= km.ngayBatDau and :#{#kmSearch.ngayKetThuc} >= km.ngayKetThuc)))")
    List<GiayResponse> getAllGiayWithoutDiscount(KhuyenMaiSearch kmSearch);

    @Query("select new luckystore.datn.model.response.GiayResponse(g.id, g.ten, g.thuongHieu.ten, anh.link, bienThe.id, bienThe.mauSac.ten, bienThe.kichThuoc.ten, bienThe.giaBan) " +
            " from Giay g left join g.lstBienTheGiay bienThe " +
            " left join g.lstAnh anh " +
            " where (anh is null or anh.uuTien = 1)" +
            " and g.id in :idGiays")
    List<GiayResponse> getAllGiayById(Set<Long> idGiays);

    @Query(value = "select  b1_0.ID_GIAY id, sum(h1_0.SO_LUONG) soLuongThongKe " +
            "from HoaDonChiTiet h1_0 " +
            "inner join BienTheGiay b1_0 on b1_0.ID=h1_0.ID_BIEN_THE_GIAY " +
            "inner join Giay g on g.ID = b1_0.ID_GIAY " +
            "inner join HoaDon h2_0 on h2_0.ID=h1_0.ID_HOA_DON " +
            "where h2_0.TRANG_THAI=1 " +
            "and (:#{#giaySearch.ten} is null or g.TEN  like %:#{#giaySearch.ten}%) " +
            "and (:#{#giaySearch.giaTu} is null or b1_0.GIA_BAN >= :#{#giaySearch.giaTu}) " +
            "and (:#{#giaySearch.giaDen} is null or b1_0.GIA_BAN <= :#{#giaySearch.giaDen}) " +
            "group by b1_0.ID_GIAY " +
            "order by sum(h1_0.SO_LUONG) desc", nativeQuery = true)
    Page<GiayResponseI> findTop(Pageable pageable, GiaySearch giaySearch);


    //top x giày bán chạy trong y ngày
    @Query("select new luckystore.datn.model.response.GiayResponse(g, sum(hdc.soLuong)) " +
            "from HoaDonChiTiet hdc " +
            "join hdc.bienTheGiay btg " +
            "join btg.giay g " +
            "join hdc.hoaDon hd " +
            "where hd.trangThai = 1 and hd.ngayTao >= :targetDateTime " +
            "group by g " +
            "order by sum(hdc.soLuong) desc")
    Page<GiayResponse> findTopSellingShoesInLastDays(LocalDateTime targetDateTime, Pageable pageable);

    //giày bán chạy nhất
    @Query("select new luckystore.datn.model.response.GiayResponse(g, sum(hdc.soLuong)) " +
            "from HoaDonChiTiet hdc " +
            "join hdc.bienTheGiay btg " +
            "join btg.giay g " +
            "join hdc.hoaDon hd " +
            "where hd.trangThai = 1 " +
            "group by g " +
            "order by sum(hdc.soLuong) desc")
    Page<GiayResponse> findTopSellingShoes(Pageable pageable);

    //top x giày xuất hiện trong sản phẩm yêu thích
    @Query("select new luckystore.datn.model.response.GiayResponse(g, count(spyt)) " +
            "from SanPhamYeuThich spyt " +
            "join spyt.giay g " +
            "group by g " +
            "order by count(spyt) desc")
    Page<GiayResponse> findTopFavoritedShoes(Pageable pageable);

}
