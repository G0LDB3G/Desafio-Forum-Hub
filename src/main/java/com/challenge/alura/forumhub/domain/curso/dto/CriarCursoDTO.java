package com.challenge.alura.forumhub.domain.curso.dto;

import com.challenge.alura.forumhub.domain.curso.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarCursoDTO(
        @NotBlank String name,
        @NotNull Categoria categoria) {
}
