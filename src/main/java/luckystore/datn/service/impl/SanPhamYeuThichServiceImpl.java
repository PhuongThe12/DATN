package luckystore.datn.service.impl;

import luckystore.datn.entity.SanPhamYeuThich;
import luckystore.datn.model.request.SanPhamYeuThichRequest;
import luckystore.datn.model.response.SanPhamYeuThichResponse;
import luckystore.datn.repository.SanPhamYeuThichRepository;
import luckystore.datn.service.SanPhamYeuThichService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SanPhamYeuThichServiceImpl implements SanPhamYeuThichService {

    @Autowired
    private SanPhamYeuThichRepository sanPhamYeuThichRepo;

    @Override
    public List<SanPhamYeuThichResponse> getAll() {
        return sanPhamYeuThichRepo.findAllResponse();
    }

    @Override
    public SanPhamYeuThichResponse addSanPhamYeuThich(SanPhamYeuThichRequest sanPhamYeuThichRequest) {
        SanPhamYeuThich sanPhamYeuThich = getSanPhamYeuThich(new SanPhamYeuThich(), sanPhamYeuThichRequest);
        return new SanPhamYeuThichResponse(sanPhamYeuThichRepo.save(sanPhamYeuThich));
    }

    @Override
    public void deleteSanPhamYeuThick(Long id) {
        sanPhamYeuThichRepo.deleteById(id);
    }

    private SanPhamYeuThich getSanPhamYeuThich(SanPhamYeuThich sanPhamYeuThich, SanPhamYeuThichRequest sanPhamYeuThichRequest) {
        sanPhamYeuThich.setId(sanPhamYeuThichRequest.getId());
        sanPhamYeuThich.setGiay(sanPhamYeuThichRequest.getGiay());
        sanPhamYeuThich.setKhachHang(sanPhamYeuThichRequest.getKhachHang());
        return sanPhamYeuThich;
    }
}
