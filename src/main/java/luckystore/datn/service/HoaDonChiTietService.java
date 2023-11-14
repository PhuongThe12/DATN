package luckystore.datn.service;

import luckystore.datn.entity.HoaDonChiTiet;
import luckystore.datn.model.response.HoaDonChiTietResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HoaDonChiTietService {

    List<HoaDonChiTietResponse> getDonMua();

    Page<HoaDonChiTietResponse> getPage(int page, String searchText, Integer status);


}
