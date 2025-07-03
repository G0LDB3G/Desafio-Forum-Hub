package com.challenge.alura.forumhub.domain.resposta;

import com.challenge.alura.forumhub.domain.resposta.dto.AtualizarRespostaDTO;
import com.challenge.alura.forumhub.domain.resposta.dto.CriarRespostaDTO;
import com.challenge.alura.forumhub.domain.topico.Topico;
import com.challenge.alura.forumhub.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "respostas")
@Entity(name = "Resposta")
@EqualsAndHashCode(of = "id")
public class Resposta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mensagem;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @Column(name = "ultima_atualizacao")
    private LocalDateTime ultimaAtualizacao;

    private Boolean resolvida;
    private Boolean excluida;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topico_id")
    private Topico topico;

    public Resposta(CriarRespostaDTO criarRespostaDTO, Usuario usuario, Topico topico) {
        this.mensagem = criarRespostaDTO.mensagem();
        this.dataCriacao = LocalDateTime.now();
        this.ultimaAtualizacao = LocalDateTime.now();
        this.resolvida = false;
        this.excluida = false;
        this.usuario = usuario;
        this.topico = topico;
    }

    public void atualizarResposta(AtualizarRespostaDTO atualizarRespostaDTO) {
        if (atualizarRespostaDTO.mensagem() != null) {
            this.mensagem = atualizarRespostaDTO.mensagem();
        }
        if (atualizarRespostaDTO.resolvida() != null) {
            this.resolvida = atualizarRespostaDTO.resolvida();
        }
        this.ultimaAtualizacao = LocalDateTime.now();
    }

    public void excluirResposta() {
        this.excluida = true;
    }
}
