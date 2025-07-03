package com.challenge.alura.forumhub.controller;

import com.challenge.alura.forumhub.domain.resposta.Resposta;
import com.challenge.alura.forumhub.domain.resposta.dto.AtualizarRespostaDTO;
import com.challenge.alura.forumhub.domain.resposta.dto.CriarRespostaDTO;
import com.challenge.alura.forumhub.domain.resposta.dto.DetalharRespostaDTO;
import com.challenge.alura.forumhub.domain.resposta.repository.RespostaRepository;
import com.challenge.alura.forumhub.domain.resposta.validacoes.ValidarRespostaAtualizada;
import com.challenge.alura.forumhub.domain.resposta.validacoes.ValidarRespostaCriada;
import com.challenge.alura.forumhub.domain.topico.Estado;
import com.challenge.alura.forumhub.domain.topico.Topico;
import com.challenge.alura.forumhub.domain.topico.repository.TopicoRepository;
import com.challenge.alura.forumhub.domain.usuario.Usuario;
import com.challenge.alura.forumhub.domain.usuario.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/respostas")
@SecurityRequirement(name = "bearer-key")
public class RespostaController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RespostaRepository respostaRepository;

    @Autowired
    List<ValidarRespostaCriada> criarValidadores;

    @Autowired
    List<ValidarRespostaAtualizada> atualizarValiadores;

    @PostMapping
    @Transactional
    @Operation(summary = "Registra uma nova resposta na base de dados, vincula a um usuario")
    public ResponseEntity<DetalharRespostaDTO> criarResposta(@RequestBody @Valid CriarRespostaDTO criarRespostaDTO,
                                                             UriComponentsBuilder uriBuilder) {
        criarValidadores.forEach(v -> v.validar(criarRespostaDTO));

        Usuario usuario = usuarioRepository.getReferenceById(criarRespostaDTO.usuarioId());
        Topico topico = topicoRepository.findById(criarRespostaDTO.topicoId()).get();

        var resposta = new Resposta(criarRespostaDTO, usuario, topico);
        respostaRepository.save(resposta);

        var uri = uriBuilder.path("/respostas/{id}").buildAndExpand(resposta.getId()).toUri();
        return ResponseEntity.created(uri).body(new DetalharRespostaDTO(resposta));
    }

    @GetMapping("/topico/{topicoId}")
    @Operation(summary = "Lê todas as respostas do tema dado")
    public ResponseEntity<Page<DetalharRespostaDTO>>
    lerRespostaDoTopico(@PageableDefault(size = 5, sort = {"ultimaAtualizacao"},
            direction = Sort.Direction.ASC) Pageable pageable, @PathVariable Long topicoId) {
        var pagina = respostaRepository.findAllByTopicoId(topicoId, pageable)
                .map(DetalharRespostaDTO::new);
        return ResponseEntity.ok(pagina);
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(summary = "Lê todas as respostas do ID do usuário dado.")
    public ResponseEntity<Page<DetalharRespostaDTO>> lerRespostaDosUsuarios(
            @PageableDefault(size = 5, sort = {"ultimaAtualizacao"},
                    direction = Sort.Direction.ASC) Pageable pageable,
            @PathVariable Long usuarioId) {

        var pagina = respostaRepository.findAllByUsuarioId(usuarioId, pageable)
                .map(DetalharRespostaDTO::new);
        return ResponseEntity.ok(pagina);
    }


    @GetMapping("/{id}")
    @Operation(summary ="Lê uma única reposta pela ID")
    public ResponseEntity<DetalharRespostaDTO> lerUmaResposta(@PathVariable Long id) {
        Resposta resposta = respostaRepository.getReferenceById(id);

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
    @Operation(summary = "Atualiza a mensagem da resposta, a solucao ou o estado da resposta")
    public ResponseEntity<DetalharRespostaDTO> atualizarResposta(@RequestBody @Valid AtualizarRespostaDTO atualizarRespostaDTO,
                                                                 @PathVariable Long id) {
        atualizarValiadores.forEach(v -> v.validar(atualizarRespostaDTO, id));
        Resposta resposta = respostaRepository.getReferenceById(id);
        resposta.atualizarResposta(atualizarRespostaDTO);

        if (atualizarRespostaDTO.resolvida()) {
            var temaResultado = topicoRepository.getReferenceById(resposta.getTopico().getId());
            temaResultado.setEstado(Estado.CLOSED);
        }

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

    @DeleteMapping("/{id}")
    @Transactional
    @Operation(summary = "Exclui uma resposta pelo ID")
    public ResponseEntity<?> borrarResposta(@PathVariable Long id) {
        Resposta resposta = respostaRepository.getReferenceById(id);
        resposta.excluirResposta();
        return ResponseEntity.noContent().build();
    }
}
