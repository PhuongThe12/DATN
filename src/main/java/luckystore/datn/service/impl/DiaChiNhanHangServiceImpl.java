package luckystore.datn.service.impl;

import luckystore.datn.exception.InvalidIdException;
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
import luckystore.datn.util.JsonString;
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
        DiaChiNhanHang diaChiNhanHang = new DiaChiNhanHang();
        if(diaChiNhanHangRequest.getMacDinh() != null){
            List<DiaChiNhanHang> diaChiNhanHangList = diaChiNhanHangRepo.findByIdKhachHangAndTrangThaiZero(diaChiNhanHangRequest.getIdKhachHang());
            for (DiaChiNhanHang diaChi : diaChiNhanHangList) {
                diaChi.setTrangThai(0); // hoặc cập nhật giá trị mong muốn
                diaChiNhanHangRepo.save(diaChi);
            }
            diaChiNhanHang = getDiaChiNhanHang(new DiaChiNhanHang(), diaChiNhanHangRequest);
            if(diaChiNhanHangRequest.getIdKhachHang() != null){
                KhachHang khachHang = khachHangRepo.findById(diaChiNhanHangRequest.getIdKhachHang()).get();
                diaChiNhanHang.setIdKhachHang(khachHang);
            }
            diaChiNhanHang.setTrangThai(1);
        }else{
            diaChiNhanHang = getDiaChiNhanHang(new DiaChiNhanHang(), diaChiNhanHangRequest);
            if(diaChiNhanHangRequest.getIdKhachHang() != null){
                KhachHang khachHang = khachHangRepo.findById(diaChiNhanHangRequest.getIdKhachHang()).get();
                diaChiNhanHang.setIdKhachHang(khachHang);
            }
            diaChiNhanHang.setTrangThai(0);
        }


        if (diaChiNhanHangRequest == null) {
            throw new NullException();
        }


        return new DiaChiNhanHangResponse(diaChiNhanHangRepo.save(diaChiNhanHang));
    }

    @Override
    public DiaChiNhanHangResponse updateTrangThaiDiaChiNhan(Long id, DiaChiNhanHangRequest diaChiNhanHangRequest) {

        System.out.println(diaChiNhanHangRequest.toString());

        List<DiaChiNhanHang> diaChiNhanHangList = diaChiNhanHangRepo.findByIdKhachHangAndTrangThaiZero(diaChiNhanHangRequest.getIdKhachHang());
        for (DiaChiNhanHang diaChi : diaChiNhanHangList) {
            diaChi.setTrangThai(0);
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
        System.out.println(diaChiNhanHangRequest.toString());
        DiaChiNhanHang diaChiNhanHangToUpdate = diaChiNhanHangRepo.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));

        // Cập nhật đối tượng cần
        diaChiNhanHangToUpdate = getDiaChiNhanHang(diaChiNhanHangToUpdate, diaChiNhanHangRequest);
        KhachHang khachHang = khachHangRepo.findById(diaChiNhanHangRequest.getIdKhachHang()).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        diaChiNhanHangToUpdate.setIdKhachHang(khachHang);
        diaChiNhanHangRepo.save(diaChiNhanHangToUpdate);

        return new DiaChiNhanHangResponse(diaChiNhanHangToUpdate);
    }


    @Override
    public DiaChiNhanHangResponse findById(Long id) {

        return new DiaChiNhanHangResponse(diaChiNhanHangRepo.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

    @Override
    public List<DiaChiNhanHangResponse> findByIdKhachHang(Long id) {
        return diaChiNhanHangRepo.findByIdKhachHang(id);
    }

    @Override
    public DiaChiNhanHangResponse findOneByIdKhachHang(Long id) {
        return diaChiNhanHangRepo.findOneByIdKhachHang(id);
    }

    @Override
    public void deleteDieuKien(Long id) {
        DiaChiNhanHang diaChiNhanHang = diaChiNhanHangRepo.findById(id).orElseThrow(() -> new RuntimeException());
        if(diaChiNhanHang.getTrangThai() != 1){
            diaChiNhanHangRepo.delete(diaChiNhanHang);
        }else{
            throw new InvalidIdException(JsonString.stringToJson(JsonString.errorToJsonObject("data", "Địa chỉ đang là mặc định , không thể xóa !")));
        }
    }


    private DiaChiNhanHang getDiaChiNhanHang(DiaChiNhanHang diaChiNhanHang, DiaChiNhanHangRequest diaChiNhanHangRequest) {
//        System.out.println(diaChiNhanHangRequest);
        diaChiNhanHang.setHoTen(diaChiNhanHangRequest.getHoTen());
        diaChiNhanHang.setDiaChiNhan(diaChiNhanHangRequest.getDiaChiNhan());
        diaChiNhanHang.setSoDienThoaiNhan(diaChiNhanHangRequest.getSoDienThoaiNhan());
//        diaChiNhanHang.setIdKhachHang(diaChiNhanHangRequest.getKhachHang());
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