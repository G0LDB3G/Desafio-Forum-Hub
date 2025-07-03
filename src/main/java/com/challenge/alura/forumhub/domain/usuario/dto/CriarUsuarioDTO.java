package com.challenge.alura.forumhub.domain.usuario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CriarUsuarioDTO(
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank String nome,
        @NotBlank String nickname,
        @NotBlank @Email String email
) {
}
