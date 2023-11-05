package luckystore.datn.repository;

import luckystore.datn.entity.HangKhachHang;
import luckystore.datn.entity.PhieuGiamGia;
import luckystore.datn.model.response.PhieuGiamGiaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PhieuGiamGiaRepository extends JpaRepository<PhieuGiamGia, Long> {

    @Query(value = """
            SELECT pgg.MA_GIAM_GIA, pgg.PHAN_TRAM_GIAM, SO_LUONG_PHIEU, NGAY_BAT_DAU, NGAY_KET_THUC
            	, GIA_TRI_DON_TOI_THIEU, GIA_TRI_GIAM_TOI_DA, hkh.TEN_HANG, nv.HO_TEN, pgg.NGAY_TAO, pgg.TRANG_THAI 
            		FROM PhieuGiamGia pgg 
            LEFT JOIN NhanVien nv ON pgg.NGUOI_TAO = nv.ID 
            LEFT JOIN HangKhachHang hkh ON pgg.DOI_TUONG_AP_DUNG = hkh.ID ORDER BY pgg.NGAY_TAO DESC
            """, nativeQuery = true)
    List<PhieuGiamGiaResponse> getAll();
    @Query(value = """
            SELECT pgg.MA_GIAM_GIA, pgg.PHAN_TRAM_GIAM, SO_LUONG_PHIEU, NGAY_BAT_DAU, NGAY_KET_THUC
            	, GIA_TRI_DON_TOI_THIEU, GIA_TRI_GIAM_TOI_DA, hkh.TEN_HANG, nv.HO_TEN, pgg.NGAY_TAO, pgg.TRANG_THAI 
            		FROM PhieuGiamGia pgg 
            LEFT JOIN NhanVien nv ON pgg.NGUOI_TAO = nv.ID 
            LEFT JOIN HangKhachHang hkh ON pgg.DOI_TUONG_AP_DUNG = hkh.ID ORDER BY pgg.NGAY_TAO DESC
            """, nativeQuery = true)
    Page<PhieuGiamGiaResponse> getPagePhieuGiamGia(Pageable pageable);

    @Query(value = """
            SELECT pgg.MA_GIAM_GIA, pgg.PHAN_TRAM_GIAM, SO_LUONG_PHIEU, NGAY_BAT_DAU, NGAY_KET_THUC
            	, GIA_TRI_DON_TOI_THIEU, GIA_TRI_GIAM_TOI_DA, hkh.TEN_HANG, nv.HO_TEN, pgg.NGAY_TAO, pgg.TRANG_THAI
            		FROM PhieuGiamGia pgg
            LEFT JOIN NhanVien nv ON pgg.NGUOI_TAO = nv.ID
            LEFT JOIN HangKhachHang hkh ON pgg.DOI_TUONG_AP_DUNG = hkh.ID WHERE pgg.ID = :id
            """, nativeQuery = true)
    PhieuGiamGiaResponse getPhieuGiamGiaById(@Param("id") Long id);

    boolean existsPhieuGiamGiaByMaGiamGia(String ma);

    boolean existsPhieuGiamGiaByDoiTuongApDung(HangKhachHang hangKhachHang);
}
