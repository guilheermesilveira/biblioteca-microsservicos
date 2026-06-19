# Sistema de Biblioteca em Microsservicos

Trabalho final de Desenvolvimento Web com Java: sistema de biblioteca em arquitetura de microsservicos com Spring Boot, Thymeleaf, PostgreSQL, Docker Compose e Nginx.

## Arquitetura

- `nginx`: API Gateway em `http://localhost`, com CORS centralizado.
- `front-ms`: interface web Thymeleaf na porta interna `8080`.
- `autor-ms`: API REST de autores na porta interna `8082`.
- `livro-ms`: API REST de livros na porta interna `8081`.
- `postgres-autores`: banco independente `db_autores`.
- `postgres-livros`: banco independente `db_livros`.

O `livro-ms` armazena somente `autorId`. Ele nao consulta o `autor-ms` e nao possui chave estrangeira entre bancos. O nome do autor e resolvido pelo `front-ms` via HTTP.

## Como executar

Na raiz do projeto:

```bash
docker compose up --build
```

Depois da inicializacao:

- Interface web: `http://localhost`
- API de livros: `http://localhost/api/livros`
- API de autores: `http://localhost/api/autores`

Para parar os containers:

```bash
docker compose down
```

Para reiniciar preservando dados:

```bash
docker compose restart
```

Para apagar containers e volumes dos bancos:

```bash
docker compose down -v
```

## Persistencia e carga inicial

Os volumes `postgres-autores-data` e `postgres-livros-data` preservam os dados entre reinicializacoes. Os scripts em `docker/postgres-autores` e `docker/postgres-livros` criam as tabelas e inserem dados iniciais apenas na primeira criacao dos volumes.

## Rotas web

- `GET /autores`: lista autores.
- `GET /autores/novo`: formulario de cadastro de autor.
- `GET /autores/{id}/editar`: formulario de edicao de autor.
- `POST /autores`: cria autor.
- `POST /autores/{id}`: atualiza autor.
- `POST /autores/{id}/excluir`: exclui autor.
- `GET /livros`: lista livros, com filtro opcional `?disponivel=true` ou `?disponivel=false`.
- `GET /livros/novo`: formulario de cadastro de livro.
- `GET /livros/{id}`: detalhe do livro.
- `GET /livros/{id}/editar`: formulario de edicao de livro.
- `POST /livros`: cria livro.
- `POST /livros/{id}`: atualiza livro.
- `POST /livros/{id}/excluir`: exclui livro.

## Endpoints REST

### Autores

- `GET /api/autores`
- `GET /api/autores/{id}`
- `POST /api/autores`
- `PUT /api/autores/{id}`
- `DELETE /api/autores/{id}`

Exemplo de corpo:

```json
{
  "nome": "Machado de Assis",
  "nacionalidade": "Brasileira",
  "anoNascimento": 1839
}
```

### Livros

- `GET /api/livros`
- `GET /api/livros?disponivel=true`
- `GET /api/livros?disponivel=false`
- `GET /api/livros/{id}`
- `POST /api/livros`
- `PUT /api/livros/{id}`
- `DELETE /api/livros/{id}`

Exemplo de corpo:

```json
{
  "titulo": "Dom Casmurro",
  "genero": "Romance",
  "anoPublicacao": 1899,
  "disponivel": true,
  "autorId": 1
}
```

## Testes

Para executar os testes localmente:

```bash
mvn test
```
