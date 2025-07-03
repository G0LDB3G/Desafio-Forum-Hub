package com.challenge.alura.forumhub.domain.usuario.validacoes;

import com.challenge.alura.forumhub.domain.usuario.dto.CriarUsuarioDTO;
import com.challenge.alura.forumhub.domain.usuario.repository.UsuarioRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioDuplicado implements ValidarCriarUsuario {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public void validar(CriarUsuarioDTO data) {
        var usuarioDuplicado = repository.findByUsername(data.username());
        if (usuarioDuplicado != null) {
            throw  new ValidationException("Este usuario já existe");
        }

        var emailDuplicado = repository.findByEmail(data.email());
        if (emailDuplicado != null) {
            throw  new ValidationException("Este email já está sendo usado");
        }
    }
}
