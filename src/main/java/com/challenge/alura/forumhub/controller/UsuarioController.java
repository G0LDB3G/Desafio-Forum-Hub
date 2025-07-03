package com.challenge.alura.forumhub.controller;

import com.challenge.alura.forumhub.domain.usuario.Usuario;
import com.challenge.alura.forumhub.domain.usuario.dto.AtualizarUsuarioDTO;
import com.challenge.alura.forumhub.domain.usuario.dto.CriarUsuarioDTO;
import com.challenge.alura.forumhub.domain.usuario.dto.DetalharUsuarioDTO;
import com.challenge.alura.forumhub.domain.usuario.repository.UsuarioRepository;
import com.challenge.alura.forumhub.domain.usuario.validacoes.ValidarAtualizarUsuario;
import com.challenge.alura.forumhub.domain.usuario.validacoes.ValidarCriarUsuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    List<ValidarCriarUsuario> criarValidador;

    @Autowired
    List<ValidarAtualizarUsuario> atualizarValidador;

    @PostMapping
    @Transactional
    @Operation(summary = "Registra um novo usuario na BD")
    public ResponseEntity<DetalharUsuarioDTO> criarUsuario(@RequestBody @Valid CriarUsuarioDTO criarUsuarioDTO,
                                                           UriComponentsBuilder uriBuilder) {
        criarValidador.forEach(v -> v.validar(criarUsuarioDTO));

        String hashedPassword = passwordEncoder.encode(criarUsuarioDTO.password());
        Usuario usuario = new Usuario(criarUsuarioDTO, hashedPassword);

        repository.save(usuario);
        var uri = uriBuilder.path("/usuarios/{username}").buildAndExpand(usuario.getUsername()).toUri();
        return ResponseEntity.created(uri).body(new DetalharUsuarioDTO(usuario));
    }

    @GetMapping("/all")
    @Operation(summary = "Enumera todos os usuarios independente do seu estado")
    public ResponseEntity<Page<DetalharUsuarioDTO>> lerTodosUsuarios(@PageableDefault(size = 5, sort = {"ultimaAtualizacao"})
                                                                     Pageable pageable) {
        var pagina = repository.findAll(pageable).map(DetalharUsuarioDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping
    @Operation(summary = "Lista apenas dos usuarios habilitados")
    public ResponseEntity<Page<DetalharUsuarioDTO>> lerUsuariosAtivos(@PageableDefault(size = 5, sort = {"ultimaAtualizacao"})
                                                                          Pageable pageable) {
        var pagina = repository.findAllByHabilitadoTrue(pageable).map(DetalharUsuarioDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/username/{username}")
    @Operation(summary = "Ler um único usuario com seu nome de usuario")
    public ResponseEntity<DetalharUsuarioDTO> lerUmUsuario(@PathVariable String username){
        Usuario usuario = (Usuario) repository.findByUsername(username);
        var dadosUsuario = new DetalharUsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRole(),
                usuario.getNome(),
                usuario.getNickname(),
                usuario.getEmail(),
                usuario.getHabilitado()
        );
        return ResponseEntity.ok(dadosUsuario);
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Ler um único usuario por seu ID")
    public ResponseEntity<DetalharUsuarioDTO> lerUmUsuarioPorId(@PathVariable Long id){
        Usuario usuario = repository.getReferenceById(id);
        var dadosUsuario = new DetalharUsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRole(),
                usuario.getNome(),
                usuario.getNickname(),
                usuario.getEmail(),
                usuario.getHabilitado()
        );
        return ResponseEntity.ok(dadosUsuario);
    }

    @PutMapping("/{username}")
    @Transactional
    @Operation(summary = "Atualiza a senha, papel, nome e nickname do usuario")
    public ResponseEntity<DetalharUsuarioDTO> atualizarUsuario(@RequestBody @Valid AtualizarUsuarioDTO atualizarUsuarioDTO,
                                                               @PathVariable String username) {
        atualizarValidador.forEach(v -> v.validar(atualizarUsuarioDTO));

        Usuario usuario = (Usuario) repository.findByUsername(username);

        if (atualizarUsuarioDTO.password() != null) {
            String hashedPassword = passwordEncoder.encode(atualizarUsuarioDTO.password());
            usuario.atualizarUsuarioComPassword(atualizarUsuarioDTO, hashedPassword);

        } else {
            usuario.atualizarUsuario(atualizarUsuarioDTO);
        }

        var dadosUsuario = new DetalharUsuarioDTO(
                usuario.getId(),
                usuario.getUsername(),
                usuario.getRole(),
                usuario.getNome(),
                usuario.getNickname(),
                usuario.getEmail(),
                usuario.getHabilitado()
        );
        return ResponseEntity.ok(dadosUsuario);
    }

    @DeleteMapping("/{username}")
    @Transactional
    @Operation(summary = "Desativa um usuario")
    public ResponseEntity<?> eliminarUsuario(@PathVariable String username) {
        Usuario usuario = (Usuario) repository.findByUsername(username);
        usuario.eliminarUsuario();
        return ResponseEntity.noContent().build();
    }
}
