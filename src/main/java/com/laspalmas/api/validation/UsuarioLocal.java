package com.laspalmas.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsuarioLocalValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface UsuarioLocal {
    String message() default "Fecha de nacimiento y contraseña son requeridos para usuarios locales";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
