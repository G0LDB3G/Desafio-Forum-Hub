package com.challenge.alura.forumhub.domain.resposta.validacoes;

import com.challenge.alura.forumhub.domain.resposta.dto.CriarRespostaDTO;

public interface ValidarRespostaCriada {
    void validar(CriarRespostaDTO data);
}
