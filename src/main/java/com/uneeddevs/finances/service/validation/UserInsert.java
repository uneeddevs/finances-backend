package com.uneeddevs.finances.service.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserInsertValidator.class)
public @interface UserInsert {

    String message() default "Validation error";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
