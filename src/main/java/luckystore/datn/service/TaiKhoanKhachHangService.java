package luckystore.datn.service;

import luckystore.datn.model.request.TaiKhoanRequest;
import luckystore.datn.model.response.TaiKhoanResponse;
import org.springframework.data.domain.Page;
import java.util.List;
public interface TaiKhoanKhachHangService {

    Page<TaiKhoanResponse> getPage(int page, String searchText, Integer status);

    TaiKhoanResponse addTaiKhoan(TaiKhoanRequest taiKhoanRequest);

//    TaiKhoanResponse khachHanglogin(TaiKhoanRequest taiKhoanRequest);

}
