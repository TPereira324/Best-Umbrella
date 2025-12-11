-- Tabelas básicas
CREATE TABLE cidade (
    cid_id BIGINT NOT NULL AUTO_INCREMENT,
    cid_name VARCHAR(40) NOT NULL,
    PRIMARY KEY (cid_id)
);
-- Cor
CREATE TABLE cor (
    cor_id BIGINT NOT NULL AUTO_INCREMENT,
    cor_name VARCHAR(40) NOT NULL,
    PRIMARY KEY (cor_id)
);
--Tipo
CREATE TABLE tipo (
    tip_id BIGINT NOT NULL AUTO_INCREMENT,
    tip_name VARCHAR(40) NOT NULL,
    PRIMARY KEY (tip_id)
);

--Utilizador
CREATE TABLE utilizador (
    ut_id BIGINT NOT NULL AUTO_INCREMENT,
    ut_name VARCHAR(255),
    ut_email VARCHAR(255) NOT NULL UNIQUE,
    ut_password VARCHAR(255),
    ut_telefone VARCHAR(255),
    ut_datareg DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ut_rating DOUBLE,
    PRIMARY KEY (ut_id)
);

-- Zona
CREATE TABLE zona (
    zon_id BIGINT NOT NULL AUTO_INCREMENT,
    zon_name VARCHAR(40) NOT NULL,
    zon_cid_id BIGINT NOT NULL,
    PRIMARY KEY (zon_id)
);

-- Estacao
CREATE TABLE estacao (
    est_id BIGINT NOT NULL AUTO_INCREMENT,
    est_name VARCHAR(255) NOT NULL,
    est_zon_id BIGINT NOT NULL,
    est_lat DOUBLE,
    est_long DOUBLE,
    est_cap INT,
    PRIMARY KEY (est_id)
);

-- Guardachuva 
CREATE TABLE guardachuva (
    gchuva_id BIGINT NOT NULL AUTO_INCREMENT,
    gchuva_num VARCHAR(60) NOT NULL,
    gchuva_datareg DATE NOT NULL,
    gchuva_cor_id BIGINT,
    gchuva_tipo_id BIGINT,
    est_id BIGINT,  
    PRIMARY KEY (gchuva_id)
);

-- UGEM 
CREATE TABLE ugem (
    ugem_id BIGINT NOT NULL AUTO_INCREMENT,
    ugem_datein DATE NOT NULL,
    ugem_datout DATE,
    ugem_gchuva_id BIGINT NOT NULL,
    ugem_ut_id BIGINT NOT NULL,
    ugem_chuva_id BIGINT,     
    ponto_fim_est_id BIGINT,   
    PRIMARY KEY (ugem_id)
);

-- GE
CREATE TABLE ge (
    ge_id BIGINT NOT NULL AUTO_INCREMENT,
    ge_datein DATE NOT NULL,
    ge_datout DATE,
    ge_gchuva_id BIGINT NOT NULL,
    ge_est_id BIGINT NOT NULL,
    PRIMARY KEY (ge_id)
);

-- Multa
CREATE TABLE multa (
    mul_id BIGINT NOT NULL AUTO_INCREMENT,
    mul_ut_id BIGINT NOT NULL,
    mul_dataem DATE NOT NULL,
    mul_dataven DATE NOT NULL,
    mul_moeda VARCHAR(255),
    mul_mot VARCHAR(255),
    mul_valor DECIMAL(10,2),
    PRIMARY KEY (mul_id)
);

-- Notificacao
CREATE TABLE notificacao (
    not_id BIGINT NOT NULL AUTO_INCREMENT,
    ut_not_id BIGINT NOT NULL,
    not_msg VARCHAR(255),
    dataenv DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (not_id)
);

-- Ugeme
CREATE TABLE ugeme (
    ugeme_id BIGINT NOT NULL AUTO_INCREMENT,
    ugeme_ugem_id BIGINT NOT NULL,
    ugeme_estado VARCHAR(255),
    ugeme_evento VARCHAR(100),
    ugeme_data DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (ugeme_id)
);

-- Estado
CREATE TABLE estado (
    estado_id BIGINT NOT NULL AUTO_INCREMENT,
    estado_name VARCHAR(255),
    est_ugeme_id BIGINT NOT NULL,
    PRIMARY KEY (estado_id)
);

-- Mugem
CREATE TABLE mugem (
    mugem_id BIGINT NOT NULL AUTO_INCREMENT,
    mugem_ugem_id BIGINT NOT NULL,
    mugem_mul_id BIGINT NOT NULL,
    PRIMARY KEY (mugem_id)
);

-- ============================================
-- FOREIGN KEYS
-- ============================================

-- Guardachuva
ALTER TABLE guardachuva
ADD CONSTRAINT guardahuva_fk_cor
FOREIGN KEY (gchuva_cor_id) REFERENCES cor(cor_id);

ALTER TABLE guardachuva
ADD CONSTRAINT guardachuva_fk_tipo
FOREIGN KEY (gchuva_tipo_id) REFERENCES tipo(tip_id);

--  Guardachuva
ALTER TABLE guardachuva
ADD CONSTRAINT guardachuva_fk_estacao
FOREIGN KEY (est_id) REFERENCES estacao(est_id);

-- Zona
ALTER TABLE zona
ADD CONSTRAINT zona_fk_cidade
FOREIGN KEY (zon_cid_id) REFERENCES cidade(cid_id);

-- Estacao
ALTER TABLE estacao
ADD CONSTRAINT estacao_fk_zona
FOREIGN KEY (est_zon_id) REFERENCES zona(zon_id);

-- GE
ALTER TABLE ge
ADD CONSTRAINT ge_fk_guardachuva
FOREIGN KEY (ge_gchuva_id) REFERENCES guardachuva(gchuva_id);

ALTER TABLE ge
ADD CONSTRAINT ge_fk_estacao
FOREIGN KEY (ge_est_id) REFERENCES estacao(est_id);

-- UGEM
ALTER TABLE ugem
ADD CONSTRAINT ugem_fk_guardachuva
FOREIGN KEY (ugem_gchuva_id) REFERENCES guardachuva(gchuva_id);

ALTER TABLE ugem
ADD CONSTRAINT ugem_fk_utilizador
FOREIGN KEY (ugem_ut_id) REFERENCES utilizador(ut_id);

 
ALTER TABLE ugem
ADD CONSTRAINT ugem_fk_guardachuva2
FOREIGN KEY (ugem_chuva_id) REFERENCES guardachuva(gchuva_id);

ALTER TABLE ugem
ADD CONSTRAINT ugem_fk_estacao_fim
FOREIGN KEY (ponto_fim_est_id) REFERENCES estacao(est_id);

-- Multa
ALTER TABLE multa
ADD CONSTRAINT multa_fk_utilizador
FOREIGN KEY (mul_ut_id) REFERENCES utilizador(ut_id);

-- Mugem
ALTER TABLE mugem
ADD CONSTRAINT mugem_fk_ugem
FOREIGN KEY (mugem_ugem_id) REFERENCES ugem(ugem_id);

ALTER TABLE mugem
ADD CONSTRAINT mugem_fk_multa
FOREIGN KEY (mugem_mul_id) REFERENCES multa(mul_id);

-- Notificacao
ALTER TABLE notificacao
ADD CONSTRAINT notificacao_fk_utilizador
FOREIGN KEY (ut_not_id) REFERENCES utilizador(ut_id);

-- Ugeme
ALTER TABLE ugeme
ADD CONSTRAINT ugeme_fk_ugem
FOREIGN KEY (ugeme_ugem_id) REFERENCES ugem(ugem_id);

-- Estado
ALTER TABLE estado
ADD CONSTRAINT estado_fk_ugeme
FOREIGN KEY (est_ugeme_id) REFERENCES ugeme(ugeme_id);

SET FOREIGN_KEY_CHECKS = 1;
