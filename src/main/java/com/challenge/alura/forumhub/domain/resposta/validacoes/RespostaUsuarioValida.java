package com.challenge.alura.forumhub.domain.resposta.validacoes;

import com.challenge.alura.forumhub.domain.resposta.dto.CriarRespostaDTO;
import com.challenge.alura.forumhub.domain.usuario.repository.UsuarioRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RespostaUsuarioValida implements ValidarRespostaCriada {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public void validar(CriarRespostaDTO data) {
        var usuarioExiste = repository.existsById(data.usuarioId());

        if (!usuarioExiste) {
            throw new ValidationException("Este usuário não existe.");
        }

        var usuario = repository.findById(data.usuarioId())
                .orElseThrow(() -> new ValidationException("Usuário não encontrado."));

        if (!usuario.getHabilitado()) {
            throw new ValidationException("Este usuário não está habilitado a responder.");
        }
    }
}

