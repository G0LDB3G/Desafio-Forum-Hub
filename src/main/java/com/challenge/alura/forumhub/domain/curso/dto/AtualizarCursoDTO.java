package com.challenge.alura.forumhub.domain.curso.dto;

import com.challenge.alura.forumhub.domain.curso.Categoria;

public record AtualizarCursoDTO(
        String name,
        Categoria categoria,
        Boolean ativo) {
}
