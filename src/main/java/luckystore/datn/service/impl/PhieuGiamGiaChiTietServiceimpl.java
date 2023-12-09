package luckystore.datn.service.impl;

import lombok.RequiredArgsConstructor;
import luckystore.datn.entity.PhieuGiamGiaChiTiet;
import luckystore.datn.repository.PhieuGiamGiaChiTietRepository;
import luckystore.datn.service.PhieuGiamGiaChiTietService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhieuGiamGiaChiTietServiceimpl implements PhieuGiamGiaChiTietService {

    private final PhieuGiamGiaChiTietRepository phieuGiamGiaChiTietRepository;

    @Override
    public PhieuGiamGiaChiTiet save(PhieuGiamGiaChiTiet phieuGiamGiaChiTiet) {
        return phieuGiamGiaChiTietRepository.save(phieuGiamGiaChiTiet);
    }
}
