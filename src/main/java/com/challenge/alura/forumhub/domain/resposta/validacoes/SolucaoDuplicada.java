package com.challenge.alura.forumhub.domain.resposta.validacoes;

import com.challenge.alura.forumhub.domain.resposta.Resposta;
import com.challenge.alura.forumhub.domain.resposta.dto.AtualizarRespostaDTO;
import com.challenge.alura.forumhub.domain.resposta.repository.RespostaRepository;
import com.challenge.alura.forumhub.domain.topico.Estado;
import com.challenge.alura.forumhub.domain.topico.repository.TopicoRepository;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SolucaoDuplicada implements ValidarRespostaAtualizada {

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Override
    public void validar(AtualizarRespostaDTO data, Long respostaId) {
        if (data.resolvida()) {
            Resposta resposta = respostaRepository.getReferenceById(respostaId);
            var topicoResolvido = topicoRepository.getReferenceById(resposta.getTopico().getId());

            if (topicoResolvido.getEstado() == Estado.CLOSED) {
                throw new ValidationException("Este tópico já foi resolvido.");
            }
        }
    }
}
