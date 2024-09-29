package com.casestudy.migroscouriertracking.courier.utils.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Annotation to validate that a timestamp is after the store creation time.
 * This can be used on fields or parameters to enforce timestamp constraints.
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimestampAfterStoreCreationValidator.class)
@Documented
public @interface TimestampAfterStoreCreation {
    String message() default "Timestamp must be after the store creation time";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
