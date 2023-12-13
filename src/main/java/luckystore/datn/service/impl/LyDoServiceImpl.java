package luckystore.datn.service.impl;

import luckystore.datn.entity.LyDo;
import luckystore.datn.exception.DuplicateException;
import luckystore.datn.model.request.LyDoRequest;
import luckystore.datn.model.request.MauSacRequest;
import luckystore.datn.model.request.ThongKeRequest;
import luckystore.datn.model.response.LyDoResponse;
import luckystore.datn.repository.LyDoRepository;
import luckystore.datn.service.LyDoService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LyDoServiceImpl implements LyDoService {

    private final LyDoRepository lyDoRepository;

    @Autowired
    public LyDoServiceImpl(LyDoRepository lyDoRepository) {
        this.lyDoRepository = lyDoRepository;
    }


    @Override
    public List<LyDoResponse> getAll() {
        return lyDoRepository.findAllActive();
    }

    @Override
    public boolean insert(LyDoRequest lyDoRequest) {
        if (checkWhenInsert(lyDoRequest)==true) {
            lyDoRepository.save(new LyDo(lyDoRequest));
            return checkWhenInsert(lyDoRequest);
        }else {
            return checkWhenInsert(lyDoRequest);
        }
    }
    @Override
    public boolean update(LyDoRequest lyDoRequest) {
        if (checkWhenInsert(lyDoRequest)==true) {
            lyDoRepository.save(new LyDo(lyDoRequest));
            return checkWhenInsert(lyDoRequest);
        }else {
            return checkWhenInsert(lyDoRequest);
        }
    }

    @Override
    public Page<LyDoResponse> findReasonsForReturn(ThongKeRequest thongKeRequest) {
        Pageable pageable = PageRequest.of(thongKeRequest.getCurrentPage() - 1, thongKeRequest.getPageSize());
        Page<LyDoResponse> page = lyDoRepository.findReasonsForReturn(pageable);
        Long totalRequests = lyDoRepository.getTotalRequestCount();

        List<LyDoResponse> updatedResponses = page.getContent().stream().map(lyDoResponse -> {
            long soLuongYeuCauTra = lyDoResponse.getSoLuongThongKe();
            double tyLe = totalRequests != 0 ? ((double) soLuongYeuCauTra / totalRequests) * 100 : 0;
            lyDoResponse.setSoLuongYeuCauTra(totalRequests);
            lyDoResponse.setTyLe(Math.round(tyLe)); // Làm tròn tỷ lệ
            return lyDoResponse;
        }).collect(Collectors.toList());

        Page<LyDoResponse> updatedPage = new PageImpl<>(updatedResponses, pageable, page.getTotalElements());
        return updatedPage;
    }


    private boolean checkWhenInsert(LyDoRequest lyDoRequest) {
        boolean check =false;
        String error = "";
        if (lyDoRepository.existsByTen(lyDoRequest.getTen())) {
            error += JsonString.errorToJsonObject("ten", "Lý Do Đã Tồn Tại");
            check = true;
        }
        if(!error.isBlank()) {
            throw new DuplicateException(JsonString.stringToJson(error));
        }
        return check;
    }
}
