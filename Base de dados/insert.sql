INSERT INTO Utilizador (nome, email, password, telefone, rating)
VALUES 
('Taha-Wur Pereira', 'taha@umbrella.pt', '12345', '912345678', 4.8),
('Joybeth Mateus', 'joybeth@umbrella.pt', '12345', '913456789', 4.5),
('Márcio Quintas', 'marcio@umbrella.pt', '12345', '914567890', 4.7),
('Feleciano Ramos', 'feleciano@umbrella.pt', '12345', '915678901', 4.6),
('Fábio Teixeira', 'fabio@umbrella.pt', '12345', '916789012', 4.9),
('Moira Silva', 'moira@umbrella.pt', '12345', '917890123', 4.4);
---
INSERT INTO Ponto_de_aluguer (ponto_id, nome, latitude, longitude, capacidade, tipo)
VALUES
(1, 'Metro Moscavide', 38.77639, -9.10169, 8, 'Estação'),
(2, 'Metro Oriente', 38.76784, -9.09935, 4, 'Estação'),
(3, 'Parque das Nações Norte', 38.76800, -9.09400, 6, 'Zona Urbana'),
(4, 'IADE', 38.7818, -9.10251, 3, 'Campus'),
(5, 'Terreiro do Paço', 38.7073, -9.1367, 10, 'Turístico'),
(6, 'Baixa-Chiado', 38.7111, -9.1419, 8, 'Centro'),
(7, 'Marquês de Pombal', 38.7256, -9.1501, 12, 'Centro'),
(8, 'Rossio', 38.7142, -9.1410, 7, 'Centro');
---
INSERT INTO Guarda_chuva (codigo_qr, estado, cor, tipo, ponto_id)
VALUES
('QR001', 'Disponível', 'Azul', 'Automático', 1),
('QR002', 'Disponível', 'Vermelho', 'Compacto', 2),
('QR003', 'Em uso', 'Preto', 'Automático', 3),
('QR004', 'Manutenção', 'Amarelo', 'Manual', 4),
('QR005', 'Disponível', 'Roxo', 'Compacto', 5),
('QR006', 'Disponível', 'Cinza', 'Automático', 6),
('QR007', 'Em uso', 'Verde', 'Manual', 7),
('QR008', 'Disponível', 'Preto', 'Compacto', 8);
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
INSERT INTO Notificacao (utilizador_id, mensagem, tipo, estado)
VALUES
(1, 'Bem-vindo à Best Umbrella!', 'Boas-vindas', 'Lido'),
(2, 'O seu aluguer foi iniciado.', 'Aluguer', 'Não lido'),
(3, 'O seu pagamento foi confirmado.', 'Pagamento', 'Lido'),
(4, 'Guarda-chuva devolvido com sucesso.', 'Aluguer', 'Lido'),
(5, 'Nova promoção disponível!', 'Promoção', 'Não lido'),
(6, 'A sua conta foi atualizada.', 'Conta', 'Lido');
---
INSERT INTO Multa (utilizador_id, aluguer_id, valor, moeda, estado, motivo, descricao, data_emissao, data_vencimento)
VALUES
(1, 1, 5.00, 'EUR', 'PENDENTE', 'ATRASO', 'Devolução fora de prazo', NOW(), DATE_ADD(NOW(), INTERVAL 7 DAY)),
(3, 3, 15.00, 'EUR', 'PAGO', 'DANO', 'Dano no cabo do guarda-chuva', NOW(), NOW()),
(5, 5, 7.50, 'EUR', 'PENDENTE', 'ATRASO', 'Entrega após o prazo limite', NOW(), DATE_ADD(NOW(), INTERVAL 5 DAY));
