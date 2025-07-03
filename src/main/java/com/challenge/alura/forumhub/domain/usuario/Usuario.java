package com.challenge.alura.forumhub.domain.usuario;

import com.challenge.alura.forumhub.domain.usuario.dto.AtualizarUsuarioDTO;
import com.challenge.alura.forumhub.domain.usuario.dto.CriarUsuarioDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "usuarios")
@Entity(name = "Usuario")
@EqualsAndHashCode(of = "id")
public class Usuario implements UserDetails {

    private static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String nome;
    private String nickname;
    private String email;
    private Boolean habilitado;

    private LocalDateTime ultimaAtualizacao;

    public Usuario(CriarUsuarioDTO criarUsuarioDTO, String hashedPassword) {
        this.username = criarUsuarioDTO.username();
        this.password = hashedPassword;
        this.role = Role.USUARIO;
        this.nome = capitalizado(criarUsuarioDTO.nome());
        this.nickname = capitalizado(criarUsuarioDTO.nickname());
        this.email = criarUsuarioDTO.email();
        this.habilitado = true;
    }

    public void atualizarUsuario(AtualizarUsuarioDTO atualizarUsuarioDTO) {
        if (atualizarUsuarioDTO.papel() != null) {
            this.role = atualizarUsuarioDTO.papel();
        }
        if (atualizarUsuarioDTO.nome() != null) {
            this.nome = capitalizado(atualizarUsuarioDTO.nome());
        }
        if (atualizarUsuarioDTO.nickname() != null) {
            this.nickname = capitalizado(atualizarUsuarioDTO.nickname());
        }
        if (atualizarUsuarioDTO.email() != null) {
            this.email = atualizarUsuarioDTO.email();
        }
        if (atualizarUsuarioDTO.habilitado() != null) {
            this.habilitado = atualizarUsuarioDTO.habilitado();
        }
    }

    public void atualizarUsuarioComPassword(AtualizarUsuarioDTO atualizarUsuarioDTO, String hashedPassword) {
        atualizarUsuario(atualizarUsuarioDTO);
        this.password = hashedPassword;
    }

    public void eliminarUsuario() {
        this.habilitado = false;
    }

    private String capitalizado(String string) {
        return string.substring(0, 1).toUpperCase() + string.substring(1);
    }

    @PrePersist
    @PreUpdate
    public void atualizarTimestamp() {
        this.ultimaAtualizacao = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return habilitado;
    }
}
