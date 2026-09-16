package com.systembank.byteBank.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateDTO(
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres.")
        String name,
        @Email(message = "Formato de email invalido")
        String email
) {
}
