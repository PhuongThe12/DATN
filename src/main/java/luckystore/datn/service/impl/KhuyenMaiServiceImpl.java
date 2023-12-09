package luckystore.datn.service.impl;

import luckystore.datn.infrastructure.constraints.ErrorMessage;
import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.entity.KhuyenMai;
import luckystore.datn.entity.KhuyenMaiChiTiet;
import luckystore.datn.exception.ConflictException;
import luckystore.datn.exception.InvalidIdException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.model.request.KhuyenMaiChiTietRequest;
import luckystore.datn.model.request.KhuyenMaiRequest;
import luckystore.datn.model.request.KhuyenMaiSearch;
import luckystore.datn.model.response.ChiTietKhuyenMaiResponse;
import luckystore.datn.model.response.GiayResponse;
import luckystore.datn.model.response.KhuyenMaiChiTietGiayResponse;
import luckystore.datn.model.response.KhuyenMaiChiTietResponse;
import luckystore.datn.model.response.KhuyenMaiResponse;
import luckystore.datn.repository.GiayRepository;
import luckystore.datn.repository.KhuyenMaiChiTietRepository;
import luckystore.datn.repository.KhuyenMaiRepository;
import luckystore.datn.service.KhuyenMaiService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service

public class KhuyenMaiServiceImpl implements KhuyenMaiService {

    @Autowired
    KhuyenMaiRepository khuyenMaiRepository;

    @Autowired
    KhuyenMaiChiTietRepository khuyenMaiChiTietRepository;

    @Autowired
    GiayRepository giayRepository;

    @Override
    public List<KhuyenMaiResponse> getAll() {
        return khuyenMaiRepository.findAllResponse();
    }

    @Override
    public Page<KhuyenMaiResponse> getPage(int page, String searchText, Integer status) {
        return khuyenMaiRepository.getPageResponse(searchText, status, PageRequest.of((page - 1), 5));
    }

    @Override
    public KhuyenMaiResponse addKhuyenMai(KhuyenMaiRequest khuyenMaiRequest) {

        List<Long> bienTheIds = new ArrayList<>();

        KhuyenMai khuyenMai = new KhuyenMai();

        khuyenMai.setTen(khuyenMaiRequest.getTen());
        khuyenMai.setNgayBatDau(khuyenMaiRequest.getNgayBatDau());
        khuyenMai.setNgayKetThuc(khuyenMaiRequest.getNgayKetThuc());
        khuyenMai.setTrangThai(khuyenMaiRequest.getTrangThai());
        khuyenMai.setGhiChu(khuyenMaiRequest.getGhiChu());
        List<KhuyenMaiChiTiet> chiTietList = new ArrayList<>();

        if(khuyenMai.getNgayBatDau().isBefore(LocalDateTime.now())) {
            throw new InvalidIdException("Ngày không được là ngày trong quá khứ");
        }

        if(!khuyenMai.getNgayBatDau().isBefore(khuyenMai.getNgayKetThuc())) {
            throw new InvalidIdException("Ngày kết thúc phải lớn hơn ngày bắt đầu");
        }

        for (KhuyenMaiChiTietRequest chiTietRequest : khuyenMaiRequest.getKhuyenMaiChiTietRequests()) {
            KhuyenMaiChiTiet chiTiet = new KhuyenMaiChiTiet();
            BienTheGiay bienTheGiay = new BienTheGiay();
            bienTheGiay.setId(chiTietRequest.getBienTheGiayId());
            chiTiet.setBienTheGiay(bienTheGiay);
            chiTiet.setPhanTramGiam(chiTietRequest.getPhanTramGiam());
            chiTiet.setKhuyenMai(khuyenMai);
            chiTietList.add(chiTiet);

            bienTheIds.add(chiTietRequest.getBienTheGiayId());
        }

        KhuyenMaiSearch khuyenMaiSearch = KhuyenMaiSearch.builder()
                .ngayBatDau(khuyenMaiRequest.getNgayBatDau())
                .ngayKetThuc(khuyenMaiRequest.getNgayKetThuc())
                .bienTheIds(bienTheIds)
                .build();

        List<Long> idDaTonTai = getDaTonTai(khuyenMaiSearch);

        if (idDaTonTai != null && !idDaTonTai.isEmpty()) {
            throw new ConflictException(idDaTonTai);
        }

        khuyenMai.setKhuyenMaiChiTiets(chiTietList);

        return new KhuyenMaiResponse(khuyenMaiRepository.save(khuyenMai));
    }

    @Override
    public KhuyenMaiResponse updateKhuyenMai(Long id, KhuyenMaiRequest khuyenMaiRequest) {
        KhuyenMai khuyenMai = khuyenMaiRepository.findById(id).orElseThrow(RuntimeException::new);

        if(khuyenMai.getNgayBatDau().isBefore(LocalDateTime.now()) && khuyenMai.getNgayKetThuc().isAfter(LocalDateTime.now())) {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("error", "Khuyến mại đang diễn ra không thể cập nhật")));
        }

        if(khuyenMai.getNgayKetThuc().isBefore(LocalDateTime.now())) {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("error", "Khuyến mại đã kết thúc không thể cập nhật")));
        }

        if(khuyenMaiRequest.getNgayBatDau().isBefore(LocalDateTime.now())) {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("error", "Ngày không được là ngày trong quá khứ")));
        }

        if(!khuyenMaiRequest.getNgayBatDau().isBefore(khuyenMai.getNgayKetThuc())) {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("error", "Ngày kết thúc phải lớn hơn ngày bắt đầu")));
        }

        List<Long> bienTheIds = new ArrayList<>();

        khuyenMai.setTen(khuyenMaiRequest.getTen());
        khuyenMai.setNgayBatDau(khuyenMaiRequest.getNgayBatDau());
        khuyenMai.setNgayKetThuc(khuyenMaiRequest.getNgayKetThuc());
        khuyenMai.setTrangThai(khuyenMaiRequest.getTrangThai());
        khuyenMai.setGhiChu(khuyenMaiRequest.getGhiChu());

        khuyenMai.getKhuyenMaiChiTiets().clear();
        List<KhuyenMaiChiTiet> chiTietList = khuyenMai.getKhuyenMaiChiTiets();

        for (KhuyenMaiChiTietRequest chiTietRequest : khuyenMaiRequest.getKhuyenMaiChiTietRequests()) {
            KhuyenMaiChiTiet chiTiet = new KhuyenMaiChiTiet();
            BienTheGiay bienTheGiay = new BienTheGiay();
            bienTheGiay.setId(chiTietRequest.getBienTheGiayId());
            chiTiet.setBienTheGiay(bienTheGiay);
            chiTiet.setPhanTramGiam(chiTietRequest.getPhanTramGiam());
            chiTiet.setKhuyenMai(khuyenMai);
            chiTietList.add(chiTiet);

            bienTheIds.add(chiTietRequest.getBienTheGiayId());
        }

        KhuyenMaiSearch khuyenMaiSearch = KhuyenMaiSearch.builder()
                .id(khuyenMai.getId())
                .ngayBatDau(khuyenMaiRequest.getNgayBatDau())
                .ngayKetThuc(khuyenMaiRequest.getNgayKetThuc())
                .bienTheIds(bienTheIds)
                .build();

        List<Long> idDaTonTai = getDaTonTaiAndIdNot(khuyenMaiSearch);

        if (idDaTonTai != null && !idDaTonTai.isEmpty()) {
            throw new ConflictException(idDaTonTai);
        }

        return new KhuyenMaiResponse(khuyenMaiRepository.save(khuyenMai));
    }

    @Override
    public KhuyenMaiResponse findById(Long id) {
        return new KhuyenMaiResponse(khuyenMaiRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

    @Override
    public ChiTietKhuyenMaiResponse getKhuyenMaiById(Long id) {
        ChiTietKhuyenMaiResponse chiTietKhuyenMaiResponse = khuyenMaiRepository.getKhuyenMaiById(id);
        if (chiTietKhuyenMaiResponse == null) {
            throw new NotFoundException(ErrorMessage.NOT_FOUND);
        }

        List<KhuyenMaiChiTietResponse> khuyenMaiChiTietResponses = khuyenMaiChiTietRepository.getAllByIdKhuyenMai(chiTietKhuyenMaiResponse.getId());

        Set<Long> idGiays = new HashSet<>();
        khuyenMaiChiTietResponses.forEach(item -> {
            idGiays.add(item.getBienTheGiayResponsel().getGiayResponse().getId());
        });

        List<GiayResponse> giayResponses = giayRepository.getAllGiayById(idGiays);

        Map<Long, GiayResponse> giayResponsesMap = new HashMap<>();
        giayResponses.forEach(giayResponse -> {
            khuyenMaiChiTietResponses.forEach(kmct -> {
                if(Objects.equals(kmct.getBienTheGiayResponsel().getId(), giayResponse.getLstBienTheGiay().get(0).getId())) {
                    giayResponse.getLstBienTheGiay().get(0).setPhanTramGiam(kmct.getPhanTramGiam());
                }
            });
            if (giayResponsesMap.containsKey(giayResponse.getId())) {
                if (giayResponse.getLstBienTheGiay().size() == 1) {
                    giayResponsesMap.get(giayResponse.getId()).getLstBienTheGiay().add(giayResponse.getLstBienTheGiay().get(0));
                }
            } else {
                giayResponsesMap.put(giayResponse.getId(), giayResponse);
            }
        });

        List<GiayResponse> giayResponseList = new ArrayList<>(giayResponsesMap.values());
        chiTietKhuyenMaiResponse.setKhuyenMaiChiTietResponses(new KhuyenMaiChiTietGiayResponse(giayResponseList));

        return chiTietKhuyenMaiResponse;
    }

    @Override
    public Page<KhuyenMaiResponse> searchingKhuyenMai(KhuyenMaiSearch kmSearch) {
        Pageable pageable = PageRequest.of(kmSearch.getCurrentPage() -1, kmSearch.getPageSize());
        if(kmSearch.getStatus() == 0) {
            return khuyenMaiRepository.getSearchingKhuyenMaiDaAn(kmSearch, pageable);
        } else if(kmSearch.getStatus() == 1) {
            return khuyenMaiRepository.getSearchingKhuyenMaiDangDienRa(kmSearch, pageable);
        } else if(kmSearch.getStatus() == 2) {
            return khuyenMaiRepository.getSearchingKhuyenMaiSapDienRa(kmSearch, pageable);
        } else if(kmSearch.getStatus() == 3) {
            return khuyenMaiRepository.getSearchingKhuyenMaiDaKetThuc(kmSearch, pageable);
        }
        return khuyenMaiRepository.getSearchingKhuyenMaiDangDienRa(kmSearch, pageable);
    }


    private List<Long> getDaTonTai(KhuyenMaiSearch kmSearch) {
        return khuyenMaiRepository.getDaTonTaiKhuyenMai(kmSearch);
    }

    private List<Long> getDaTonTaiAndIdNot(KhuyenMaiSearch kmSearch) {
        return khuyenMaiRepository.getDaTonTaiKhuyenMaiAndIdNot(kmSearch);
    }

}
