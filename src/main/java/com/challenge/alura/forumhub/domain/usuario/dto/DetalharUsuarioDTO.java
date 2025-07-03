package com.challenge.alura.forumhub.domain.usuario.dto;

import com.challenge.alura.forumhub.domain.usuario.Role;
import com.challenge.alura.forumhub.domain.usuario.Usuario;

public record DetalharUsuarioDTO(
        Long id,
        String username,
        Role papel,
        String nome,
        String nickname,
        String email,
        Boolean habilitado
) {
    public DetalharUsuarioDTO(Usuario usuario){
        this(usuario.getId(),
                usuario.getUsername(),
                usuario.getRole(),
                usuario.getNome(),
                usuario.getNickname(),
                usuario.getEmail(),
                usuario.getHabilitado());
    }
}

