package luckystore.datn.service.impl;

import luckystore.datn.infrastructure.constraints.ErrorMessage;
import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.entity.KhuyenMai;
import luckystore.datn.entity.KhuyenMaiChiTiet;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.model.request.KhuyenMaiChiTietRequest;
import luckystore.datn.model.request.KhuyenMaiRequest;
import luckystore.datn.model.response.KhuyenMaiResponse;
import luckystore.datn.repository.KhuyenMaiChiTietRepository;
import luckystore.datn.repository.KhuyenMaiRepository;
import luckystore.datn.service.KhuyenMaiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service

public class KhuyenMaiServiceImpl implements KhuyenMaiService {

    @Autowired
    KhuyenMaiRepository khuyenMaiRepository;

    @Autowired
    KhuyenMaiChiTietRepository khuyenMaiChiTietRepository;

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
        KhuyenMai khuyenMai = new KhuyenMai();
        khuyenMai.setTen(khuyenMaiRequest.getTen());
        khuyenMai.setNgayBatDau(khuyenMaiRequest.getNgayBatDau());
        khuyenMai.setNgayKetThuc(khuyenMaiRequest.getNgayKetThuc());
        khuyenMai.setTrangThai(khuyenMaiRequest.getTrangThai());
        khuyenMai.setGhiChu(khuyenMaiRequest.getGhiChu());

        List<KhuyenMaiChiTiet> chiTietList = new ArrayList<>();
        for (KhuyenMaiChiTietRequest chiTietRequest : khuyenMaiRequest.getKhuyenMaiChiTietRequests()) {
            KhuyenMaiChiTiet chiTiet = new KhuyenMaiChiTiet();
            BienTheGiay bienTheGiay = new BienTheGiay();
            bienTheGiay.setId(chiTietRequest.getBienTheGiayId());
            chiTiet.setBienTheGiay(bienTheGiay);
            chiTiet.setPhanTramGiam(chiTietRequest.getPhanTramGiam());
            chiTiet.setKhuyenMai(khuyenMai);
            chiTietList.add(chiTiet);
        }
        khuyenMai.setKhuyenMaiChiTiets(chiTietList);
        khuyenMaiRepository.save(khuyenMai);
        khuyenMaiChiTietRepository.saveAll(chiTietList);
        return new KhuyenMaiResponse(khuyenMaiRepository.save(khuyenMai));
    }

    @Override
    public KhuyenMaiResponse updateKhuyenMai(Long id, KhuyenMaiRequest khuyenMaiRequest) {
        KhuyenMai khuyenMai = khuyenMaiRepository.findById(id).orElseThrow(() -> new RuntimeException());
        khuyenMai.setTen(khuyenMaiRequest.getTen());
        khuyenMai.setNgayBatDau(khuyenMaiRequest.getNgayBatDau());
        khuyenMai.setNgayKetThuc(khuyenMaiRequest.getNgayKetThuc());
        khuyenMai.setTrangThai(khuyenMaiRequest.getTrangThai());
        khuyenMai.setGhiChu(khuyenMaiRequest.getGhiChu());
        List<KhuyenMaiChiTiet> chiTietList = new ArrayList<>();
        for (KhuyenMaiChiTietRequest chiTietRequest : khuyenMaiRequest.getKhuyenMaiChiTietRequests()) {
            if (chiTietRequest.getId() != null) {
                KhuyenMaiChiTiet chiTiet = khuyenMaiChiTietRepository.findById(chiTietRequest.getId()).orElseThrow(() -> new RuntimeException());
                BienTheGiay bienTheGiay = new BienTheGiay();
                bienTheGiay.setId(chiTietRequest.getBienTheGiayId());
                chiTiet.setBienTheGiay(bienTheGiay);
                chiTiet.setPhanTramGiam(chiTietRequest.getPhanTramGiam());
                chiTiet.setKhuyenMai(khuyenMai);
                chiTietList.add(chiTiet);
            } else {
                KhuyenMaiChiTiet chiTiet = new KhuyenMaiChiTiet();
                BienTheGiay bienTheGiay = new BienTheGiay();
                bienTheGiay.setId(chiTietRequest.getBienTheGiayId());
                chiTiet.setBienTheGiay(bienTheGiay);
                chiTiet.setPhanTramGiam(chiTietRequest.getPhanTramGiam());
                chiTiet.setKhuyenMai(khuyenMai);
                chiTietList.add(chiTiet);
            }
        }
        khuyenMai.setKhuyenMaiChiTiets(chiTietList);
        khuyenMaiRepository.save(khuyenMai);
        return new KhuyenMaiResponse(khuyenMaiRepository.save(khuyenMai));
    }

    @Override
    public KhuyenMaiResponse findById(Long id) {
        return new KhuyenMaiResponse(khuyenMaiRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

}
