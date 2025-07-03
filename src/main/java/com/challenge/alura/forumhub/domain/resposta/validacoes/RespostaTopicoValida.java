package com.challenge.alura.forumhub.domain.resposta.validacoes;

import com.challenge.alura.forumhub.domain.resposta.dto.CriarRespostaDTO;
import com.challenge.alura.forumhub.domain.topico.Estado;
import com.challenge.alura.forumhub.domain.topico.repository.TopicoRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RespostaTopicoValida implements ValidarRespostaCriada {

    @Autowired
    private TopicoRepository repository;

    @Override
    public void validar(CriarRespostaDTO data) {
        boolean topicoExiste = repository.existsById(data.topicoId());
        if (!topicoExiste) {
            throw new ValidationException("Este tópico não existe.");
        }

        var topico = repository.findById(data.topicoId())
                .orElseThrow(() -> new ValidationException("Tópico não encontrado."));

        if (topico.getEstado() != Estado.OPEN) {
            throw new ValidationException("Este tópico está fechado para novas respostas.");
        }
    }
}

