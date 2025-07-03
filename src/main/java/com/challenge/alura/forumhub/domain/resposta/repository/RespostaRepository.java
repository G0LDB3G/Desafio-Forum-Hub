package com.challenge.alura.forumhub.domain.resposta.repository;

import com.challenge.alura.forumhub.domain.resposta.Resposta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RespostaRepository extends JpaRepository<Resposta, Long> {

    Page<Resposta> findAllByTopicoId(Long topicoId, Pageable pageable);

    Page<Resposta> findAllByUsuarioId(Long usuarioId, Pageable pageable);

    Resposta getReferenceByTopicoId(Long id);

    Resposta getReferenceById(Long id);
}
