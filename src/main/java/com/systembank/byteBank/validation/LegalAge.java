package com.systembank.byteBank.validation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = LegalAgeValidation.class) // Liga a anotação ao validador
@Documented
public @interface LegalAge {

    String message() default "O usuário deve ser maior de 18 anos para abrir uma conta.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
