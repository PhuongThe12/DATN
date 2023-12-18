package luckystore.datn.service.impl;

import luckystore.datn.entity.Giay;
import luckystore.datn.entity.KhachHang;
import luckystore.datn.entity.SanPhamYeuThich;
import luckystore.datn.model.request.SanPhamYeuThichRequest;
import luckystore.datn.model.response.SanPhamYeuThichResponse;
import luckystore.datn.repository.SanPhamYeuThichRepository;
import luckystore.datn.service.SanPhamYeuThichService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SanPhamYeuThichServiceImpl implements SanPhamYeuThichService {

    @Autowired
    private SanPhamYeuThichRepository sanPhamYeuThichRepo;

    @Override
    public Page<SanPhamYeuThichResponse> getPage(int page, String searchText, Long idKhachHang) {
        return sanPhamYeuThichRepo.getPageResponse(searchText, PageRequest.of((page - 1), 5), idKhachHang);
    }

    @Override
    public SanPhamYeuThichResponse addSanPhamYeuThich(SanPhamYeuThichRequest sanPhamYeuThichRequest) {
        SanPhamYeuThich sanPhamYeuThich = new SanPhamYeuThich();

        // Kiểm tra và cập nhật ID của KhachHang
        Long khachHangId = sanPhamYeuThichRequest.getKhachHangId();
        if (khachHangId != null) {
            KhachHang khachHang = new KhachHang();
            khachHang.setId(khachHangId);
            sanPhamYeuThich.setKhachHang(khachHang);
        }

        // Kiểm tra và cập nhật ID của Giay
        Long giayId = sanPhamYeuThichRequest.getGiayId();
        if (giayId != null) {
            Giay giay = new Giay();
            giay.setId(giayId);
            sanPhamYeuThich.setGiay(giay);
        }

        // Lưu bản ghi mới vào cơ sở dữ liệu
        SanPhamYeuThich savedSanPhamYeuThich = sanPhamYeuThichRepo.save(sanPhamYeuThich);

        // Trả về đối tượng SanPhamYeuThichResponse với thông tin đã lưu
        return new SanPhamYeuThichResponse(savedSanPhamYeuThich);
    }

    @Override
    @Transactional
    public void deleteSanPhamYeuThichByIdKhachHangAndIdGiay(Long idKhachHang, Long idGiay) {
        sanPhamYeuThichRepo.deleteByKhachHang_IdAndGiay_Id(idKhachHang, idGiay);
    }

    @Override
    public void deleteSanPhamYeuThick(Long id) {
        sanPhamYeuThichRepo.deleteById(id);
    }

    @Override
    public boolean existsByIdKhachHangAndIdIdGiay(Long idKhachHang, Long idGiay) {
        boolean tonTai = sanPhamYeuThichRepo.existsByIdKhachHangAndIdIdGiay(idKhachHang, idGiay);
        if (tonTai) {
            return true;
        }
        return false;
    }

}
