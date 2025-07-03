package com.challenge.alura.forumhub.domain.resposta.dto;

public record AtualizarRespostaDTO(
        String mensagem,
        Boolean resolvida,
        Boolean excluida
) {
}
