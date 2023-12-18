package luckystore.datn.repository;

import luckystore.datn.entity.PhieuGiamGia;
import luckystore.datn.model.request.FindPhieuGiamGiaRequest;
import luckystore.datn.model.response.PhieuGiamGiaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PhieuGiamGiaRepository extends JpaRepository<PhieuGiamGia, Long> {

    @Query(value = """
        SELECT pgg.ID, pgg.MA_GIAM_GIA, pgg.PHAN_TRAM_GIAM, pgg.SO_LUONG_PHIEU,
        	   pgg.NGAY_BAT_DAU, pgg.NGAY_KET_THUC, pgg.GIA_TRI_DON_TOI_THIEU,
        	   pgg.GIA_TRI_GIAM_TOI_DA, hkh.TEN_HANG, pgg.TRANG_THAI, pgg.NGUOI_TAO,
        	   pgg.NGAY_TAO FROM PhieuGiamGia pgg\s
        			LEFT JOIN HangKhachHang hkh ON pgg.DOI_TUONG_AP_DUNG = hkh.ID
    """, nativeQuery = true)
    List<PhieuGiamGiaResponse> getAll();

    @Query(value = """
        SELECT pgg.ID, pgg.MA_GIAM_GIA, pgg.PHAN_TRAM_GIAM, pgg.SO_LUONG_PHIEU,
        	   pgg.NGAY_BAT_DAU, pgg.NGAY_KET_THUC, pgg.GIA_TRI_DON_TOI_THIEU,
        	   pgg.GIA_TRI_GIAM_TOI_DA, hkh.TEN_HANG, pgg.TRANG_THAI, nv.EMAIL,
        	   pgg.NGAY_TAO FROM PhieuGiamGia pgg\s
        			LEFT JOIN HangKhachHang hkh ON pgg.DOI_TUONG_AP_DUNG = hkh.ID
        			LEFT JOIN NhanVien nv ON nv.ID = pgg.NGUOI_TAO
        	   WHERE (:searchText IS NULL OR pgg.MA_GIAM_GIA LIKE %:searchText%) AND (:status IS NULL OR pgg.TRANG_THAI = :status)
    """, nativeQuery = true)
    Page<PhieuGiamGiaResponse> getPage(Pageable pageable, String searchText, Integer status);


    @Query(value = """
                SELECT pgg.ID, pgg.MA_GIAM_GIA, pgg.PHAN_TRAM_GIAM, pgg.SO_LUONG_PHIEU,
                	   pgg.NGAY_BAT_DAU, pgg.NGAY_KET_THUC, pgg.GIA_TRI_DON_TOI_THIEU,
                	   pgg.GIA_TRI_GIAM_TOI_DA, hkh.TEN_HANG, pgg.TRANG_THAI, nv.EMAIL,
                	   pgg.NGAY_TAO FROM PhieuGiamGia pgg\s
                			LEFT JOIN HangKhachHang hkh ON pgg.DOI_TUONG_AP_DUNG = hkh.ID
                			LEFT JOIN NhanVien nv ON nv.ID = pgg.NGUOI_TAO 
                        WHERE 
                			(:#{#req.maGiamGia} IS NULL 
                            OR :#{#req.maGiamGia} LIKE '' 
                            OR pgg.MA_GIAM_GIA LIKE %:#{#req.maGiamGia}%)
                        AND
                            (:#{#req.hangKhachHang} IS NULL 
                            OR :#{#req.hangKhachHang} LIKE '' 
                            OR hkh.TEN_HANG LIKE %:#{#req.hangKhachHang}%)
                        AND 
                             (
                                (:#{#req.ngayBatDau} IS NULL 
                                or :#{#req.ngayBatDau} LIKE ''
                                or :#{#req.ngayKetThuc} IS NULL 
                                or :#{#req.ngayKetThuc} LIKE '')           
                                or((pgg.NGAY_BAT_DAU>= :#{#req.ngayBatDau} 
                                AND pgg.NGAY_BAT_DAU<= :#{#req.ngayKetThuc}) 
                                AND(pgg.NGAY_KET_THUC>= :#{#req.ngayBatDau} 
                                AND pgg.NGAY_KET_THUC<= :#{#req.ngayKetThuc} )) 
                             )
                        AND (:#{#req.trangThai} IS NULL OR pgg.TRANG_THAI = :#{#req.trangThai})
            ORDER BY pgg.NGAY_TAO DESC        
            """, nativeQuery = true)
    Page<PhieuGiamGiaResponse> getListSearchPhieu(Pageable pageable,@Param("req")FindPhieuGiamGiaRequest req);

    @Query(value = """
                SELECT pgg.ID, pgg.MA_GIAM_GIA, pgg.PHAN_TRAM_GIAM, pgg.SO_LUONG_PHIEU,
                	   pgg.NGAY_BAT_DAU, pgg.NGAY_KET_THUC, pgg.GIA_TRI_DON_TOI_THIEU,
                	   pgg.GIA_TRI_GIAM_TOI_DA, hkh.TEN_HANG, pgg.TRANG_THAI, nv.EMAIL,
                	   pgg.NGAY_TAO FROM PhieuGiamGia pgg\s
                			LEFT JOIN HangKhachHang hkh ON pgg.DOI_TUONG_AP_DUNG = hkh.ID
                			LEFT JOIN NhanVien nv ON nv.ID = pgg.NGUOI_TAO 
                        WHERE  q
                            (:#{#hangKhachHang} IS NULL 
                            OR :#{#hangKhachHang} LIKE '' 
                            OR hkh.TEN_HANG = :#{#hangKhachHang}) AND pgg.TRANG_THAI = 0
            ORDER BY pgg.NGAY_TAO DESC        
            """, nativeQuery = true)
    List<PhieuGiamGiaResponse> getListPhieuByHangKhachHang(@Param("hangKhachHang")String hangKhachHang);

    @Query(value = """
                SELECT pgg.ID, pgg.MA_GIAM_GIA, pgg.PHAN_TRAM_GIAM, pgg.SO_LUONG_PHIEU,
                	   pgg.NGAY_BAT_DAU, pgg.NGAY_KET_THUC, pgg.GIA_TRI_DON_TOI_THIEU,
                	   pgg.GIA_TRI_GIAM_TOI_DA, hkh.TEN_HANG, pgg.TRANG_THAI,
                	   pgg.NGAY_TAO FROM PhieuGiamGia pgg\s
                			LEFT JOIN HangKhachHang hkh ON pgg.DOI_TUONG_AP_DUNG = hkh.ID
                			LEFT JOIN NhanVien nv ON nv.ID = pgg.NGUOI_TAO 
                        WHERE pgg.ID = :id
            """, nativeQuery = true)
    Optional<PhieuGiamGiaResponse> getPhieuResponse(Long id);
}
