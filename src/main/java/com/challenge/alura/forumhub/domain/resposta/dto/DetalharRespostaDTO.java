package com.challenge.alura.forumhub.domain.resposta.dto;

import com.challenge.alura.forumhub.domain.resposta.Resposta;

import java.time.LocalDateTime;

public record DetalharRespostaDTO(
        Long id,
        String mensagem,
        LocalDateTime dataCriacao,
        LocalDateTime ultimaAtualizacao,
        Boolean resolvida,
        Boolean excluida,
        Long usuarioId,
        String username,
        Long topicoId,
        String topico
) {
    public DetalharRespostaDTO(Resposta resposta) {
        this(
                resposta.getId(),
                resposta.getMensagem(),
                resposta.getDataCriacao(),
                resposta.getUltimaAtualizacao(),
                resposta.getResolvida(),
                resposta.getExcluida(),
                resposta.getUsuario().getId(),
                resposta.getUsuario().getUsername(),
                resposta.getTopico().getId(),
                resposta.getTopico().getTitulo());
    }
}
