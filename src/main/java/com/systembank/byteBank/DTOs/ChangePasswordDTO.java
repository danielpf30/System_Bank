package com.systembank.byteBank.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordDTO (
        @NotBlank(message = "A senha atual é obrigatoria")
        String currentPassword,

        @NotBlank(message = "A nova senha é obrigatoria")
        @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
        String NewPassword
) {
}
