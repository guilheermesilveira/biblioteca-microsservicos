CREATE TABLE IF NOT EXISTS autores (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    nacionalidade VARCHAR(80) NOT NULL,
    ano_nascimento INTEGER NOT NULL CHECK (ano_nascimento > 0)
);

INSERT INTO autores (nome, nacionalidade, ano_nascimento) VALUES
    ('Machado de Assis', 'Brasileira', 1839),
    ('Clarice Lispector', 'Brasileira', 1920),
    ('George Orwell', 'Britanica', 1903)
ON CONFLICT DO NOTHING;
