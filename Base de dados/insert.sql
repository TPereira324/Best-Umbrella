INSERT INTO Utilizador (nome, email, password, telefone, rating, perfil, zona_id, guarda_chuva_id, ugr_id)
VALUES 
('Taha-Wur Pereira', 'taha@umbrella.pt', '12345', '912345678', 4.8, 'gestor', 1, 1, 1),
('Joybeth Mateus', 'joybeth@umbrella.pt', '12345', '913456789', 4.5, 'cliente', 2, 2, 2),
('Márcio Quintas', 'marcio@umbrella.pt', '12345', '914567890', 4.7, 'cliente', 3, 3, 1),
('Feleciano Ramos', 'feleciano@umbrella.pt', '12345', '915678901', 4.6, 'gestor', 4, 4, 2),
('Fábio Teixeira', 'fabio@umbrella.pt', '12345', '916789012', 4.9, 'admin', 5, 5, 1),
('Moira Silva', 'moira@umbrella.pt', '12345', '917890123', 4.4, 'cliente', 6, 6, 2);
---
INSERT INTO Ponto_de_aluguer (nome, latitude, longitude, capacidade, tipo)
VALUES
('Parque das Nações', 38.7683, -9.0944, 10, 'Zona Urbana'),
('Marquês de Pombal', 38.7279, -9.1504, 10, 'Centro'),
('Metro Oriente', 38.7678, -9.0994, 10, 'Estação'),
('Baixa-Chiado', 38.7102, -9.1394, 10, 'Centro'),
('Rossio', 38.7148, -9.1412, 10, 'Centro'),
('Terreiro do Paço', 38.7073, -9.1367, 10, 'Turístico'),
('IADE', 38.777634, -9.095091, 10, 'Campus'),
('Metro Moscavide', 38.7764, -9.1017, 8, 'Estação');
---
INSERT INTO Guardachuva (codigo_qr, estado, cor, tipo)
VALUES
('QR001', 'Disponível', 'Azul', 'Automático'),
('QR002', 'Disponível', 'Vermelho', 'Compacto'),
('QR003', 'Em uso', 'Preto', 'Automático'),
('QR004', 'Manutenção', 'Amarelo', 'Manual'),
('QR005', 'Disponível', 'Roxo', 'Compacto'),
('QR006', 'Disponível', 'Cinza', 'Automático');
---
INSERT INTO Aluguer (utilizador_id, guarda_chuva_id, ponto_inicio_id, ponto_fim_id, custo, estado)
VALUES
(1, 1, 1, 2, 2.50, 'Concluído'),
(2, 2, 2, 3, 3.00, 'Em curso'),
(3, 3, 3, 4, 2.00, 'Cancelado'),
(4, 4, 4, 5, 4.00, 'Concluído'),
(5, 5, 5, 6, 3.50, 'Em curso'),
(6, 6, 6, 7, 2.75, 'Concluído');
---
INSERT INTO Zona (nome) VALUES
('Terreiro do Paço'),
('IADE'),
('Metro Moscavide'),
('Metro Oriente'),
('Rossio'),
('Baixa-Chiado'),
('Marquês de Pombal');

INSERT INTO CPDAD (nome, zona_id) VALUES
('Lisboa', 1),
('Lisboa', 2),
('Lisboa', 3),
('Lisboa', 4),
('Lisboa', 5),
('Lisboa', 6),
('Lisboa', 7);
---
INSERT INTO Tipo (descricao) VALUES ('Urbano'), ('Turístico');

INSERT INTO Con (descricao, tipo_id) VALUES 
('Coberto', 1), 
('Aberto', 2);

INSERT INTO GE (nome, con_id) VALUES 
('Lisboa Centro', 1), 
('Lisboa Norte', 2);

INSERT INTO UGR (nome, ge_id) VALUES 
('UGR A', 1), 
('UGR B', 2);

---
INSERT INTO Estado (descricao, guardachuva_id) VALUES 
('Disponível', 1), 
('Em uso', 2), 
('Manutenção', 3);

INSERT INTO Estapa (nome, estado_id) VALUES 
('Inicial', 1), 
('Em progresso', 2), 
('Finalizado', 3);
