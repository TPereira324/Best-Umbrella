
-- Lista todas as cores
SELECT * FROM cor;


-- Lista todos os tipos de guarda-chuva
SELECT * FROM tipo;


-- Lista todas as cidades
SELECT * FROM cidade;


-- Lista todas as zonas
SELECT * FROM zona;

-- Lista zonas com o nome da cidade associada
SELECT z.zon_id, z.zon_name, c.cid_name
FROM zona z
JOIN cidade c ON z.zon_cid_id = c.cid_id;


-- Lista todas as estações
SELECT * FROM estacao;

-- Lista estações com zona e cidade
SELECT e.est_id, e.est_name, e.est_lat, e.est_long, z.zon_name, c.cid_name
FROM estacao e
JOIN zona z ON e.est_zon_id = z.zon_id
JOIN cidade c ON z.zon_cid_id = c.cid_id;


-- Lista todos os guarda-chuvas
SELECT * FROM guardachuva;

-- Lista guarda-chuvas com cor e tipo
SELECT g.gchuva_id, g.gchuva_num, g.gchuva_datareg, c.cor_name, t.tip_name
FROM guardachuva g
LEFT JOIN cor c ON g.gchuva_cor_id = c.cor_id
LEFT JOIN tipo t ON g.gchuva_tipo_id = t.tip_id;


-- Lista todos os registos GE
SELECT * FROM ge;

-- Lista guarda-chuvas com a estação onde foram registados
SELECT ge.ge_id, ge.ge_datein, ge.ge_datout, g.gchuva_num, e.est_name
FROM ge
JOIN guardachuva g ON ge.ge_gchuva_id = g.gchuva_id
JOIN estacao e ON ge.ge_est_id = e.est_id;



-- Lista todos os utilizadores
SELECT * FROM utilizador;


-- Lista todos os alugueres
SELECT * FROM ugem;


-- Lista alugueres com utilizador + guarda-chuva
SELECT u.ugem_id, u.ugem_datein, u.ugem_datout, g.gchuva_num, ut.ut_name
FROM ugem u
JOIN guardachuva g ON u.ugem_gchuva_id = g.gchuva_id
JOIN utilizador ut ON u.ugem_ut_id = ut.ut_id;



-- Lista todas as multas
SELECT * FROM multa;


-- Lista multas com o nome do utilizador associado
SELECT m.*, u.ut_name
FROM multa m
JOIN utilizador u ON m.mul_ut_id = u.ut_id;



-- Lista todos os registos mugem
SELECT * FROM mugem;


-- Lista mugem com todos os detalhes: utilizador + guarda-chuva + valor da multa
SELECT mg.mugem_id, u.ut_name, g.gchuva_num, m.mul_valor
FROM mugem mg
JOIN ugem ug ON mg.mugem_ugem_id = ug.ugem_id
JOIN multa m ON mg.mugem_mul_id = m.mul_id
JOIN utilizador u ON ug.ugem_ut_id = u.ut_id
JOIN guardachuva g ON ug.ugem_gchuva_id = g.gchuva_id;



-- Lista todas as notificações
SELECT * FROM notificacao;


-- Lista notificações com o utilizador associado
SELECT n.*, u.ut_name
FROM notificacao n
JOIN utilizador u ON n.ut_not_id = u.ut_id;


-- Lista todos os eventos de aluguer
SELECT * FROM ugeme;

-- Lista todos os estados
SELECT * FROM estado;

-- Lista estados com o evento associado no ugeme
SELECT e.estado_id, e.estado_name, u.ugeme_evento
FROM estado e
JOIN ugeme u ON e.est_ugeme_id = u.ugeme_id;




