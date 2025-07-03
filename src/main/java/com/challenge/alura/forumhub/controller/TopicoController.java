package com.challenge.alura.forumhub.controller;

import com.challenge.alura.forumhub.domain.curso.Curso;
import com.challenge.alura.forumhub.domain.curso.repository.CursoRepository;
import com.challenge.alura.forumhub.domain.resposta.Resposta;
import com.challenge.alura.forumhub.domain.resposta.dto.DetalharRespostaDTO;
import com.challenge.alura.forumhub.domain.resposta.repository.RespostaRepository;
import com.challenge.alura.forumhub.domain.topico.Estado;
import com.challenge.alura.forumhub.domain.topico.Topico;
import com.challenge.alura.forumhub.domain.topico.dto.AtualizarTopicoDTO;
import com.challenge.alura.forumhub.domain.topico.dto.CriarTopicoDTO;
import com.challenge.alura.forumhub.domain.topico.dto.DetalharTopicoDTO;
import com.challenge.alura.forumhub.domain.topico.repository.TopicoRepository;
import com.challenge.alura.forumhub.domain.topico.validacoes.ValidarTopicoAtualizado;
import com.challenge.alura.forumhub.domain.topico.validacoes.ValidarTopicoCriado;
import com.challenge.alura.forumhub.domain.usuario.Usuario;
import com.challenge.alura.forumhub.domain.usuario.repository.UsuarioRepository;
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

import java.util.List;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    List<ValidarTopicoCriado> criarValidadores;

    @Autowired
    List<ValidarTopicoAtualizado> atualizarValidadores;

    @PostMapping
    @Transactional
    @Operation(summary = "Registra um novo tópico na base de dados")
    public ResponseEntity<DetalharTopicoDTO> criarTopico(
            @RequestBody @Valid CriarTopicoDTO criarTopicoDTO,
            UriComponentsBuilder uriBuilder) {

        criarValidadores.forEach(v -> v.validar(criarTopicoDTO));

        Usuario usuario = usuarioRepository.findById(criarTopicoDTO.usuarioId()).get();
        Curso curso = cursoRepository.findById(criarTopicoDTO.cursoId()).get();
        Topico topico = new Topico(criarTopicoDTO, usuario, curso);

        topicoRepository.save(topico);

        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetalharTopicoDTO(topico));
    }

    @GetMapping("/all")
    @Operation(summary = "Lê todos os temas independente do seu estado")
    public ResponseEntity<Page<DetalharTopicoDTO>> lerTodosOsTopicos(@PageableDefault(size = 5, sort = {"ultimaAtualizacao"}) Pageable pageable) {
        var pagina = topicoRepository.findAll(pageable).map(DetalharTopicoDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping
    @Operation(summary = "Lista de temas abertos e errados")
    public ResponseEntity<Page<DetalharTopicoDTO>> lerTopicossNaoEliminados(@PageableDefault(size = 5, sort = {"ultimaAtualizacao"}) Pageable pageable) {
        var pagina = topicoRepository.findAllByEstadoIsNot(Estado.DELETED, pageable).map(DetalharTopicoDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Lê um único tema pelo ID")
    public ResponseEntity<DetalharTopicoDTO> lerUmTopico(@PathVariable Long id) {
        Topico topico = topicoRepository.getReferenceById(id);
        var dadosTopico = new DetalharTopicoDTO(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getDataCriacao(),
                topico.getUltimaAtualizacao(),
                topico.getEstado(),
                topico.getUsuario().getUsername(),
                topico.getCurso().getName(),
                topico.getCurso().getCategoria()
        );
        return ResponseEntity.ok(dadosTopico);
    }

    @GetMapping("/{id}/solucao")
    @Operation(summary = "Lê a resposta do tópico marcada com sua solução")
    public ResponseEntity<DetalharRespostaDTO> lerSolucaoTopico(@PathVariable Long id) {
        Resposta resposta = respostaRepository.getReferenceByTopicoId(id);

        var dadosResposta = new DetalharRespostaDTO(
                resposta.getId(),
                resposta.getMensagem(),
                resposta.getDataCriacao(),
                resposta.getUltimaAtualizacao(),
                resposta.getResolvida(),
                resposta.getExcluida(),
                resposta.getUsuario().getId(),
                resposta.getUsuario().getUsername(),
                resposta.getTopico().getId(),
                resposta.getTopico().getTitulo()
        );
        return ResponseEntity.ok(dadosResposta);
    }

    @PutMapping("/{id}")
    @Transactional
    @Operation(summary = "Atualiza o título, a mensagem, o estado, ou o ID do curso de um tema")
    public ResponseEntity<DetalharTopicoDTO> atualizarTopico(@RequestBody @Valid AtualizarTopicoDTO atualizarTopicoDTO,
                                                            @PathVariable Long id) {
        atualizarValidadores.forEach(v -> v.validar(atualizarTopicoDTO));

        Topico topico = topicoRepository.getReferenceById(id);

        if (atualizarTopicoDTO.cursoId() != null) {
            Curso curso = cursoRepository.getReferenceById(atualizarTopicoDTO.cursoId());
            topico.atualizarTopicoComConcurso(atualizarTopicoDTO, curso);
        } else {
            topico.atualizarTopico(atualizarTopicoDTO);
        }

        var dadosTopico = new DetalharTopicoDTO(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getDataCriacao(),
                topico.getUltimaAtualizacao(),
                topico.getEstado(),
                topico.getUsuario().getUsername(),
                topico.getCurso().getName(),
                topico.getCurso().getCategoria()
        );
        return ResponseEntity.ok(dadosTopico);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Exclui um topico")
    public ResponseEntity<?> eliminarTopico(@PathVariable Long id) {
        Topico topico = topicoRepository.getReferenceById(id);
        topico.eliminarTopico();
        return ResponseEntity.noContent().build();
    }
}
