package luckystore.datn.model.request;

import lombok.Getter;
import lombok.Setter;
import luckystore.datn.infrastructure.constants.PaginationConstant;

@Getter
@Setter
public abstract class PageableRequest {

    private int currentPage = PaginationConstant.DEFAULT_PAGE;

    private int size = PaginationConstant.DEFAULT_SIZE;
}
