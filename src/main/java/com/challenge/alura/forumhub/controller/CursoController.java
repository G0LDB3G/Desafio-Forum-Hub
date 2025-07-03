package com.challenge.alura.forumhub.controller;

import com.challenge.alura.forumhub.domain.curso.Curso;
import com.challenge.alura.forumhub.domain.curso.dto.CriarCursoDTO;
import com.challenge.alura.forumhub.domain.curso.dto.DetalharCursoDTO;
import com.challenge.alura.forumhub.domain.curso.dto.AtualizarCursoDTO;
import com.challenge.alura.forumhub.domain.curso.repository.CursoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/cursos")
@SecurityRequirement(name = "bearer-key")
public class CursoController {

    @Autowired
    private CursoRepository repository;

    @PostMapping
    @Transactional
    @Operation(summary = "Registrar um novo curso no banco de dados.")
    public ResponseEntity<DetalharCursoDTO> criarCurso(@RequestBody @Valid CriarCursoDTO criarCursoDTO,
                                                       UriComponentsBuilder uriBuilder) {
        Curso curso = new Curso(criarCursoDTO);
        repository.save(curso);
        var uri = uriBuilder.path("/cursos/{id}").buildAndExpand(curso.getId()).toUri();

        return ResponseEntity.created(uri).body(new DetalharCursoDTO(curso));
    }

    @GetMapping("/all")
    @Operation(summary = "Leia todos os cursos (ativos ou não)")
    public ResponseEntity<Page<DetalharCursoDTO>> listarCursos(@PageableDefault(size = 5, sort = {"id"}) Pageable pageable) {
        var pagina = repository.findAll(pageable).map(DetalharCursoDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping
    @Operation(summary = "Lista de cursos ativos")
    public ResponseEntity<Page<DetalharCursoDTO>> listarCursosAtivos(@PageableDefault(size = 5, sort = {"id"}) Pageable pageable) {
        var pagina = repository.findAllByAtivoTrue(pageable).map(DetalharCursoDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Leia um curso pelo ID")
    public ResponseEntity<DetalharCursoDTO> listarUmCurso(@PathVariable Long id) {
        Curso curso = repository.getReferenceById(id);
        var dadosDoCurso = new DetalharCursoDTO(
                curso.getId(),
                curso.getName(),
                curso.getCategoria(),
                curso.getAtivo()
        );
        return ResponseEntity.ok(dadosDoCurso);
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Atualiza o nome, a categoria e/ou status de um curso")
    public ResponseEntity<DetalharCursoDTO> atualizarCurso(@PathVariable Long id,
                                                           @RequestBody @Valid AtualizarCursoDTO atualizarCursoDTO) {
        Curso curso = repository.getReferenceById(id);
        curso.atualizarCurso(atualizarCursoDTO);

        var dadosDoCurso = new DetalharCursoDTO(
                curso.getId(),
                curso.getName(),
                curso.getCategoria(),
                curso.getAtivo()
        );
        return ResponseEntity.ok(dadosDoCurso);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Exclui um curso")
    public ResponseEntity<?> excluirCurso(@PathVariable Long id) {
        Curso curso = repository.getReferenceById(id);
        curso.excluirCurso();
        return ResponseEntity.noContent().build();
    }

}
