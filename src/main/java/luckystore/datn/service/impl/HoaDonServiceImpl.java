package luckystore.datn.service.impl;

import jakarta.transaction.Transactional;
import luckystore.datn.constraints.ErrorMessage;
import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.entity.HoaDon;
import luckystore.datn.entity.HoaDonChiTiet;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.model.request.AddOrderProcuctRequest;
import luckystore.datn.model.request.HoaDonSearch;
import luckystore.datn.model.response.HoaDonBanHangResponse;
import luckystore.datn.model.response.HoaDonChiTietResponse;
import luckystore.datn.model.response.HoaDonResponse;
import luckystore.datn.model.response.HoaDonYeuCauRespone;
import luckystore.datn.repository.BienTheGiayRepository;
import luckystore.datn.repository.HoaDonRepository;
import luckystore.datn.service.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class HoaDonServiceImpl implements HoaDonService {

    @Autowired
    HoaDonRepository hoaDonRepository;
    @Autowired
    BienTheGiayRepository bienTheGiayRepository;

    @Override
    public List<HoaDonResponse> getAll() {
        return hoaDonRepository.findAllResponse();
    }

    @Override
    public Page<HoaDonResponse> getPage(int page, String searchText, Integer status) {
        return hoaDonRepository.getPageResponse(searchText, status, PageRequest.of((page - 1), 5));
    }

    @Override
    public Page<HoaDonYeuCauRespone> getPageHoaDonYeuCau(int page, HoaDonSearch hoaDonSearch) {
        return hoaDonRepository.getPageHoaDonYeuCauResponse(hoaDonSearch, PageRequest.of((page - 1), 5));
    }

    @Override
    public HoaDonResponse findById(Long id) {
        return new HoaDonResponse(hoaDonRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

    @Override
    public List<HoaDonBanHangResponse> getAllChuaThanhToan() {
        List<HoaDonBanHangResponse> list = hoaDonRepository.getAllChuaThanhToan();
        Map<Long, HoaDonBanHangResponse> responseMap = new HashMap<>();

        for (HoaDonBanHangResponse hd : list) {
            if (responseMap.containsKey(hd.getId())) {
                responseMap.get(hd.getId()).getHoaDonChiTiets().add(hd.getHoaDonChiTiets().get(0));
            } else {
                responseMap.put(hd.getId(), hd);
            }
        }

        return new ArrayList<>(responseMap.values());
    }

    @Override
    public HoaDonBanHangResponse createNewHoaDon() {
        HoaDon hoaDon = new HoaDon();
        hoaDon.setTrangThai(0);
        hoaDon.setNgayTao(LocalDateTime.now());

        hoaDon = hoaDonRepository.save(hoaDon);
        return new HoaDonBanHangResponse(hoaDon, hoaDon.getTrangThai());
    }

    @Override
    public HoaDonBanHangResponse addProduct(AddOrderProcuctRequest addOrderProcuctRequest) {
        HoaDon hoaDon = hoaDonRepository.findById(addOrderProcuctRequest.getIdHoaDon())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy hóa đơn"));

        BienTheGiay bienTheGiay = bienTheGiayRepository.findById(addOrderProcuctRequest.getIdGiay())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy giày này"));

        Set<HoaDonChiTiet> hoaDonChiTiets = hoaDon.getListHoaDonChiTiet();

        int soLuongCu = 0;
        boolean notExist = true;
        for (HoaDonChiTiet hdct : hoaDonChiTiets) {
            if (Objects.equals(hdct.getBienTheGiay().getId(), bienTheGiay.getId())) {
                soLuongCu = hdct.getSoLuong();
                hdct.setSoLuong(addOrderProcuctRequest.getSoLuong());
                notExist = false;
                break;
            }
        }

        if (notExist) {
            hoaDonChiTiets.add(HoaDonChiTiet.builder()
                    .hoaDon(hoaDon)
                    .bienTheGiay(bienTheGiay)
                    .soLuong(addOrderProcuctRequest.getSoLuong())
                    .build());
        }

        bienTheGiay.setSoLuong(bienTheGiay.getSoLuong() + soLuongCu - addOrderProcuctRequest.getSoLuong());
        bienTheGiayRepository.save(bienTheGiay);

        hoaDon = hoaDonRepository.save(hoaDon);
        List<HoaDonChiTietResponse> hoaDonChiTietResponses = hoaDon.getListHoaDonChiTiet().stream().map(HoaDonChiTietResponse::new).collect(Collectors.toList());
        return new HoaDonBanHangResponse(hoaDon.getId(), hoaDonChiTietResponses, hoaDon.getTrangThai());
    }

    @Override
    @Transactional
    public String deleteHoaDon(Long id) {
        HoaDon hoaDon = hoaDonRepository.findById(id).orElseThrow(() -> new NotFoundException("Không tìm thấy hóa đơn"));

        Set<HoaDonChiTiet> lstHoaDonCT = hoaDon.getListHoaDonChiTiet();
        List<BienTheGiay> bienTheGiays = new ArrayList<>();
        for (HoaDonChiTiet hd : lstHoaDonCT) {
            BienTheGiay bienThe = bienTheGiayRepository.findById(hd.getBienTheGiay().getId()).orElseThrow(() -> new NotFoundException("Không tìm thấy hóa đơn"));
            bienThe.setSoLuong(bienThe.getSoLuong() + hd.getSoLuong());
            bienTheGiays.add(bienThe);
        }
        bienTheGiayRepository.saveAll(bienTheGiays);

        hoaDonRepository.delete(hoaDon);
        return "Xóa thành công";
    }

    @Override
    public void deleteAllHoaDonChiTiet(Long idHd) {
        HoaDon hoaDon = hoaDonRepository.findById(idHd).orElseThrow(() -> new NotFoundException("Không tìm thấy hóa đơn"));

        Set<HoaDonChiTiet> lstHoaDonCT = hoaDon.getListHoaDonChiTiet();
        List<BienTheGiay> bienTheGiays = new ArrayList<>();
        for (HoaDonChiTiet hd : lstHoaDonCT) {
            BienTheGiay bienThe = bienTheGiayRepository.findById(hd.getBienTheGiay().getId()).orElseThrow(() -> new NotFoundException("Không tìm thấy hóa đơn"));
            bienThe.setSoLuong(bienThe.getSoLuong() + hd.getSoLuong());
            bienTheGiays.add(bienThe);
        }
        bienTheGiayRepository.saveAll(bienTheGiays);

    }
}
