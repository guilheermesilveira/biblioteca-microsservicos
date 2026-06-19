CREATE TABLE IF NOT EXISTS livros (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    genero VARCHAR(60) NOT NULL,
    ano_publicacao INTEGER NOT NULL CHECK (ano_publicacao > 0),
    disponivel BOOLEAN NOT NULL DEFAULT TRUE,
    autor_id BIGINT NOT NULL
);

INSERT INTO livros (titulo, genero, ano_publicacao, disponivel, autor_id) VALUES
    ('Dom Casmurro', 'Romance', 1899, TRUE, 1),
    ('A Hora da Estrela', 'Romance', 1977, TRUE, 2),
    ('1984', 'Distopia', 1949, FALSE, 3)
ON CONFLICT DO NOTHING;
