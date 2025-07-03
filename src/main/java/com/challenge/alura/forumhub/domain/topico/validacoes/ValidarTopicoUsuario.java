package com.challenge.alura.forumhub.domain.topico.validacoes;

import com.challenge.alura.forumhub.domain.topico.dto.CriarTopicoDTO;
import com.challenge.alura.forumhub.domain.usuario.repository.UsuarioRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarTopicoUsuario implements ValidarTopicoCriado {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public void validar(CriarTopicoDTO data) {
        var existeUsuario = repository.existsById(data.usuarioId());
        if (!existeUsuario) {
            throw new ValidationException("Este usuario não existe");
        }

        var usuarioHabilitado = repository.findById(data.usuarioId()).get().getHabilitado();
        if (!usuarioHabilitado) {
            throw new ValidationException("Este usuario foi desativado.");
        }
    }
}
