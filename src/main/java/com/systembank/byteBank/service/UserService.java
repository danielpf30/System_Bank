package com.systembank.byteBank.service;

import com.systembank.byteBank.DTOs.UserRequestDTO;
import com.systembank.byteBank.DTOs.UserResponseDTO;
import com.systembank.byteBank.DTOs.UserUpdateDTO;
import com.systembank.byteBank.exception.BusinessRuleException;
import com.systembank.byteBank.models.User;
import com.systembank.byteBank.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import static com.systembank.byteBank.DTOs.UserResponseDTO.fromEntity;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO register(UserRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new BusinessRuleException("Este email ja existe");
        }
        if (userRepository.existsByCpf(dto.cpf())) {
            throw new BusinessRuleException("Este CPF ja existe");
        }
        User userSave = dto.toEntity();
        String hash = passwordEncoder.encode(dto.password());
        userSave.setPassword(hash);

        User savedUser = userRepository.save(userSave);
        return fromEntity(savedUser);
    }

    public UserResponseDTO update(UUID id, UserUpdateDTO dto) {
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
       User savedUser = userRepository.save(existingUser);
       return fromEntity(savedUser);

    }

    public void delete(UUID id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new BusinessRuleException("Usuario nao encontrado"));
        userRepository.delete(existingUser);
    }

    public List<UserResponseDTO> findAll() {
       List<User> list = userRepository.findAll();
       return list.stream().map(UserResponseDTO::fromEntity).toList();
    }

    public UserResponseDTO getUserFindByCpf(String cpf) {
        User user = userRepository.findByCpf(cpf).orElseThrow(() -> new BusinessRuleException("Usuario nao encontrado"));
        return UserResponseDTO.fromEntity(user);
    }
    public UserResponseDTO getUserFindById(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new BusinessRuleException("Usuario nao encontrado"));
        return UserResponseDTO.fromEntity(user);
    }
}
