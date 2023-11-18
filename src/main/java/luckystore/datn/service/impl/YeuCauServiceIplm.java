package luckystore.datn.service.impl;


import luckystore.datn.constraints.ErrorMessage;
import luckystore.datn.entity.*;
import luckystore.datn.exception.NotFoundException;
import luckystore.datn.exception.NullException;
import luckystore.datn.model.request.YeuCauChiTietRequest;
import luckystore.datn.model.request.YeuCauRequest;
import luckystore.datn.model.response.YeuCauResponse;
import luckystore.datn.repository.*;
import luckystore.datn.service.ImageHubService;
import luckystore.datn.service.YeuCauService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class YeuCauServiceIplm implements YeuCauService {

    private final YeuCauRepository yeuCauRepository;
    private final YeuCauChiTietRepository yeuCauChiTietRepository;
    private final HoaDonRepository hoaDonRepository;
    private final HoaDonChiTietRepository hoaDonChiTietRepository;
    private final BienTheGiayRepository bienTheGiayRepository;

    private final ImageHubService imageHubService;

    @Autowired
    public YeuCauServiceIplm(YeuCauRepository yeuCauRepo, YeuCauChiTietRepository yeuCauChiTietRepository, HoaDonRepository hoaDonRepository, HoaDonChiTietRepository hoaDonChiTietRepository, BienTheGiayRepository bienTheGiayRepository, ImageHubService imageHubService) {
        this.yeuCauRepository = yeuCauRepo;
        this.yeuCauChiTietRepository = yeuCauChiTietRepository;
        this.hoaDonRepository = hoaDonRepository;
        this.hoaDonChiTietRepository = hoaDonChiTietRepository;
        this.bienTheGiayRepository = bienTheGiayRepository;
        this.imageHubService = imageHubService;
    }

    public List<YeuCauResponse> getAll() {
        return yeuCauRepository.finAllResponse();
    }

    @Override
    public YeuCauResponse addYeuCau(YeuCauRequest yeuCauRequest) {
        HoaDon hoaDon = hoaDonRepository.findById(yeuCauRequest.getHoaDon()).orElse(null);
        YeuCau yeuCau = new YeuCau(yeuCauRequest,hoaDon,new Date(),new Date());

        for (YeuCauChiTietRequest ycctr : yeuCauRequest.getListYeuCauChiTiet()) {

        }
//        addYeuCauChiTiet(yeuCauRequest,yeuCau);
        return new YeuCauResponse();
    }

    private List<YeuCauChiTiet> getListYeuCauChiTiet(List<YeuCauChiTietRequest> listYeuCauChiTietRequest) {
        List<YeuCauChiTiet> listYeuCauChiTiet = new ArrayList<>();
        for(YeuCauChiTietRequest request :listYeuCauChiTietRequest){

        }
        return listYeuCauChiTiet;
    }

    @Override
    public YeuCauResponse updateYeuCau(Long id, YeuCauRequest yeuCauRequest) {
        YeuCau yeuCau;
        if (id == null) {
            throw new NullException();
        } else {
            yeuCau = yeuCauRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND));
        }
        HoaDon hoaDon = new HoaDon();
        yeuCau = new YeuCau(yeuCauRequest, hoaDon,new Date(),new Date());
        yeuCau.setId(id);

        return new YeuCauResponse(yeuCauRepository.save(yeuCau));
    }

    @Override
    public YeuCauResponse findById(Long id) {
        return new YeuCauResponse(yeuCauRepository.findById(id).orElseThrow(() -> new NotFoundException(ErrorMessage.NOT_FOUND)));
    }

    @Override
    public YeuCauResponse findByStatus() {
        return new YeuCauResponse(yeuCauRepository.findResponseByStatus());
    }

    @Override
    public Page<YeuCauResponse> getPage(Integer page,String searchText ,Date ngayBatDau, Date ngayKetThuc, Integer loaiYeuCau, Integer trangThai) {
        return yeuCauRepository.getPageResponse(ngayBatDau,searchText,ngayKetThuc,loaiYeuCau,trangThai, PageRequest.of((page - 1), 5));
    }


    public Date fomatDate(Date date, String string){

        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String date1 = sdf1.format(date);
        //2023-10-26 + 23:59:59
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            // Chuyển đổi chuỗi thành đối tượng Date
            return sdf.parse(date1 + string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}
