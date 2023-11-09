package luckystore.datn.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import luckystore.datn.validator.PriceCheckValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = PriceCheckValidator.class)
public @interface PriceCheck {
    String message() default "Giá bán phải lớn hơn giá nhập";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
