package luckystore.datn.service.impl;

import luckystore.datn.entity.DieuKien;
import luckystore.datn.entity.DotGiamGia;
import luckystore.datn.exception.DuplicateException;
import luckystore.datn.exception.InvalidIdException;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.infrastructure.constraints.ErrorMessage;
import luckystore.datn.model.request.DieuKienRequest;
import luckystore.datn.model.request.DotGiamGiaRequest;
import luckystore.datn.model.request.KhuyenMaiSearch;
import luckystore.datn.model.response.DotGiamGiaResponse;
import luckystore.datn.repository.DieuKienRepository;
import luckystore.datn.repository.DotGiamGiaRepository;
import luckystore.datn.service.DotGiamGiaService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DotGiamGiaServiceImpl implements DotGiamGiaService {


    @Autowired
    DieuKienRepository dieuKienRepository;
    @Autowired
    private DotGiamGiaRepository dotGiamGiaRepository;

    @Override
    public List<DotGiamGiaResponse> getAll() {
        return dotGiamGiaRepository.findAllResponse();
    }

    @Override
    public Page<DotGiamGiaResponse> getPage(int page, String searchText, Integer status) {
        return dotGiamGiaRepository.getPageResponse(searchText, status, PageRequest.of((page - 1), 5));
    }

    @Override
    public DotGiamGiaResponse addDotGiamGia(DotGiamGiaRequest dotGiamGiaRequest) {
        checkWhenInsert(dotGiamGiaRequest);

        DotGiamGia dotGiamGia = new DotGiamGia();
        dotGiamGia.setTen(dotGiamGiaRequest.getTen());
        dotGiamGia.setGhiChu(dotGiamGiaRequest.getGhiChu());
        dotGiamGia.setNgayBatDau(dotGiamGiaRequest.getNgayBatDau());
        dotGiamGia.setNgayKetThuc(dotGiamGiaRequest.getNgayKetThuc());
        dotGiamGia.setTrangThai(dotGiamGiaRequest.getTrangThai());
        dotGiamGia.setDanhSachDieuKien(new ArrayList<>());

        if (dotGiamGiaRequest.getDieuKienRequests().isEmpty()) {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không có điều kiện cho đợt giảm giá")));
        }

        for (DieuKienRequest dieuKienRequest : dotGiamGiaRequest.getDieuKienRequests()) {
            DieuKien dieuKien = new DieuKien();
            dieuKien.setTongHoaDon(dieuKienRequest.getTongHoaDon());
            dieuKien.setPhanTramGiam(dieuKienRequest.getPhanTramGiam());
            dieuKien.setDotGiamGia(dotGiamGia);
            dotGiamGia.getDanhSachDieuKien().add(dieuKien);
        }

        return new DotGiamGiaResponse(dotGiamGiaRepository.save(dotGiamGia));
    }

    @Override
    public DotGiamGiaResponse updateDotGiamGia(Long id, DotGiamGiaRequest dotGiamGiaRequest) {
        DotGiamGia dotGiamGia = dotGiamGiaRepository.findById(id).orElseThrow(() -> new RuntimeException());
        dotGiamGia.setTen(dotGiamGiaRequest.getTen());
        dotGiamGia.setGhiChu(dotGiamGiaRequest.getGhiChu());
        dotGiamGia.setNgayBatDau(dotGiamGiaRequest.getNgayBatDau());
        dotGiamGia.setNgayKetThuc(dotGiamGiaRequest.getNgayKetThuc());
        dotGiamGia.setTrangThai(dotGiamGiaRequest.getTrangThai());
        dotGiamGia.getDanhSachDieuKien().clear();

        List<DieuKien> listDK = dotGiamGia.getDanhSachDieuKien();

        if (dotGiamGia.getNgayBatDau().isBefore(LocalDateTime.now()) && dotGiamGia.getNgayKetThuc().isAfter(LocalDateTime.now())) {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Đợt giảm giá đang diễn ra không thể cập nhật")));
        }
        if (dotGiamGia.getNgayKetThuc().isBefore(LocalDateTime.now())) {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Đợt giảm giá đã kết thúc không thể cập nhật")));
        }

        if (dotGiamGiaRequest.getDieuKienRequests().isEmpty()) {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Không có điều kiện cho đợt giảm giá")));
        }

        for (DieuKienRequest dieuKienRequest : dotGiamGiaRequest.getDieuKienRequests()) {
            DieuKien dieuKien = new DieuKien();
            dieuKien.setTongHoaDon(dieuKienRequest.getTongHoaDon());
            dieuKien.setPhanTramGiam(dieuKienRequest.getPhanTramGiam());
            dieuKien.setDotGiamGia(dotGiamGia);
            listDK.add(dieuKien);
        }

        return new DotGiamGiaResponse(dotGiamGiaRepository.save(dotGiamGia));
    }


    @Override
    public DotGiamGiaResponse findById(Long id) {
        return new DotGiamGiaResponse(dotGiamGiaRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

    @Override
    public List<DotGiamGiaResponse> getAllActive() {
        return dotGiamGiaRepository.getAllActive();
    }

    @Override
    public Page<DotGiamGiaResponse> searchingDotGiamGia(KhuyenMaiSearch kmSearch) {
        Pageable pageable = PageRequest.of(kmSearch.getCurrentPage() - 1, kmSearch.getPageSize());
        if (kmSearch.getStatus() == 0) {
            return dotGiamGiaRepository.getSearchingDotGiamGiaDaAn(kmSearch, pageable);
        } else if (kmSearch.getStatus() == 1) {
            return dotGiamGiaRepository.getSearchingDotGiamGiaDangDienRa(kmSearch, pageable);
        } else if (kmSearch.getStatus() == 2) {
            return dotGiamGiaRepository.getSearchingDotGiamGiaSapDienRa(kmSearch, pageable);
        } else if (kmSearch.getStatus() == 3) {
            return dotGiamGiaRepository.getSearchingDotGiamGiaDaKetThuc(kmSearch, pageable);
        }
        return dotGiamGiaRepository.getSearchingDotGiamGiaDangDienRa(kmSearch, pageable);
    }

    @Override
    public Long hienThiDotGiamGia(Long id) {
        DotGiamGia khuyenMai = dotGiamGiaRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        if (khuyenMai.getNgayBatDau().isBefore(LocalDateTime.now()) && khuyenMai.getNgayKetThuc().isAfter(LocalDateTime.now())) {
            khuyenMai.setTrangThai(1);
            dotGiamGiaRepository.save(khuyenMai);
            return id;
        } else {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Khuyến mại đã kết thúc hoặc chưa bắt đầu không thể thay đổi")));
        }
    }

    @Override
    public Long anDotGiamGia(Long id) {
        DotGiamGia khuyenMai = dotGiamGiaRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        if (khuyenMai.getNgayBatDau().isBefore(LocalDateTime.now()) && khuyenMai.getNgayKetThuc().isAfter(LocalDateTime.now())) {
            khuyenMai.setTrangThai(0);
            dotGiamGiaRepository.save(khuyenMai);
            return id;
        } else {
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Khuyến mại đã kết thúc hoặc chưa bắt đầu không thể thay đổi")));
        }
    }

    private void checkWhenInsert(DotGiamGiaRequest dotGiamGiaRequest) {
        if (dotGiamGiaRepository.existsByTen(dotGiamGiaRequest.getTen())) {
            String errorObject = JsonString.errorToJsonObject("ten", "Tên đã tồn tại");
            throw new DuplicateException(JsonString.stringToJson(errorObject));
        }
    }

}