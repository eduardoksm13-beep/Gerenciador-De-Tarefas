CREATE TABLE IF NOT EXISTS usuarios(
    id SERIAL primary key,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL ,
    );
CREATE TABLE IF NOT EXISTS tarefas(
    id SERIAL primary key,
    titulo VARCHAR(150) NOT NULL,
    descricao TEXT,
    concluida BOOLEAN DEFAULT FALSE,
    data_criacao TIMESTAMP DEFAULT NOW(),
    usuarios_id INTEGER REFERENCES usuarios(id) ON DELETE CASCADE
);