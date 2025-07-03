package com.challenge.alura.forumhub.domain.resposta.validacoes;

import com.challenge.alura.forumhub.domain.resposta.dto.AtualizarRespostaDTO;

public interface ValidarRespostaAtualizada {

    void validar(AtualizarRespostaDTO data, Long respostaId);
}
