package luckystore.datn.service;

import luckystore.datn.entity.LyDo;
import luckystore.datn.model.request.LyDoRequest;
import luckystore.datn.model.request.ThongKeRequest;
import luckystore.datn.model.response.LyDoResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LyDoService {

    List<LyDoResponse> getAll();

    boolean insert(LyDoRequest lyDoRequest);

    boolean update(LyDoRequest lyDoRequest);
   Page<LyDoResponse> findReasonsForReturn(ThongKeRequest thongKeRequest);

}
