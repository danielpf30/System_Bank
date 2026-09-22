package com.systembank.byteBank;

import com.systembank.byteBank.DTOs.UserRequestDTO;
import com.systembank.byteBank.DTOs.UserResponseDTO;
import com.systembank.byteBank.DTOs.UserUpdateDTO;
import com.systembank.byteBank.exception.BusinessRuleException;
import com.systembank.byteBank.models.User;
import com.systembank.byteBank.repository.UserRepository;
import com.systembank.byteBank.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void deveLancarExcecaoQuandoEmailJaExistir(){
        UserRequestDTO dto = new UserRequestDTO("Daniel", "12345678911","daniel@gmail.com", "daniel10", LocalDate.of(2005,10,2));

        when(userRepository.existsByEmail("daniel@gmail.com")).thenReturn(true);

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
                userService.register(dto);
        });

        assertEquals("Este email ja existe", exception.getMessage());

    }

    @Test
    public void deveLancarExcecaoQuandoCpfJaExistir(){
        UserRequestDTO dto = new UserRequestDTO("daniel", "12345678911", "daniel@gmail.com","daniel10",LocalDate.of(2005,10,2));

        when(userRepository.existsByCpf(dto.cpf())).thenReturn(true);

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            userService.register(dto);
        });

        assertEquals("Este CPF ja existe", exception.getMessage());
    }

    @Test
    public void deveRegistrarUsuarioComSucesso(){
        UserRequestDTO dto = new UserRequestDTO("Daniel", "12345678911", "daniel@gmail.com", "messi10",LocalDate.of(2005,10,2));

        when(userRepository.existsByCpf(dto.cpf())).thenReturn(false);
        when(userRepository.existsByEmail(dto.email())).thenReturn(false);

        when(passwordEncoder.encode(dto.password())).thenReturn("hash-do-123");

        User userSave = dto.toEntity();
        userSave.setId(UUID.randomUUID());
        when(userRepository.save(any(User.class))).thenReturn(userSave);

        UserResponseDTO userResponseDTO = userService.register(dto);

        assertNotNull(userResponseDTO);
        assertEquals("Daniel", userResponseDTO.name());

        verify(userRepository).save(any(User.class));

    }

    @Test
    public void deveAtualizarUsuarioComSucesso(){
        UUID id = UUID.randomUUID();
        User existingUser = new User();
        existingUser.setId(id);
        existingUser.setName("Daniel");
        existingUser.setCpf("12345678911");
        existingUser.setEmail("daniel@gmail.com");
        existingUser.setBirthDate(LocalDate.of(2005,10,2));

        UserUpdateDTO dto = new UserUpdateDTO("Daniel Alves", "daniel.alves@gmail.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        UserResponseDTO result = userService.update(id, dto);

        assertNotNull(result);
        assertEquals("Daniel Alves", result.name());
        assertEquals("daniel.alves@gmail.com", result.email());

        verify(userRepository).save(existingUser);
    }

    @Test
    public void deveLancarExcecaoQuandoUsuarioNaoExistirNoUpdate(){
        UUID id = UUID.randomUUID();
        UserUpdateDTO dto = new UserUpdateDTO("Daniel", "daniel@gmail.com");

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            userService.update(id, dto);
        });

        assertEquals("Usuario nao encontrado", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void deveLancarExcecaoQuandoEmailJaEstiverEmUsoNoUpdate(){
        UUID id = UUID.randomUUID();
        User existingUser = new User();
        existingUser.setId(id);
        existingUser.setName("Daniel");
        existingUser.setCpf("12345678911");
        existingUser.setEmail("daniel@gmail.com");
        existingUser.setBirthDate(LocalDate.of(2005,10,2));

        UserUpdateDTO dto = new UserUpdateDTO(null, "outro@gmail.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.existsByEmail("outro@gmail.com")).thenReturn(true);

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            userService.update(id, dto);
        });

        assertEquals("Este email ja esta em uso", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void deveDeletarUsuarioComSucesso(){
        UUID id = UUID.randomUUID();
        User existingUser = new User();
        existingUser.setId(id);

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));

        userService.delete(id);

        verify(userRepository).delete(existingUser);
    }

    @Test
    public void deveLancarExcecaoQuandoUsuarioNaoExistirNoDelete(){
        UUID id = UUID.randomUUID();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            userService.delete(id);
        });

        assertEquals("Usuario nao encontrado", exception.getMessage());
        verify(userRepository, never()).delete(any(User.class));
    }

    @Test
    public void deveRetornarTodosOsUsuarios(){
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setName("Daniel");
        user1.setCpf("12345678911");
        user1.setEmail("daniel@gmail.com");
        user1.setBirthDate(LocalDate.of(2005,10,2));

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setName("Maria");
        user2.setCpf("98765432100");
        user2.setEmail("maria@gmail.com");
        user2.setBirthDate(LocalDate.of(1990,5,15));

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<UserResponseDTO> result = userService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Daniel", result.get(0).name());
        assertEquals("Maria", result.get(1).name());
    }

    @Test
    public void deveRetornarListaVaziaQuandoNaoExistiremUsuarios(){
        when(userRepository.findAll()).thenReturn(List.of());

        List<UserResponseDTO> result = userService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void deveBuscarUsuarioPorCpfComSucesso(){
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setName("Daniel");
        user.setCpf("12345678911");
        user.setEmail("daniel@gmail.com");
        user.setBirthDate(LocalDate.of(2005,10,2));

        when(userRepository.findByCpf("12345678911")).thenReturn(Optional.of(user));

        UserResponseDTO result = userService.getUserFindByCpf("12345678911");

        assertNotNull(result);
        assertEquals("Daniel", result.name());
        assertEquals("12345678911", result.cpf());
    }

    @Test
    public void deveLancarExcecaoQuandoUsuarioNaoExistirPorCpf(){
        when(userRepository.findByCpf("12345678911")).thenReturn(Optional.empty());

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            userService.getUserFindByCpf("12345678911");
        });

        assertEquals("Usuario nao encontrado", exception.getMessage());
    }

    @Test
    public void deveBuscarUsuarioPorIdComSucesso(){
        UUID id = UUID.randomUUID();
        User user = new User();
        user.setId(id);
        user.setName("Daniel");
        user.setCpf("12345678911");
        user.setEmail("daniel@gmail.com");
        user.setBirthDate(LocalDate.of(2005,10,2));

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserResponseDTO result = userService.getUserFindById(id);

        assertNotNull(result);
        assertEquals("Daniel", result.name());
        assertEquals(id, result.id());
    }

    @Test
    public void deveLancarExcecaoQuandoUsuarioNaoExistirPorId(){
        UUID id = UUID.randomUUID();

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            userService.getUserFindById(id);
        });

        assertEquals("Usuario nao encontrado", exception.getMessage());
    }
}
