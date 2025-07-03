package com.challenge.alura.forumhub.domain.usuario.dto;

import com.challenge.alura.forumhub.domain.usuario.Role;

public record AtualizarUsuarioDTO(
        String password,
        Role papel,
        String nome,
        String nickname,
        String email,
        Boolean habilitado
) {
}
