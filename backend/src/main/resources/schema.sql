-- Criar colunas ausentes na tabela `ugem` para compatibilizar com JPA
-- Executado pelo Spring Boot em startup (spring.sql.init.mode=always)

ALTER TABLE ugem
  ADD COLUMN IF NOT EXISTS ugem_custo DOUBLE NOT NULL DEFAULT 0,
  ADD COLUMN IF NOT EXISTS estado VARCHAR(50) NULL,
  ADD COLUMN IF NOT EXISTS ponto_inicio_id INT NULL,
  ADD COLUMN IF NOT EXISTS ponto_fim_id INT NULL;

-- Nota: FKs opcionais (evitamos aqui para não falhar se já existirem)
-- ALTER TABLE ugem ADD CONSTRAINT fk_ugem_inicio FOREIGN KEY (ponto_inicio_id) REFERENCES estacao(est_id);
-- ALTER TABLE ugem ADD CONSTRAINT fk_ugem_fim FOREIGN KEY (ponto_fim_id) REFERENCES estacao(est_id);