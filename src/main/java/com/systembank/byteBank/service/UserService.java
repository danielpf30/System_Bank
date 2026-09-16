package com.systembank.byteBank.service;

import com.systembank.byteBank.DTOs.UserRequestDTO;
import com.systembank.byteBank.DTOs.UserUpdateDTO;
import com.systembank.byteBank.exception.BusinessRuleException;
import com.systembank.byteBank.models.User;
import com.systembank.byteBank.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(UserRequestDTO dto) {
       userRepository.save(dto.toEntity());
    }

    public User update(User user) {
        if (userRepository.findById(user.getId()).isPresent()) {
            return userRepository.save(user);
        }
        throw new BusinessRuleException("Usuario nao encontrado");
    }

    public User updatePartial(UUID id, UserUpdateDTO dto) {
       User existingUser = userRepository.findById(id)
               .orElseThrow(() -> new BusinessRuleException("Usuario nao encontrado"));

       if (dto.name() != null) {
           existingUser.setName(dto.name());
       }
       if (dto.email() != null && !dto.email().equals(existingUser.getEmail())) {
           if (userRepository.existsByEmail(dto.email())) {
               throw new BusinessRuleException("Este email ja esta em uso");
           }
           existingUser.setEmail(dto.email());
       }
       return userRepository.save(existingUser);

    }
    public Optional<User> getUserFindByCpf(String cpf) {
        return userRepository.findByCpf(cpf);
    }
}
