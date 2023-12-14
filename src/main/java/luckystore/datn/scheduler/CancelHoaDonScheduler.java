package luckystore.datn.scheduler;

import luckystore.datn.entity.BienTheGiay;
import luckystore.datn.entity.HoaDonChiTiet;
import luckystore.datn.infrastructure.constraints.TrangThaiHoaDon;
import luckystore.datn.repository.BienTheGiayRepository;
import luckystore.datn.repository.HoaDonChiTietRepository;
import luckystore.datn.repository.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class CancelHoaDonScheduler {

    @Autowired
    private HoaDonRepository hoaDonRepository;

    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    private BienTheGiayRepository bienTheGiayRepository;

    @Scheduled(fixedRate = 600000)
    public void scheduledTask() {
        LocalDateTime now = LocalDateTime.now();

        List<Long> hoaDonIdList = hoaDonRepository.getHoadonChuaHoanThanh(now);
        for (Long id : hoaDonIdList) {
            hoaDonRepository.cancelHoaDon(id, TrangThaiHoaDon.DA_HUY, "Hóa đơn bị huỷ do khách hàng chưa hoàn tất thanh toán");

            List<HoaDonChiTiet> lstHoaDonCT = hoaDonChiTietRepository.findAllByIdHoaDon(id);

            List<BienTheGiay> lstBienThe = new ArrayList<>();
            lstHoaDonCT.forEach(hdct -> {
                BienTheGiay bienThe = new BienTheGiay();
                bienThe.setId(hdct.getBienTheGiay().getId());
                bienThe.setSoLuong(hdct.getSoLuong());
                lstBienThe.add(bienThe);
            });

            List<Long> idsBienThe = lstBienThe.stream().map(BienTheGiay::getId).toList();
            List<BienTheGiay> bienTheGiays = bienTheGiayRepository.getAllByIds(idsBienThe);
            lstBienThe.forEach(bienTheHuy -> {
                bienTheGiays.forEach(bienThe -> {
                    if (Objects.equals(bienTheHuy.getId(), bienThe.getId())) {
                        bienThe.setSoLuong(bienThe.getSoLuong() + bienTheHuy.getSoLuong());
                    }
                });
            });

            bienTheGiayRepository.saveAll(bienTheGiays);
            hoaDonRepository.cancelHoaDon(id, TrangThaiHoaDon.DA_HUY, "Hóa đơn bị huỷ do khách hàng chưa hoàn tất thanh toán");
        }
    }
}

