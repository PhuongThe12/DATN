package luckystore.datn.service.impl;

import luckystore.datn.constraints.ErrorMessage;
import luckystore.datn.entity.KhachHang;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.exception.NullException;
import luckystore.datn.model.request.KhachHangRequest;
import luckystore.datn.model.response.KhachHangRestponse;
import luckystore.datn.repository.KhachHangRepository;
import luckystore.datn.service.KhachHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class KhachHangServiceImpl implements KhachHangService {


    @Autowired
    private KhachHangRepository khachHangRepo;

    @Override
    public List<KhachHangRestponse> getAll() {

        return khachHangRepo.findAllResponse();
    }

    @Override
    public Page<KhachHangRestponse> getPage(int page, String searchText, Integer status) {
        return khachHangRepo.getPageResponse(searchText, status, PageRequest.of((page - 1), 5));
    }

    @Override
    public KhachHangRestponse addKhachHang(KhachHangRequest khachHangRequest) {
        KhachHang khachHang = getKhachHang(new KhachHang(), khachHangRequest);
        khachHang.setDiemTichLuy(0);
        return new KhachHangRestponse(khachHangRepo.save(khachHang));
    }

    @Override
    public KhachHangRestponse updateKhachHang(Long id, KhachHangRequest khachHangRequest) {
        KhachHang khachHang;
        if (id == null) {
            throw new NullException();
        } else {
            khachHang = khachHangRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        }
        khachHang = getKhachHang(khachHang, khachHangRequest);
        return new KhachHangRestponse(khachHangRepo.save(khachHang));
    }

    @Override
    public KhachHangRestponse findById(Long id) {
        return new KhachHangRestponse(khachHangRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

    @Override
    public List<KhachHangRestponse> searchByName(String searchText) {
        return khachHangRepo.searchByName(searchText);
    }

    private KhachHang getKhachHang(KhachHang khachHang, KhachHangRequest khachHangRequest) {
        khachHang.setHoTen(khachHangRequest.getHoTen());
        khachHang.setGioiTinh(khachHangRequest.getGioiTinh());
        khachHang.setSoDienThoai(khachHangRequest.getSoDienThoai());
        khachHang.setNgaySinh(khachHangRequest.getNgaySinh());
        khachHang.setEmail(khachHangRequest.getEmail());
        khachHang.setDiemTichLuy(khachHangRequest.getDiemTichLuy());
        khachHang.setHangKhachHang(khachHangRequest.getHangKhachHang());
        khachHang.setTrangThai(khachHangRequest.getTrangThai() == null || khachHangRequest.getTrangThai() == 0 ? 0 : 1);
        return khachHang;
    }
}
