package luckystore.datn.service;

import luckystore.datn.model.request.*;
import luckystore.datn.model.response.BienTheGiayResponse;
import luckystore.datn.model.response.GiayResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GiayService {

    Page<GiayResponse> getAllActive(GiaySearch giaySearch);

    List<GiayResponse> getAllContains(List<Long> ids);

    GiayResponse getResponseById(Long id);

    GiayResponse addGiay(GiayRequest giayRequest);

    Page<GiayResponse> findAllForList(GiaySearch giaySearch);

    Page<GiayResponse> getPage();

    GiayResponse updateSoLuong(GiayRequest giayRequest);

    GiayResponse updateGia(GiayRequest giayRequest);

    GiayResponse updateGiay(Long id, GiayRequest giayRequest);

    Page<GiayResponse> findAllBySearch(GiaySearch giaySearch);

    void addExcel(List<GiayExcelRequest> giayExcelRequests);

    void updateExcel(List<GiayExcelRequest> lst);

    Integer getSoLuong(Long id);

    BienTheGiayResponse getBienTheByBarcode(String barCode);

    List<BienTheGiayResponse> getBienTheGiayByListId(List<Long> ids);

    Page<GiayResponse> findSimpleBySearch(GiaySearch giaySearch);

    List<GiayResponse> getAllGiayWithoutDiscount(KhuyenMaiSearch kmSearch);

    Page<GiayResponse> findTopSellingShoes(ThongKeRequest thongKeRequest);
    Page<GiayResponse> findTopSellingShoesInLastDays(ThongKeRequest thongKeRequest);
    Page<BienTheGiayResponse> findTopSellingShoeVariantInLastDays(ThongKeRequest thongKeRequest);
    Page<GiayResponse> findTopFavoritedShoes(ThongKeRequest thongKeRequest);
    Page<BienTheGiayResponse> findTopCartVariants(ThongKeRequest thongKeRequest);
    Page<BienTheGiayResponse> findVariantReturnRates(ThongKeRequest thongKeRequest);


}
