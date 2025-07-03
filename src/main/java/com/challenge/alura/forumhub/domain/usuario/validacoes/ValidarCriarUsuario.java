package com.challenge.alura.forumhub.domain.usuario.validacoes;

import com.challenge.alura.forumhub.domain.usuario.dto.CriarUsuarioDTO;

public interface ValidarCriarUsuario {
    void validar(CriarUsuarioDTO data);
}
