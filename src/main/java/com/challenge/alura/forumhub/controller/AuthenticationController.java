package com.challenge.alura.forumhub.controller;

import com.challenge.alura.forumhub.domain.usuario.Usuario;
import com.challenge.alura.forumhub.domain.usuario.dto.AutenticarUsuarioDTO;
import com.challenge.alura.forumhub.infra.security.DadosTokenJWT;
import com.challenge.alura.forumhub.infra.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<DadosTokenJWT> autenticarUsuario(@RequestBody @Valid AutenticarUsuarioDTO autenticarUsuario) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(autenticarUsuario.username(),
                autenticarUsuario.password());
        var usuarioAutenticado = authenticationManager.authenticate(authToken);
        var JWTtoken = tokenService.gerarToken((Usuario) usuarioAutenticado.getPrincipal());
        return ResponseEntity.ok(new DadosTokenJWT(JWTtoken));
    }

}
