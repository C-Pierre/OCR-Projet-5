package com.openclassrooms.mddapi.infrastructure.auth.validator;

import java.lang.annotation.*;
import jakarta.validation.Payload;
import jakarta.validation.Constraint;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AuthPasswordValidator.class)
@Documented
public @interface AuthValidPassword {

    String message() default "Invalid password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
