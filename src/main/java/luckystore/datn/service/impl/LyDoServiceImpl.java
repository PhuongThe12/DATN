package luckystore.datn.service.impl;

import luckystore.datn.entity.LyDo;
import luckystore.datn.exception.DuplicateException;
import luckystore.datn.model.request.LyDoRequest;
import luckystore.datn.model.request.MauSacRequest;
import luckystore.datn.model.response.LyDoResponse;
import luckystore.datn.repository.LyDoRepository;
import luckystore.datn.service.LyDoService;
import luckystore.datn.util.JsonString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public LyDoResponse insert(LyDoRequest lyDoRequest) {
        checkWhenInsert(lyDoRequest);
        LyDo lyDo = new LyDo(lyDoRequest);
        return new LyDoResponse(lyDoRepository.save(new LyDo(lyDoRequest)));
    }
    @Override
    public LyDoResponse update(LyDoRequest lyDoRequest) {
        return new LyDoResponse(lyDoRepository.save(new LyDo(lyDoRequest)));
    }

    private void checkWhenInsert(LyDoRequest lyDoRequest) {
        String error = "";
        if (lyDoRepository.existsByTen(lyDoRequest.getTen())) {
            error += JsonString.errorToJsonObject("ten", "Lý Do Đã Tồn Tại");
        }
        if(!error.isBlank()) {
            throw new DuplicateException(JsonString.stringToJson(error));
        }
    }
}
