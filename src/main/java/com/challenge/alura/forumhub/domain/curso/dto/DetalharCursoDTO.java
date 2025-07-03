package com.challenge.alura.forumhub.domain.curso.dto;

import com.challenge.alura.forumhub.domain.curso.Categoria;
import com.challenge.alura.forumhub.domain.curso.Curso;

public record DetalharCursoDTO(
        Long id,
        String Name,
        Categoria categoria,
        Boolean ativo) {

    public DetalharCursoDTO(Curso curso) {
        this(
                curso.getId(),
                curso.getName(),
                curso.getCategoria(),
                curso.getAtivo());
    }

}
