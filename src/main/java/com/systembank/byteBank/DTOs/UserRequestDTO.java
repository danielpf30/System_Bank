package com.systembank.byteBank.DTOs;

import com.systembank.byteBank.models.User;
import com.systembank.byteBank.validation.LegalAge;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;
import java.time.LocalDate;

public record UserRequestDTO(
        @NotBlank(message = "O nome é obrigatorio")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
        String name,
        @NotBlank(message = "O CPF é obrigatorio")
        @CPF
        String cpf,
        @NotBlank(message = "O email é obrigatorio")
        @Email
        String email,
        @NotBlank(message = "A senha é obrigatoria")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String password,
        @LegalAge(message = "A data de nascimento é obrigatoria")
        LocalDate birthDate) {

    public User toEntity() {
        User user = new User();
        user.setName(name);
        user.setCpf(cpf);
        user.setEmail(email);
        user.setBirthDate(birthDate);
        return user;
    }
}
