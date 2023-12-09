package luckystore.datn.service.impl;

import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.entity.GioHang;
import luckystore.datn.entity.GioHangChiTiet;
import luckystore.datn.exception.ConflictException;
import luckystore.datn.model.request.BienTheGiayGioHangRequest;
import luckystore.datn.model.request.BienTheGiayRequest;
import luckystore.datn.model.request.GioHangChiTietRequest;
import luckystore.datn.model.request.GioHangRequest;
import luckystore.datn.model.response.GioHangChiTietResponse;
import luckystore.datn.model.response.GioHangResponse;
import luckystore.datn.repository.BienTheGiayRepository;
import luckystore.datn.repository.GioHangChiTietRepository;
import luckystore.datn.repository.GioHangRepository;
import luckystore.datn.repository.KhachHangRepository;
import luckystore.datn.service.GioHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class GioHangServiceImpl implements GioHangService {

    @Autowired
    GioHangRepository gioHangRepository;

    @Autowired
    GioHangChiTietRepository gioHangChiTietRepository;

    @Autowired
    BienTheGiayRepository bienTheGiayRepository;

    @Autowired
    KhachHangRepository khachHangRepository;

    @Override
    public GioHangChiTietResponse addGiohangChiTiet(GioHangChiTietRequest gioHangChiTietRequest) {
        GioHangChiTiet gioHangChiTiet = getGioHangChiTiet(new GioHangChiTiet(), gioHangChiTietRequest);
        return new GioHangChiTietResponse(gioHangChiTietRepository.save(gioHangChiTiet));
    }

    @Override
    public GioHangResponse getGioHangByKhachHang(Long id) {
        return gioHangRepository.getGioHangByIdKhachHang(id);
    }

    @Override
    @Transactional
    public void updateSoLuongGioHang(GioHangChiTietRequest gioHangChiTietRequest) {
        gioHangChiTietRepository.updateSoLuongGioHangChiTiet(gioHangChiTietRequest.getSoLuong(), gioHangChiTietRequest.getId());
    }

    @Override
    public void deleteGioHangChiTiet(GioHangChiTietRequest gioHangChiTietRequest) {
        gioHangChiTietRepository.deleteById(gioHangChiTietRequest.getId());
    }

    @Override
    public void checkSoLuong(Set<BienTheGiayGioHangRequest> bienTheGiayGioHangRequestSet) {
        checkSoLuongForList(bienTheGiayGioHangRequestSet);
    }

    @Override
    public Integer getSoLuong(Long id, Long idGioHang) {
        return gioHangChiTietRepository.getSoLuong(id, idGioHang);
    }

    @Override
    public void deleteAllGioHangChiTiet(List<GioHangChiTietRequest> gioHangChiTietRequestList) {
        List<GioHangChiTiet> gioHangChiTietList = new ArrayList<>();
        for (GioHangChiTietRequest ghct : gioHangChiTietRequestList) {
            GioHangChiTiet gioHangChiTiet = getGioHangChiTiet(new GioHangChiTiet(), ghct);
            gioHangChiTietList.add(gioHangChiTiet);
        }
        gioHangChiTietRepository.deleteAll(gioHangChiTietList);
    }

    @Override
    public GioHangResponse addGioHang(GioHangRequest gioHangRequest) {
        GioHang gioHang = new GioHang();
        gioHang.setKhachHang(khachHangRepository.findById(gioHangRequest.getKhachHang().getId()).get());
        gioHang.setNgayTao(LocalDateTime.now());
        gioHang.setGhiChu("NaN");
        gioHang.setTrangThai(1);
        GioHang gioHangSaved = gioHangRepository.save(gioHang);
        if(gioHangRequest.getGioHangChiTietRequestList() != null){
            List<GioHangChiTiet> gioHangChiTietList = getListGioHangChiTiet(gioHangRequest.getGioHangChiTietRequestList(),gioHangSaved);
            gioHangChiTietRepository.saveAll(gioHangChiTietList);
        }

        return new GioHangResponse(gioHangRepository.save(gioHang));
    }

    public GioHangChiTiet getGioHangChiTiet(GioHangChiTiet gioHangChiTiet, GioHangChiTietRequest gioHangChiTietRequest) {
        GioHangChiTietResponse gioHangChiTietResponse = gioHangChiTietRepository.findByGioHangAndBienTheGiay(gioHangChiTietRequest.getGioHang(), gioHangChiTietRequest.getBienTheGiay());
        if (gioHangChiTietResponse == null) {
            BienTheGiay bienTheGiay = bienTheGiayRepository.findById(gioHangChiTietRequest.getBienTheGiay()).get();
            gioHangChiTiet.setGioHang(gioHangRepository.findById(gioHangChiTietRequest.getGioHang()).get());
            gioHangChiTiet.setBienTheGiay(bienTheGiay);
            gioHangChiTiet.setSoLuong(gioHangChiTietRequest.getSoLuong());
            gioHangChiTiet.setGiaBan(bienTheGiay.getGiaBan());
            gioHangChiTiet.setNgayTao(LocalDateTime.now());
            gioHangChiTiet.setGhiChu(gioHangChiTiet.getGhiChu());
        } else {
            gioHangChiTiet.setId(gioHangChiTietResponse.getId());
            BienTheGiay bienTheGiay = bienTheGiayRepository.findById(gioHangChiTietRequest.getBienTheGiay()).get();
            gioHangChiTiet.setGioHang(gioHangRepository.findById(gioHangChiTietRequest.getGioHang()).get());
            gioHangChiTiet.setBienTheGiay(bienTheGiay);
            gioHangChiTiet.setSoLuong(gioHangChiTietResponse.getSoLuong() + gioHangChiTietRequest.getSoLuong());
            gioHangChiTiet.setGiaBan(bienTheGiay.getGiaBan());
            gioHangChiTiet.setNgayTao(LocalDateTime.now());
            gioHangChiTiet.setGhiChu(gioHangChiTiet.getGhiChu());
        }
        return gioHangChiTiet;
    }

    private void checkSoLuongForList(Set<BienTheGiayGioHangRequest> list) {
        List<String> errors = new ArrayList<>();
        for (BienTheGiayGioHangRequest bienTheGiayRequest : list) {
            Integer soLuongKho = bienTheGiayRepository.getSoLuong(bienTheGiayRequest.getId());
            if (bienTheGiayRequest.getSoLuongMua() > soLuongKho) {
                String error = "";
                if (bienTheGiayRepository.getSoLuong(bienTheGiayRequest.getId()) == 0) {
                    error = "" + bienTheGiayRequest.getId() + ": 1";
                } else {
                    error = "" + bienTheGiayRequest.getId() + ": 2" + ": " + soLuongKho;
                }
                errors.add(error);

            }
        }
        if (!errors.isEmpty()) {
            throw new ConflictException(errors);
        }
    }

    private List<GioHangChiTiet> getListGioHangChiTiet(List<GioHangChiTietRequest> gioHangChiTietRequestList, GioHang gioHang) {
        List<GioHangChiTiet> list = new ArrayList<>();
        for (GioHangChiTietRequest ghct : gioHangChiTietRequestList) {
            GioHangChiTiet gioHangChiTiet = new GioHangChiTiet();
            gioHangChiTiet.setGioHang(gioHang);
            gioHangChiTiet.setBienTheGiay(bienTheGiayRepository.findById(ghct.getBienTheGiay()).get());
            gioHangChiTiet.setSoLuong(ghct.getSoLuong());
            gioHangChiTiet.setGiaBan(ghct.getGiaBan());
            gioHangChiTiet.setNgayTao(LocalDateTime.now());
            gioHangChiTiet.setGhiChu("");
            list.add(gioHangChiTiet);
        }
        return list;
    }
}
