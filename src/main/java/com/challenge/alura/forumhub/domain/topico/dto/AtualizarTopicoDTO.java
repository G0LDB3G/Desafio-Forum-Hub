package com.challenge.alura.forumhub.domain.topico.dto;

import com.challenge.alura.forumhub.domain.topico.Estado;

public record AtualizarTopicoDTO(
        String titulo,
        String mensagem,
        Estado estado,
        Long cursoId
) {
}
