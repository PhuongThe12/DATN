package luckystore.datn.service.impl;

import luckystore.datn.entity.GioHang;
import luckystore.datn.entity.GioHangChiTiet;
import luckystore.datn.model.request.GioHangChiTietRequest;
import luckystore.datn.model.response.GioHangChiTietResponse;
import luckystore.datn.model.response.GioHangResponse;
import luckystore.datn.repository.BienTheGiayRepository;
import luckystore.datn.repository.GioHangChiTietRepository;
import luckystore.datn.repository.GioHangRepository;
import luckystore.datn.service.GioHangService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GioHangServiceImpl implements GioHangService {

    @Autowired
    GioHangRepository gioHangRepository;

    @Autowired
    GioHangChiTietRepository gioHangChiTietRepository;

    @Autowired
    BienTheGiayRepository bienTheGiayRepository;

    @Override
    public GioHangChiTietResponse addGiohangChiTiet(GioHangChiTietRequest gioHangChiTietRequest) {
        GioHangChiTiet gioHangChiTiet = getGioHangChiTiet(new GioHangChiTiet(),gioHangChiTietRequest);
        return new GioHangChiTietResponse(gioHangChiTietRepository.save(gioHangChiTiet));
    }

    @Override
    public GioHangResponse getGioHangByKhachHang(Long id) {
        return gioHangRepository.getGioHangByIdKhachHang(id);
    }

    @Override
    @Transactional
    public void updateSoLuongGioHang(GioHangChiTietRequest gioHangChiTietRequest) {
        gioHangChiTietRepository.updateSoLuongGioHangChiTiet(gioHangChiTietRequest.getSoLuong(),gioHangChiTietRequest.getId());
    }

    @Override
    public void deleteGioHangChiTiet(GioHangChiTietRequest gioHangChiTietRequest) {
        gioHangChiTietRepository.deleteById(gioHangChiTietRequest.getId());
    }

    public GioHangChiTiet getGioHangChiTiet(GioHangChiTiet gioHangChiTiet, GioHangChiTietRequest gioHangChiTietRequest){
        gioHangChiTiet.setId(gioHangChiTietRequest.getId());
        gioHangChiTiet.setGioHang(gioHangRepository.findById(gioHangChiTietRequest.getGioHang()).get());
        gioHangChiTiet.setBienTheGiay(bienTheGiayRepository.findById(gioHangChiTietRequest.getBienTheGiay()).get());
        gioHangChiTiet.setSoLuong(gioHangChiTiet.getSoLuong());
        gioHangChiTiet.setGiaBan(gioHangChiTiet.getGiaBan());
        gioHangChiTiet.setNgayTao(gioHangChiTiet.getNgayTao());
        gioHangChiTiet.setGhiChu(gioHangChiTiet.getGhiChu());
        return gioHangChiTiet;
    }
}
