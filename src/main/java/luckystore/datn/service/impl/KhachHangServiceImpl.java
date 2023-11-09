package luckystore.datn.service.impl;

import luckystore.datn.constraints.ErrorMessage;
import luckystore.datn.entity.HangKhachHang;
import luckystore.datn.entity.KhachHang;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.exception.NullException;
import luckystore.datn.model.request.KhachHangRequest;
import luckystore.datn.model.response.KhachHangResponse;
import luckystore.datn.repository.HangKhachHangRepository;
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

    @Autowired
    private HangKhachHangRepository hangKhachHangRepo;

    @Override
    public List<KhachHangResponse> getAll() {
        List<KhachHangResponse> list = khachHangRepo.findAllResponse();
        return khachHangRepo.findAllResponse();
    }

    @Override
    public Page<KhachHangResponse> getPage(int page, String searchText, Integer status) {
        return khachHangRepo.getPageResponse(searchText, status, PageRequest.of((page - 1), 5));
    }

    @Override
    public KhachHangResponse addKhachHang(KhachHangRequest khachHangRequest) {
        KhachHang khachHang = getKhachHang(new KhachHang(), khachHangRequest);
        HangKhachHang hangKhachHang = new HangKhachHang();
        khachHang.setDiemTichLuy(0);
        if(khachHang.getDiemTichLuy()>=30){
            khachHang.setHangKhachHang(hangKhachHangRepo.findHangVip5(hangKhachHang));
        }else if(khachHang.getDiemTichLuy()>=20){
            khachHang.setHangKhachHang(hangKhachHangRepo.findHangVip4(hangKhachHang));
        }else if(khachHang.getDiemTichLuy()>=15){
            khachHang.setHangKhachHang(hangKhachHangRepo.findHangVip3(hangKhachHang));
        }else if(khachHang.getDiemTichLuy()>=10){
            khachHang.setHangKhachHang(hangKhachHangRepo.findHangVip2(hangKhachHang));
        }else {
            khachHang.setHangKhachHang(hangKhachHangRepo.findHangVip1(hangKhachHang));
        }
        return new KhachHangResponse(khachHangRepo.save(khachHang));
    }

    @Override
    public KhachHangResponse updateKhachHang(Long id, KhachHangRequest khachHangRequest) {
        KhachHang khachHang;
        if (id == null) {
            throw new NullException();
        } else {
            khachHang = khachHangRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        }
        khachHang = getKhachHang(khachHang, khachHangRequest);
        HangKhachHang hangKhachHang = new HangKhachHang();
        if(khachHang.getDiemTichLuy()>=30){
            khachHang.setHangKhachHang(hangKhachHangRepo.findHangVip5(hangKhachHang));
        }else if(khachHang.getDiemTichLuy()>=20){
            khachHang.setHangKhachHang(hangKhachHangRepo.findHangVip4(hangKhachHang));
        }else if(khachHang.getDiemTichLuy()>=15){
            khachHang.setHangKhachHang(hangKhachHangRepo.findHangVip3(hangKhachHang));
        }else if(khachHang.getDiemTichLuy()>=10){
            khachHang.setHangKhachHang(hangKhachHangRepo.findHangVip2(hangKhachHang));
        }else {
            khachHang.setHangKhachHang(hangKhachHangRepo.findHangVip1(hangKhachHang));
        }
        return new KhachHangResponse(khachHangRepo.save(khachHang));
    }

    @Override
    public KhachHangResponse findById(Long id) {
        return new KhachHangResponse(khachHangRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
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
    private KhachHang getKhachHangDieuKien(KhachHang khachHang, KhachHangRequest khachHangRequest) {
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
