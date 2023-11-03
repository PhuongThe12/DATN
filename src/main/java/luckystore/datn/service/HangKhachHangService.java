package luckystore.datn.service;

import luckystore.datn.model.request.HangKhachHangRequest;
import luckystore.datn.model.response.HangKhachHangResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface HangKhachHangService {
    List<HangKhachHangResponse> getAll();

    Page<HangKhachHangResponse> getPage(int page, String searchText, Integer status);

    HangKhachHangResponse addHangKhachHang(HangKhachHangRequest hangKhachHangRequest);

    HangKhachHangResponse updateHangKhachHang(Long id, HangKhachHangRequest hangKhachHangRequest);

    HangKhachHangResponse findById(Long id);
}
