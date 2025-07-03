package com.challenge.alura.forumhub.domain.resposta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CriarRespostaDTO(
        @NotBlank String mensagem,
        @NotNull Long usuarioId,
        @NotNull Long topicoId
) {
}
