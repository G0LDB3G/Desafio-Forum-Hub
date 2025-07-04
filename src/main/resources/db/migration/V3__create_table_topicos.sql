CREATE TABLE topicos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    estado ENUM('OPEN', 'CLOSED', 'DELETED') NOT NULL,
    data_criacao DATETIME NOT NULL,
    mensagem VARCHAR(255) NOT NULL,
    titulo VARCHAR(255) NOT NULL,
    ultima_atualizacao DATETIME NOT NULL,
    curso_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    FOREIGN KEY (curso_id) REFERENCES cursos(id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
