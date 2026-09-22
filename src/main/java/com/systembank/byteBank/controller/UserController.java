package com.systembank.byteBank.controller;

import com.systembank.byteBank.DTOs.UserRequestDTO;
import com.systembank.byteBank.DTOs.UserResponseDTO;
import com.systembank.byteBank.DTOs.UserUpdateDTO;
import com.systembank.byteBank.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public void UserController(UserService userService) {
        this.userService = userService;
    }
    //rota de post/register
    @PostMapping
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO userResponseDTO = userService.register(userRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    //rota de patch/update
    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(
            @PathVariable UUID id,
            @Valid @RequestBody UserUpdateDTO userUpdateDTO) {
       return ResponseEntity.ok(userService.update(id, userUpdateDTO));
    }

    //rota de delete/delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id){
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //rotas de get  cpf,id e findAll
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getFindAll(){
        return ResponseEntity.ok(userService.findAll());
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<UserResponseDTO> getFindByCpf(@PathVariable String cpf){
        return ResponseEntity.ok(userService.getUserFindByCpf(cpf));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getFindById(@PathVariable UUID id){
        return ResponseEntity.ok(userService.getUserFindById(id));
    }
}
