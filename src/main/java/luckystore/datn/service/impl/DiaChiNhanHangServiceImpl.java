package luckystore.datn.service.impl;

import luckystore.datn.infrastructure.constraints.ErrorMessage;
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

import java.util.List;

@Service
public class DiaChiNhanHangServiceImpl implements DiaChiNhanHangService {

    @Autowired
    private DiaChiNhanHangRepository diaChiNhanHangRepo;
    @Autowired
    private KhachHangRepository khachHangRepo;


    @Override
    public Page<DiaChiNhanHangResponse> getPage(int page, String searchText, Integer status) {
        return diaChiNhanHangRepo.getPageResponse(searchText, status, PageRequest.of((page - 1), 4));
    }

    @Override
    public List<DiaChiNhanHangResponse> getPageByKhachHang(Long idKhachHang) {
        return diaChiNhanHangRepo.getPageResponseByKhachHang(idKhachHang);
    }

    @Override
    public DiaChiNhanHangResponse addDiaChiNhanHang(DiaChiNhanHangRequest diaChiNhanHangRequest) {
        List<DiaChiNhanHang> diaChiNhanHangList = diaChiNhanHangRepo.findByTrangThaiNot(0);
        for (DiaChiNhanHang diaChi : diaChiNhanHangList) {
            diaChi.setTrangThai(0); // hoặc cập nhật giá trị mong muốn
            diaChiNhanHangRepo.save(diaChi);
        }
        if (diaChiNhanHangRequest == null) {
            throw new NullException();
        }

        DiaChiNhanHang diaChiNhanHang = getDiaChiNhanHang(new DiaChiNhanHang(), diaChiNhanHangRequest);
        if(diaChiNhanHangRequest.getIdKhachHang() != null){
            KhachHang khachHang = khachHangRepo.findById(diaChiNhanHangRequest.getIdKhachHang()).get();
            diaChiNhanHang.setIdKhachHang(khachHang);
        }
        diaChiNhanHang.setTrangThai(1);
        return new DiaChiNhanHangResponse(diaChiNhanHangRepo.save(diaChiNhanHang));
    }

    @Override
    public DiaChiNhanHangResponse updateTrangThaiDiaChiNhan(Long id, DiaChiNhanHangRequest diaChiNhanHangRequest) {
        // Cập nhật tất cả các đối tượng khác có trạng thái khác nhau
        List<DiaChiNhanHang> diaChiNhanHangList = diaChiNhanHangRepo.findByTrangThaiNot(0);
        for (DiaChiNhanHang diaChi : diaChiNhanHangList) {
            diaChi.setTrangThai(0); // hoặc cập nhật giá trị mong muốn
            diaChiNhanHangRepo.save(diaChi);
        }
        if (id == null || diaChiNhanHangRequest == null) {
            throw new NullException();
        }

        DiaChiNhanHang diaChiNhanHangToUpdate = diaChiNhanHangRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        // Cập nhật đối tượng cần
        diaChiNhanHangToUpdate = getTrangThaiDiaChiNhanHang(diaChiNhanHangToUpdate, diaChiNhanHangRequest);

        // Cập nhật trạng thái của đối tượng cần
        diaChiNhanHangToUpdate.setTrangThai(1);

        // Lưu lại đối tượng cần
        diaChiNhanHangRepo.save(diaChiNhanHangToUpdate);

        return new DiaChiNhanHangResponse(diaChiNhanHangToUpdate);
    }

    @Override
    public DiaChiNhanHangResponse updateDiaChiNhanHang(Long id, DiaChiNhanHangRequest diaChiNhanHangRequest) {

        // Cập nhật tất cả các đối tượng khác có trạng thái khác nhau
        DiaChiNhanHang diaChiNhanHangToUpdate = diaChiNhanHangRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        // Cập nhật đối tượng cần
        diaChiNhanHangToUpdate = getDiaChiNhanHang(diaChiNhanHangToUpdate, diaChiNhanHangRequest);
        // Lưu lại đối tượng cần
        diaChiNhanHangToUpdate.setIdKhachHang(khachHangRepo.findIdKH(new KhachHang()));
        diaChiNhanHangRepo.save(diaChiNhanHangToUpdate);

        return new DiaChiNhanHangResponse(diaChiNhanHangToUpdate);
    }


    @Override
    public DiaChiNhanHangResponse findById(Long id) {

        return new DiaChiNhanHangResponse(diaChiNhanHangRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

    @Override
    public DiaChiNhanHangResponse findByIdKhachHang(Long id) {
        return diaChiNhanHangRepo.findByIdKhachHang(id);
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
        diaChiNhanHang.setDiaChiNhan(diaChiNhanHangRequest.getDiaChiNhan());
        diaChiNhanHang.setProvince(diaChiNhanHangRequest.getProvinces());
        diaChiNhanHang.setDistrict(diaChiNhanHangRequest.getDistricts());
        diaChiNhanHang.setWard(diaChiNhanHangRequest.getWards());
        diaChiNhanHang.setTrangThai(diaChiNhanHangRequest.getTrangThai() == null || diaChiNhanHangRequest.getTrangThai() == 0 ? 0 : 1);
        return diaChiNhanHang;
    }

    private DiaChiNhanHang getTrangThaiDiaChiNhanHang(DiaChiNhanHang diaChiNhanHang, DiaChiNhanHangRequest diaChiNhanHangRequest) {
//        System.out.println(diaChiNhanHangRequest);
//        diaChiNhanHang.setHoTen(diaChiNhanHangRequest.getHoTen());
//        diaChiNhanHang.setDiaChiNhan(diaChiNhanHangRequest.getDiaChiNhan());
//        diaChiNhanHang.setSoDienThoaiNhan(diaChiNhanHangRequest.getSoDienThoaiNhan());
//        diaChiNhanHang.setIdKhachHang(diaChiNhanHangRequest.getKhachHang());
//        diaChiNhanHang.setProvince(diaChiNhanHangRequest.getProvinces());
//        diaChiNhanHang.setDistrict(diaChiNhanHangRequest.getDistricts());
//        diaChiNhanHang.setWard(diaChiNhanHangRequest.getWards());
        diaChiNhanHang.setTrangThai(1);
        return diaChiNhanHang;
    }
}