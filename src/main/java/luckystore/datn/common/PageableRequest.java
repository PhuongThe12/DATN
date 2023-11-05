package luckystore.datn.common;

import lombok.Getter;
import luckystore.datn.infrastructure.constant.PaginationConstant;
import org.springframework.web.bind.annotation.GetMapping;

@Getter
public class PageableRequest {

    private int page = PaginationConstant.DEFAULT_PAGE;

    private int size = PaginationConstant.DEFAULT_SIZE;
}
