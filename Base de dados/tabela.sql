-- Tabela GUARDACHUVA
CREATE TABLE Guardachuva (
    id SERIAL PRIMARY KEY,
    codigo_qr VARCHAR(50) UNIQUE NOT NULL,
    estado VARCHAR(50),
    cor VARCHAR(30),
    tipo VARCHAR(50)
);

-- Tabela TIPO
CREATE TABLE Tipo (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(100)
);

-- Tabela CON (Condição ou Configuração)
CREATE TABLE Con (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(100),
    tipo_id INT REFERENCES Tipo(id)
);

-- Tabela GE (Geografia ou Grupo Especial)
CREATE TABLE GE (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100),
    con_id INT REFERENCES Con(id)
);

-- Tabela UGR (Unidade de Gestão Regional)
CREATE TABLE UGR (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100),
    ge_id INT REFERENCES GE(id)
);

-- Tabela ESTADO (Estado atual do guarda-chuva ou sistema)
CREATE TABLE Estado (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(100),
    guardachuva_id INT REFERENCES Guardachuva(id)
);

-- Tabela ESTAPA (Etapa ou fase do processo)
CREATE TABLE Estapa (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100),
    estado_id INT REFERENCES Estado(id)
);

-- Tabela ZONA
CREATE TABLE Zona (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100)
);

-- Tabela CPDAD (Cidade ou ponto de atuação)
CREATE TABLE CPDAD (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100),
    zona_id INT REFERENCES Zona(id)
);

-- Tabela UTILIZADOR
CREATE TABLE Utilizador (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL, -- armazenar hash
    telefone VARCHAR(20),
    rating DECIMAL(2,1) CHECK (rating BETWEEN 0 AND 5),
    perfil VARCHAR(50), -- ex: 'admin', 'gestor', 'cliente'
    ativo BOOLEAN DEFAULT TRUE,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Relacionamentos opcionais
    zona_id INT REFERENCES Zona(id),
    guarda_chuva_id INT REFERENCES Guardachuva(id),
    ugr_id INT REFERENCES UGR(id)
);
