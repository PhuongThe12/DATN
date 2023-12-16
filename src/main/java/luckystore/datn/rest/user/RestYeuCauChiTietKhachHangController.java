package luckystore.datn.rest.user;

import luckystore.datn.model.response.YeuCauChiTietResponse;
import luckystore.datn.repository.HoaDonChiTietRepository;
import luckystore.datn.repository.YeuCauChiTietRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/user/yeu-cau-chi-tiet")
public class RestYeuCauChiTietKhachHangController {

    private final YeuCauChiTietRepository yeuCauChiTietRepository;

    @Autowired
    public RestYeuCauChiTietKhachHangController(HoaDonChiTietRepository hoaDonChiTietRepository, YeuCauChiTietRepository yeuCauChiTietRepository) {
        this.yeuCauChiTietRepository = yeuCauChiTietRepository;
    }


    @GetMapping("/list/{id}")
    public List<YeuCauChiTietResponse> getHoaDonChiTiet(@PathVariable Long id) {
        return yeuCauChiTietRepository.getPageResponse(id);
    }

}
