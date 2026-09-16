package com.systembank.byteBank.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

public class LegalAgeValidation implements ConstraintValidator<LegalAge, LocalDate> {

    @Override
    public boolean isValid(LocalDate dataNascimento, ConstraintValidatorContext context) {
        // Se a data for nula, o @NotNull trata. Evita NullPointerException aqui.
        if (dataNascimento == null) {
            return true;
        }

        // Calcula o período entre a data de nascimento e o dia de hoje
        LocalDate hoje = LocalDate.now();
        int idade = Period.between(dataNascimento, hoje).getYears();

        // Retorna true se tiver 18 anos ou mais
        return idade >= 18;
    }
}
