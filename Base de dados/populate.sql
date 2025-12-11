-- Inserir dados na tabela cidade
INSERT INTO cidade (cid_name) VALUES 
('Lisboa'),
('Porto'),
('Braga'),
('Coimbra'),
('Faro'),
('Setúbal'),
('Aveiro');

-- Inserir dados na tabela cor
INSERT INTO cor (cor_name) VALUES 
('Azul'),
('Vermelho'),
('Preto'),
('Amarelo'),
('Roxo'),
('Cinza'),
('Verde'),
('Branco');

-- Inserir dados na tabela tipo
INSERT INTO tipo (tip_name) VALUES 
('Automático'),
('Compacto'),
('Manual'),
('Urbano'),
('Turístico');

-- Inserir dados na tabela utilizador
INSERT INTO utilizador (ut_name, ut_email, ut_password, ut_telefone, ut_rating) VALUES 
('Taha-Wur Pereira', 'taha@umbrella.pt', '12345', '912345678', 4.8),
('Joybeth Mateus', 'joybeth@umbrella.pt', '12345', '913456789', 4.5),
('Márcio Quintas', 'marcio@umbrella.pt', '12345', '914567890', 4.7),
('Feleciano Ramos', 'feleciano@umbrella.pt', '12345', '915678901', 4.6),
('Fábio Teixeira', 'fabio@umbrella.pt', '12345', '916789012', 4.9),
('Moira Silva', 'moira@umbrella.pt', '12345', '917890123', 4.4);

-- Inserir dados na tabela zona
INSERT INTO zona (zon_name, zon_cid_id) VALUES
('Terreiro do Paço', 1),
('IADE', 1),
('Metro Moscavide', 1),
('Metro Oriente', 1),
('Rossio', 1),
('Baixa-Chiado', 1),
('Marquês de Pombal', 1);

-- Inserir dados na tabela estacao com coordenadas CORRETAS
INSERT INTO estacao (est_name, est_zon_id, est_lat, est_long, est_cap) VALUES
('IADE', 2, 38.7818, -9.10251, 3),
('Parque das Nações', 1, 38.76800, -9.09400, 6),
('Metro Moscavide', 3, 38.77639, -9.10169, 8),
('Metro Oriente', 4, 38.76784, -9.09935, 4),
('Terreiro do Paço', 1, 38.70667, -9.13528, 10),
('Rossio', 5, 38.713718, -9.139681, 7),
('Baixa-Chiado', 6, 38.71056, -9.14000, 8),
('Marquês de Pombal', 7, 38.724686, -9.150442, 12);

-- Inserir dados na tabela guardachuva
INSERT INTO guardachuva (gchuva_num, gchuva_datareg, gchuva_cor_id, gchuva_tipo_id, est_id) VALUES
('QR001', CURDATE(), 1, 1, 1),  -- Azul, Automático, Estação IADE
('QR002', CURDATE(), 2, 2, 2),  -- Vermelho, Compacto, Estação Parque das Nações
('QR003', CURDATE(), 3, 1, 3),  -- Preto, Automático, Estação Metro Moscavide
('QR004', CURDATE(), 4, 3, 4),  -- Amarelo, Manual, Estação Metro Oriente
('QR005', CURDATE(), 5, 2, 5),  -- Roxo, Compacto, Estação Terreiro do Paço
('QR006', CURDATE(), 6, 1, 6),  -- Cinza, Automático, Estação Rossio
('QR007', CURDATE(), 7, 1, 7),  -- Verde, Automático, Estação Baixa-Chiado
('QR008', CURDATE(), 8, 2, 8);  -- Branco, Compacto, Estação Marquês de Pombal

-- Inserir dados na tabela ge (Guardachuvas em Estação)
INSERT INTO ge (ge_datein, ge_datout, ge_gchuva_id, ge_est_id) VALUES
(CURDATE(), NULL, 1, 1),
(CURDATE(), NULL, 2, 2),
(CURDATE(), NULL, 3, 3),
(CURDATE(), NULL, 4, 4),
(CURDATE(), NULL, 5, 5),
(CURDATE(), NULL, 6, 6),
(CURDATE(), NULL, 7, 7),
(CURDATE(), NULL, 8, 8);

-- Inserir dados na tabela ugem 
INSERT INTO ugem (ugem_datein, ugem_datout, ugem_gchuva_id, ugem_ut_id, ugem_chuva_id, ponto_fim_est_id) VALUES
('2024-01-15', '2024-01-20', 3, 2, 3, 2),  -- Joybeth usou guardachuva 3
('2024-01-16', NULL, 1, 3, 1, NULL),      -- Márcio usando guardachuva 1
('2024-01-17', '2024-01-18', 5, 4, 5, 4),  -- Feleciano usou guardachuva 5
('2024-01-18', NULL, 2, 5, 2, NULL),      -- Fábio usando guardachuva 2
(CURDATE(), NULL, 6, 6, 6, NULL);         -- Moira usando guardachuva 6

-- Inserir dados na tabela multa
INSERT INTO multa (mul_ut_id, mul_dataem, mul_dataven, mul_moeda, mul_mot, mul_valor) VALUES
(2, '2024-01-21', '2024-02-21', 'EUR', 'Devolução tardia', 5.00),
(4, '2024-01-19', '2024-02-19', 'EUR', 'Danos no equipamento', 10.00),
(6, '2024-01-22', '2024-02-22', 'EUR', 'Perda do guardachuva', 15.00);

-- Inserir dados na tabela mugem 
INSERT INTO mugem (mugem_ugem_id, mugem_mul_id) VALUES
(1, 1),  -- Multa 1 associada ao empréstimo 1
(3, 2),  -- Multa 2 associada ao empréstimo 3
(5, 3);  -- Multa 3 associada ao empréstimo 5

-- Inserir dados na tabela notificacao
INSERT INTO notificacao (ut_not_id, not_msg) VALUES
(2, 'A sua multa por devolução tardia foi aplicada. Valor: 5.00 EUR'),
(4, 'Multa por danos no equipamento. Valor: 10.00 EUR'),
(6, 'Multa por perda do guardachuva. Valor: 15.00 EUR'),
(3, 'Lembrete: O seu empréstimo termina em breve'),
(5, 'Obrigado por usar o nosso serviço!');

-- Inserir dados na tabela ugeme 
INSERT INTO ugeme (ugeme_ugem_id, ugeme_estado, ugeme_evento, ugeme_data) VALUES
(1, 'Iniciado', 'inicio_aluguer', '2024-01-15 10:00:00'),
(1, 'Finalizado', 'fim_aluguer', '2024-01-20 14:30:00'),
(1, 'Multa aplicada', 'multa_aplicada', '2024-01-21 09:15:00'),
(2, 'Iniciado', 'inicio_aluguer', '2024-01-16 11:20:00'),
(3, 'Iniciado', 'inicio_aluguer', '2024-01-17 08:45:00'),
(3, 'Finalizado', 'fim_aluguer', '2024-01-18 16:20:00'),
(3, 'Multa aplicada', 'multa_aplicada', '2024-01-19 10:30:00'),
(4, 'Iniciado', 'inicio_aluguer', '2024-01-18 13:15:00'),
(5, 'Iniciado', 'inicio_aluguer', NOW());

-- Inserir dados na tabela estado
INSERT INTO estado (estado_name, est_ugeme_id) VALUES
('Aluguer ativo', 4),
('Aluguer ativo', 8),
('Aluguer ativo', 9),
('Aluguer finalizado', 2),
('Multa pendente', 3),
('Multa pendente', 7);
