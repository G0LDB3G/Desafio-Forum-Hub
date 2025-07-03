package com.challenge.alura.forumhub.domain.usuario.validacoes;

import com.challenge.alura.forumhub.domain.usuario.dto.AtualizarUsuarioDTO;
import com.challenge.alura.forumhub.domain.usuario.repository.UsuarioRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarAtualizacaoUsuario implements ValidarAtualizarUsuario {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public void validar(AtualizarUsuarioDTO data) {
        if (data.email() != null) {
            var emailDuplicado = repository.findByEmail(data.email());
            if (emailDuplicado != null) {
                throw new ValidationException("Este email já está sendo usado");
            }
        }
    }
}
