package com.challenge.alura.forumhub.domain.topico.validacoes;

import com.challenge.alura.forumhub.domain.topico.dto.CriarTopicoDTO;
import com.challenge.alura.forumhub.domain.topico.repository.TopicoRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TopicoDuplicado implements ValidarTopicoCriado{

    @Autowired
    private TopicoRepository topicoRepository;

    @Override
    public void validar(CriarTopicoDTO data) {
        var topicoDuplicado = topicoRepository.existsByTituloAndMensagem(data.titulo(), data.mensagem());
        if (topicoDuplicado) {
            throw new ValidationException("Este topico já existe." + topicoRepository.findByTitulo(data.titulo()).getId());
        }

    }
}
