create table guardachuva (
gchuva_id int not null auto_increment,
gchuva_num VARCHAR(60) not null, 		    
 gchuva_datareg Date not null,
    gchuva_cor_id INT,
	gchuva_tipo_id INT,
	primary key (gchuva_id)	

);
		     		     
create table cor (
	cor_id int not null auto_increment,
	cor_name VARCHAR(40) not null, 	
	primary key (cor_id)
);


create table tipo (
	tip_id int not null auto_increment,
	tip_name VARCHAR(40) not null, 
	primary key (tip_id)
);


create table cidade (
	cid_id int not null auto_increment,
	cid_name VARCHAR(40) not null, 					
	primary key (cid_id)
);




create table zona (
zon_id int not null auto_increment,
zon_name VARCHAR(40) not null,
zon_cid_id INT not null,
primary key (zon_id)
);


create table estacao (
est_id int not null auto_increment,
est_name VARCHAR(40) not null,
est_zon_id INT not null,
est_lat DOUBLE,
est_long DOUBLE,
est_cap INT,
primary key (est_id)
);


create table ge (
ge_id int not null auto_increment,
ge_datein date not null,
ge_datout date,
ge_gchuva_id INT not null,
ge_est_id INT not null,
primary key (ge_id)
);




create table ugem (
ugem_id int not null auto_increment,
ugem_datein date not null,
ugem_datout date,
ugem_gchuva_id INT not null,
ugem_ut_id INT not null,
primary key (ugem_id)
);





CREATE TABLE multa (
 mul_id INT NOT NULL AUTO_INCREMENT,
 mul_ut_id INT NOT NULL,
mul_dataem DATE NOT NULL,
mul_dataven DATE NOT NULL,
 mul_moeda VARCHAR(255),
 mul_mot VARCHAR(255),
 mul_valor DECIMAL(10,2),
 PRIMARY KEY (mul_id)
);


create table mugem (			
mugem_id INT not null auto_increment,
mugem_ugem_id INT not null,
mugem_mul_id INT not null,
primary key (mugem_id)
					


);

create table utilizador (
ut_id int not null auto_increment,
ut_name VARCHAR(255),
ut_email VARCHAR (255),
ut_password VARCHAR (255),
ut_telefone VARCHAR (255),
ut_datareg DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
ut_rating DOUBLE,
primary key (ut_id)
);





CREATE TABLE notificacao (
    not_id INT NOT NULL AUTO_INCREMENT,
    ut_not_id INT NOT NULL,
    not_msg VARCHAR(255),
    dataenv DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (not_id)
);

create table ugeme  (
					ugeme_id INT not null auto_increment,
					ugeme_ugem_id INT not null,
					ugeme_estado VARCHAR(255),
					ugeme_evento VARCHAR(100), --'inicio_aluguer', 'fim_aluguer', 'multa_aplicada'
					ugeme_data DATETIME not null DEFAULT CURRENT_TIMESTAMP,
					primary key (ugeme_id)

					
);


create table estado  (
					estado_id INT not null auto_increment,
					estado_name VARCHAR(255),
					est_ugeme_id INT not null,
					primary key (estado_id)
);



-- Foreign Keys

alter table guardachuva
add constraint guardahuva_fk_cor
foreign key (gchuva_cor_id) references cor(cor_id)
ON DELETE NO ACTION ON UPDATE NO ACTION;

-- Foreign Keys para a tabela guardachuva
ALTER TABLE guardachuva
ADD CONSTRAINT guardachuva_fk_tipo
FOREIGN KEY (gchuva_tipo_id) REFERENCES tipo(tip_id)
ON DELETE NO ACTION ON UPDATE NO ACTION;

-- Foreign Keys para a tabela zona
ALTER TABLE zona
ADD CONSTRAINT zona_fk_cidade
FOREIGN KEY (zon_cid_id) REFERENCES cidade(cid_id)
ON DELETE NO ACTION ON UPDATE NO ACTION;

-- Foreign Keys para a tabela estacao
ALTER TABLE estacao
ADD CONSTRAINT estacao_fk_zona
FOREIGN KEY (est_zon_id) REFERENCES zona(zon_id)
ON DELETE NO ACTION ON UPDATE NO ACTION;

-- Foreign Keys para a tabela ge
ALTER TABLE ge
ADD CONSTRAINT ge_fk_guardachuva
FOREIGN KEY (ge_gchuva_id) REFERENCES guardachuva(gchuva_id)
ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE ge
ADD CONSTRAINT ge_fk_estacao
FOREIGN KEY (ge_est_id) REFERENCES estacao(est_id)
ON DELETE NO ACTION ON UPDATE NO ACTION;

-- Foreign Keys para a tabela ugem
ALTER TABLE ugem
ADD CONSTRAINT ugem_fk_guardachuva
FOREIGN KEY (ugem_gchuva_id) REFERENCES guardachuva(gchuva_id)
ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE ugem
ADD CONSTRAINT ugem_fk_utilizador
FOREIGN KEY (ugem_ut_id) REFERENCES utilizador(ut_id)
ON DELETE NO ACTION ON UPDATE NO ACTION;

-- Foreign Keys para a tabela multa
ALTER TABLE multa
ADD CONSTRAINT multa_fk_utilizador
FOREIGN KEY (mul_ut_id) REFERENCES utilizador(ut_id)
ON DELETE NO ACTION ON UPDATE NO ACTION;

-- Foreign Keys para a tabela mugem
ALTER TABLE mugem
ADD CONSTRAINT mugem_fk_ugem
FOREIGN KEY (mugem_ugem_id) REFERENCES ugem(ugem_id)
ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE mugem
ADD CONSTRAINT mugem_fk_multa
FOREIGN KEY (mugem_mul_id) REFERENCES multa(mul_id)
ON DELETE NO ACTION ON UPDATE NO ACTION;

-- Foreign Keys para a tabela notificacao
ALTER TABLE notificacao
ADD CONSTRAINT notificacao_fk_utilizador
FOREIGN KEY (ut_not_id) REFERENCES utilizador(ut_id)
ON DELETE NO ACTION ON UPDATE NO ACTION;

-- Foreign Keys para a tabela ugeme
ALTER TABLE ugeme
ADD CONSTRAINT ugeme_fk_ugem
FOREIGN KEY (ugeme_ugem_id) REFERENCES ugem(ugem_id)
ON DELETE NO ACTION ON UPDATE NO ACTION;

-- Foreign Keys para a tabela estado
ALTER TABLE estado
ADD CONSTRAINT estado_fk_ugeme
FOREIGN KEY (est_ugeme_id) REFERENCES ugeme(ugeme_id)
ON DELETE NO ACTION ON UPDATE NO ACTION;

