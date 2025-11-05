CREATE DATABASE IF NOT EXISTS best_umbrella;
USE best_umbrella;

-- ========================= 
-- Tabela: Utilizador 
-- ========================= 
CREATE TABLE IF NOT EXISTS Utilizador (
    utilizador_id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    telefone VARCHAR(20),
    data_registo DATETIME DEFAULT CURRENT_TIMESTAMP,
    rating DECIMAL(3,2)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================= 
-- Tabela: Ponto_de_aluguer 
-- ========================= 
CREATE TABLE IF NOT EXISTS Ponto_de_aluguer (
    ponto_id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    latitude DECIMAL(10,7) NOT NULL,
    longitude DECIMAL(10,7) NOT NULL,
    capacidade INT NOT NULL,
    tipo VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================= 
-- Tabela: Guarda_chuva 
-- ========================= 
CREATE TABLE IF NOT EXISTS Guarda_chuva (
    guarda_chuva_id INT AUTO_INCREMENT PRIMARY KEY,
    codigo_qr VARCHAR(100) UNIQUE NOT NULL,
    estado VARCHAR(50) NOT NULL,
    cor VARCHAR(50),
    tipo VARCHAR(50),
    ponto_id INT,
    data_registo DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ponto_id) REFERENCES Ponto_de_aluguer(ponto_id)
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================= 
-- Tabela: Aluguer 
-- ========================= 
CREATE TABLE IF NOT EXISTS Aluguer (
    aluguer_id INT AUTO_INCREMENT PRIMARY KEY,
    utilizador_id INT,
    guarda_chuva_id INT,
    ponto_inicio_id INT,
    ponto_fim_id INT,
    data_inicio DATETIME DEFAULT CURRENT_TIMESTAMP,
    data_fim DATETIME,
    custo DECIMAL(6,2),
    estado VARCHAR(50),
    FOREIGN KEY (utilizador_id) REFERENCES Utilizador(utilizador_id)
        ON DELETE CASCADE,
    FOREIGN KEY (guarda_chuva_id) REFERENCES Guarda_chuva(guarda_chuva_id)
        ON DELETE SET NULL,
    FOREIGN KEY (ponto_inicio_id) REFERENCES Ponto_de_aluguer(ponto_id)
        ON DELETE SET NULL,
    FOREIGN KEY (ponto_fim_id) REFERENCES Ponto_de_aluguer(ponto_id)
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================= 
-- Tabela: Notificacao 
-- ========================= 
CREATE TABLE IF NOT EXISTS Notificacao (
    notificacao_id INT AUTO_INCREMENT PRIMARY KEY,
    utilizador_id INT,
    mensagem TEXT NOT NULL,
    tipo VARCHAR(50),
    data_envio DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(50),
    FOREIGN KEY (utilizador_id) REFERENCES Utilizador(utilizador_id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ========================= 
-- Tabela: Multa 
-- ========================= 
CREATE TABLE IF NOT EXISTS Multa (
    multa_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    utilizador_id INT NULL,
    aluguer_id INT NULL,

    valor DOUBLE NULL,
    moeda VARCHAR(16) NULL,
    estado VARCHAR(20) NULL,     
    motivo VARCHAR(20) NULL,      
    descricao VARCHAR(255) NULL,

    data_emissao DATETIME NULL,
    data_vencimento DATETIME NULL,
    data_pagamento DATETIME NULL,

    jurosAcumulados DOUBLE NULL,
    descontoAplicado DOUBLE NULL,

    INDEX idx_multa_utilizador (utilizador_id),
    INDEX idx_multa_estado (estado),
    INDEX idx_multa_vencimento (data_vencimento),

    CONSTRAINT fk_multa_utilizador
        FOREIGN KEY (utilizador_id) REFERENCES Utilizador(utilizador_id)
        ON UPDATE CASCADE ON DELETE SET NULL,

    CONSTRAINT fk_multa_aluguer
        FOREIGN KEY (aluguer_id) REFERENCES Aluguer(aluguer_id)
        ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

