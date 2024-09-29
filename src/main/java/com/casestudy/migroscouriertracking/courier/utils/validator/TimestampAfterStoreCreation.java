package com.casestudy.migroscouriertracking.courier.utils.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimestampAfterStoreCreationValidator.class)
@Documented
public @interface TimestampAfterStoreCreation {
    String message() default "Timestamp must be after the store creation time";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
