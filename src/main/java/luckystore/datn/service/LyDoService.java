package luckystore.datn.service;

import luckystore.datn.entity.LyDo;
import luckystore.datn.model.request.LyDoRequest;
import luckystore.datn.model.response.LyDoResponse;

import java.util.List;

public interface LyDoService {

    List<LyDoResponse> getAll();

    LyDoResponse insertOrUpate(LyDoRequest lyDoRequest);

}