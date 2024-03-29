package luckystore.datn.service.impl;

import jakarta.transaction.Transactional;
import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.entity.HoaDonChiTiet;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.model.response.DonMuaResponse;
import luckystore.datn.model.response.HoaDonChiTietResponse;
import luckystore.datn.model.response.HoaDonResponse;
import luckystore.datn.repository.BienTheGiayRepository;
import luckystore.datn.repository.HoaDonChiTietRepository;
import luckystore.datn.service.HoaDonChiTietService;
import luckystore.datn.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoaDonChiTietServiceImpl implements HoaDonChiTietService {
    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepo;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    private BienTheGiayRepository bienTheGiayRepository;

    @Override
    public Page<DonMuaResponse> getAll(int page, Integer status) {
        return hoaDonChiTietRepo.donMua(status, PageRequest.of((page - 1), 5));
    }

    @Override
    public List<HoaDonChiTietResponse> getAll() {
        return hoaDonChiTietRepository.findAllResponse();
    }

    @Override
    public List<HoaDonChiTietResponse> getAllByIdHoaDon(Long id) {
        return hoaDonChiTietRepository.findAllResponse(id);
    }

    @Override
    public Page<HoaDonChiTietResponse> getPage(int page, String searchText, Integer status) {
        return null;
    }

    @Override
    public HoaDonChiTietResponse findById(Long id) {
        return new HoaDonChiTietResponse(hoaDonChiTietRepo.findById(id).get());
    }

    @Override
    @Transactional
    public void deleteHoaDonChiTiet(Long idHdct) {
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(idHdct).orElseThrow(() ->
                new NotFoundException("Không tìm thấy"));

        BienTheGiay bienTheGiay = bienTheGiayRepository.findById(hoaDonChiTiet.getBienTheGiay().getId()).orElseThrow(
                () -> new NotFoundException("Không tìm thấy"));

        bienTheGiay.setSoLuong(bienTheGiay.getSoLuong() + hoaDonChiTiet.getSoLuong());

        bienTheGiayRepository.save(bienTheGiay);
        hoaDonChiTietRepository.deleteById(idHdct);
    }

    @Override
    public HoaDonChiTiet getHoaDonChiTiet(Long id) {
        return hoaDonChiTietRepository.getHoaDonChiTietWithBienTheGiay(id);
    }

}
