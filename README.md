# ForumHub - API REST de Fórum 💬

Projeto de fórum de discussões com autenticação, cadastro de usuários, tópicos, cursos e respostas. Desenvolvido com **Java**, **Spring Boot** e **MySQL**, com autenticação segura via **JWT**.

## Tecnologias Usadas 🧑‍💻

<div>
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white">
  <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">
  <img src="https://img.shields.io/badge/MySQL-00758F?style=for-the-badge&logo=mysql&logoColor=white">
  <img src="https://img.shields.io/badge/JWT-000000?style=for-the-badge&logo=JSON%20web%20tokens&logoColor=white">
  <img src="https://img.shields.io/badge/Lombok-E62C2C?style=for-the-badge&logo=java&logoColor=white">
</div>

---

## Funcionalidades 💡

1. **Cadastro e login de usuários**
2. **Criação, listagem, atualização e exclusão lógica de tópicos**
3. **Gerenciamento de cursos com ativação/desativação**
4. **Criação e atualização de respostas, incluindo marcação de solução**
5. **Autenticação segura com JWT**
6. **Tratamento de erros centralizado**
7. **Documentação com Swagger UI**

---

## Estrutura do Projeto 🗂️

- `controller/`: endpoints REST da aplicação  
- `domain/`: entidades, DTOs e lógica de domínio  
- `infra/`: segurança, serviços auxiliares, documentação e tratamento de exceções  
- `ForumhubApplication.java`: ponto de entrada principal da aplicação  
- `application.properties`: configurações do projeto (banco de dados, token, logs)  
- `pom.xml`: gerenciamento de dependências e plugins Maven 

---


## Endpoints Principais 📡

| Método | Endpoint               | Descrição                        |
|--------|------------------------|----------------------------------|
| POST   | `/login`               | Login e geração de token         |
| POST   | `/users`               | Cadastro de novo usuário         |
| GET    | `/usuarios`            | Lista usuários habilitados       |
| PUT    | `/usuarios/{username}` | Atualiza dados do usuário        |
| DELETE | `/usuarios/{username}` | Desativa usuário (soft delete)   |
| GET    | `/topicos`             | Lista tópicos abertos            |
| POST   | `/topicos`             | Criação de novo tópico           |
| PUT    | `/topicos/{id}`        | Atualiza tópico existente        |
| DELETE | `/topicos/{id}`        | Exclui (lógico) um tópico        |
| GET    | `/topicos/{id}/solucao`| Ver resposta marcada como solução|

---

## Segurança 🔐

- JWT gerado no login
- Validação em filtro personalizado `SecurityFilter`
- Tokens expiram após 6 horas
- Somente endpoints públicos são liberados sem autenticação:
  - `/login`
  - `/users`
  - `/v3/api-docs/**`
  - `/swagger-ui.html`, `/swagger-ui/**`

---

## Tratamento de Erros 🧯

- `404`: Entidade não encontrada  
- `400`: Dados inválidos com detalhes por campo  
- `ValidationException`: Regras de negócio quebradas  

---

## Lógica Interna ⚙️

### 🔄 Criação e atualização de entidades

- **Usuários**: criados com senha criptografada (`BCrypt`), capitalização automática de nome e nickname.
- **Tópicos/Respostas**: têm campos de data de criação e última atualização.
- **Exclusão lógica**: tanto usuários quanto tópicos e respostas são desativados com campos `habilitado`, `estado`, ou `excluida`.

### 🔑 Token JWT

- Criado via `TokenService`
- Assinado com chave secreta (`HMAC256`)
- Armazena o `username` como subject

---

## Requisitos 💼

- Java 21+
- Maven 3.9+
- MySQL 8+
- IDE recomendada: IntelliJ ou VSCode com extensão Spring

---

## Como Usar 🚀

1. Clone o projeto e configure o banco MySQL com a database `forum_hub`.
2. No arquivo `application.properties`, atualize:

```properties
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
api.security.token.secret=uma-chave-secreta
```

3. Compile o projeto com:

```bash
mvn clean install
```

---
