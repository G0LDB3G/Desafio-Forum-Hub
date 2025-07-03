package com.challenge.alura.forumhub.domain.topico.validacoes;

import com.challenge.alura.forumhub.domain.curso.repository.CursoRepository;
import com.challenge.alura.forumhub.domain.topico.dto.AtualizarTopicoDTO;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidarCursoAtualizado implements ValidarTopicoAtualizado {

    @Autowired
    private CursoRepository repository;

    @Override
    public void validar(AtualizarTopicoDTO data) {
        var existeCurso = repository.existsById(data.cursoId());
        if (!existeCurso) {
            throw new ValidationException("Este curso não existe");
        }

        var cursoHabilitado = repository.findById(data.cursoId()).get().getAtivo();
        if (!cursoHabilitado) {
            throw new ValidationException("Este curso não está disponível no momento.");
        }
    }
}
