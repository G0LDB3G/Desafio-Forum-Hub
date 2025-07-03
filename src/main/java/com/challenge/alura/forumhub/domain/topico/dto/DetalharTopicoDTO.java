package com.challenge.alura.forumhub.domain.topico.dto;

import com.challenge.alura.forumhub.domain.curso.Categoria;
import com.challenge.alura.forumhub.domain.topico.Estado;
import com.challenge.alura.forumhub.domain.topico.Topico;

import java.time.LocalDateTime;

public record DetalharTopicoDTO(
        Long id,
        String titulo,
        String mensagem,
        LocalDateTime dataCriacao,
        LocalDateTime ultimaAtualizacao,
        Estado estado,
        String usuario,
        String curso,
        Categoria categoriaCurso
) {

    public DetalharTopicoDTO(Topico topico) {
        this(topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getDataCriacao(),
                topico.getUltimaAtualizacao(),
                topico.getEstado(),
                topico.getUsuario().getUsername(),
                topico.getCurso().getName(),
                topico.getCurso().getCategoria()
        );
    }
}
