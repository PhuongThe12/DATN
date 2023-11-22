package luckystore.datn.service.impl;

import luckystore.datn.constraints.ErrorMessage;
import luckystore.datn.entity.DiaChiNhanHang;
import luckystore.datn.entity.KhachHang;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.exception.NullException;
import luckystore.datn.model.request.DiaChiNhanHangRequest;
import luckystore.datn.model.response.DiaChiNhanHangResponse;
import luckystore.datn.repository.DiaChiNhanHangRepository;
import luckystore.datn.repository.KhachHangRepository;
import luckystore.datn.service.DiaChiNhanHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class DiaChiNhanHangServiceImpl implements DiaChiNhanHangService {

    @Autowired
    private DiaChiNhanHangRepository diaChiNhanHangRepo;
    @Autowired
    private KhachHangRepository khachHangRepo;


    @Override
    public Page<DiaChiNhanHangResponse> getPage(int page, String searchText, Integer status) {
        return diaChiNhanHangRepo.getPageResponse(searchText, status, PageRequest.of((page - 1), 5));
    }

    @Override
    public DiaChiNhanHangResponse addDiaChiNhanHang(DiaChiNhanHangRequest diaChiNhanHangRequest) {
        DiaChiNhanHang diaChiNhanHang = getDiaChiNhanHang(new DiaChiNhanHang(), diaChiNhanHangRequest);
        diaChiNhanHang.setIdKhachHang(khachHangRepo.findIdKH(new KhachHang()));
//        diaChiNhanHang.setProvince();
        return new DiaChiNhanHangResponse(diaChiNhanHangRepo.save(diaChiNhanHang));
    }

    @Override
    public DiaChiNhanHangResponse updateDiaChiNhanHang(Long id, DiaChiNhanHangRequest diaChiNhanHangRequest) {
        DiaChiNhanHang diaChiNhanHang;
        if (id == null) {
            throw new NullException();
        } else {
            diaChiNhanHang = diaChiNhanHangRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        }
        diaChiNhanHang = getDiaChiNhanHang(diaChiNhanHang, diaChiNhanHangRequest);
        return new DiaChiNhanHangResponse(diaChiNhanHangRepo.save(diaChiNhanHang));
    }

    @Override
    public DiaChiNhanHangResponse findById(Long id) {

        return new DiaChiNhanHangResponse(diaChiNhanHangRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

    @Override
    public void deleteDieuKien(Long id) {
        DiaChiNhanHang diaChiNhanHang = diaChiNhanHangRepo.findById(id).orElseThrow(() -> new RuntimeException());
        diaChiNhanHangRepo.delete(diaChiNhanHang);
    }


    private DiaChiNhanHang getDiaChiNhanHang(DiaChiNhanHang diaChiNhanHang, DiaChiNhanHangRequest diaChiNhanHangRequest) {
//        System.out.println(diaChiNhanHangRequest);
        diaChiNhanHang.setHoTen(diaChiNhanHangRequest.getHoTen());
        diaChiNhanHang.setDiaChiNhan(diaChiNhanHangRequest.getDiaChiNhan());
        diaChiNhanHang.setSoDienThoaiNhan(diaChiNhanHangRequest.getSoDienThoaiNhan());
        diaChiNhanHang.setIdKhachHang(diaChiNhanHangRequest.getKhachHang());
        diaChiNhanHang.setProvince(diaChiNhanHangRequest.getProvinces());
        diaChiNhanHang.setDistrict(diaChiNhanHangRequest.getDistricts());
        diaChiNhanHang.setWard(diaChiNhanHangRequest.getWards());
        diaChiNhanHang.setTrangThai(diaChiNhanHangRequest.getTrangThai() == null || diaChiNhanHangRequest.getTrangThai() == 0 ? 0 : 1);
        return diaChiNhanHang;
    }
}
