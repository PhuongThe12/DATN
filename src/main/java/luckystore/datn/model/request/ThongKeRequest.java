package luckystore.datn.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ThongKeRequest {

    private Integer currentPage = 1;

    //Top X
    private Integer pageSize = 5;

    //Top Y
    private Integer lastDate = 30;


}
