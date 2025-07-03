package com.challenge.alura.forumhub.domain.topico.validacoes;

import com.challenge.alura.forumhub.domain.topico.dto.AtualizarTopicoDTO;

public interface ValidarTopicoAtualizado {
    void validar(AtualizarTopicoDTO data);
}
