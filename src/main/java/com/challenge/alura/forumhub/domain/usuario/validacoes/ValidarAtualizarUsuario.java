package com.challenge.alura.forumhub.domain.usuario.validacoes;

import com.challenge.alura.forumhub.domain.usuario.dto.AtualizarUsuarioDTO;

public interface ValidarAtualizarUsuario {
    void validar(AtualizarUsuarioDTO data);
}
