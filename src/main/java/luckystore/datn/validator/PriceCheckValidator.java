package luckystore.datn.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import luckystore.datn.model.request.BienTheGiayRequest;
import luckystore.datn.validation.PriceCheck;

public class PriceCheckValidator implements ConstraintValidator<PriceCheck, BienTheGiayRequest> {
    @Override
    public boolean isValid(BienTheGiayRequest value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        return value.getGiaNhap().compareTo(value.getGiaBan()) < 0;
    }
}
