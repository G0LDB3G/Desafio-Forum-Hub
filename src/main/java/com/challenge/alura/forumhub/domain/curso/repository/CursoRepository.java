package com.challenge.alura.forumhub.domain.curso.repository;

import com.challenge.alura.forumhub.domain.curso.Curso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CursoRepository  extends JpaRepository<Curso, Long> {

    Page<Curso> findAllByAtivoTrue(Pageable pageable);
}
