package luckystore.datn.exception;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@NotNull
@Data
@Builder
public class NotFoundException extends RuntimeException{
    private Object data;
}
