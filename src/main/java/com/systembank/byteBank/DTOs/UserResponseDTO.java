package com.systembank.byteBank.DTOs;

import com.systembank.byteBank.models.User;
import java.time.LocalDate;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        String cpf,
        String name,
        String email,
        LocalDate birthdate
) {

    public static UserResponseDTO fromEntity(User response){
        return new UserResponseDTO(
                response.getId(),
                response.getCpf(),
                response.getName(),
                response.getEmail(),
                response.getBirthDate()
        );
    }
}
