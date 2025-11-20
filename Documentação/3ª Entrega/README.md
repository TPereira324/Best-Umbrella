# **Best Umbrella**

**Projeto Mobile – Universidade Europeia / IADE**
**Licenciatura em Engenharia Informática – 3º Semestre (2025/2026)**

---

## **Identificação**

* **Grupo:** G03
* **Elementos:** Fábio Teixeira, Feleciano Barata e Taha-Wur Pereira
* **Nome do Projeto:** Best Umbrella

---

## **Descrição do Projeto**

O **Best Umbrella** é uma aplicação móvel criada para resolver um problema urbano comum: ser apanhado pela chuva sem proteção e acabar por comprar guarda-chuvas descartáveis de baixa qualidade.

A solução passa por oferecer um sistema inovador e sustentável de **aluguer temporário de guarda-chuvas** em pontos estratégicos da cidade, como universidades, estações de transporte, cafés parceiros e centros comerciais.

Ao contrário de soluções que dependem de máquinas automáticas caras, o Best Umbrella funciona apenas com **QR Codes e um smartphone**, permitindo uma implementação rápida, económica e escalável.

---

## **Objetivos**

O projeto pretende disponibilizar uma experiência **prática, sustentável e inteligente**, promovendo hábitos de consumo conscientes e reduzindo o desperdício.

Distingue-se por ser uma solução **flexível, de baixo custo e altamente escalável**, facilmente replicável em qualquer cidade através de parcerias locais.

A integração com **APIs meteorológicas** permite prever chuva e enviar notificações proativas, incentivando o utilizador a reservar um guarda-chuva antes de ser surpreendido pelo mau tempo.

---

## **Público-Alvo**

* Estudantes e trabalhadores urbanos.
* Turistas que precisam de soluções temporárias.
* Estabelecimentos parceiros que beneficiam da presença de pontos de aluguer.

---

## **Pesquisa de Mercado**

Existem iniciativas internacionais semelhantes, como:

* **Rentbrella (Brasil)** – utiliza estações automáticas, mas exige elevados custos de infraestrutura.
* **UmbraCity (Canadá)** – focada em campus universitários, com menor escalabilidade urbana.

O **Best Umbrella** diferencia-se por não depender de máquinas.
Os **QR Codes** permitem uma implementação económica e altamente flexível.

---

## **Guiões de Teste**

### **1. Alugar um guarda-chuva**

O utilizador consulta o mapa, escolhe um ponto, lê o QR Code e inicia o aluguer automaticamente.

### **2. Devolver o guarda-chuva**

No ponto escolhido, lê novamente o QR Code e o sistema finaliza o aluguer.

### **3. Receber alerta de chuva**

A API meteorológica envia uma notificação quando a chuva se aproxima, sugerindo uma reserva imediata.

### **4. Consultar histórico**

O utilizador acede ao perfil e vê todos os alugueres com datas, locais e custos.

---


## **Enquadramento das Unidades Curriculares**

### **Programação de Dispositivos Móveis**

O desenvolvimento da aplicação Best Umbrella foi realizado em Kotlin utilizando Jetpack Compose como framework principal.
Foram aplicados conhecimentos como:

Criação de interfaces intuitivas e responsivas

Navegação entre ecrãs

Integração com APIs REST

Gestão de dados locais e estados

O objetivo foi construir uma experiência de utilizador fluida, simples e eficiente para permitir o acesso rápido às funcionalidades: mapa interativo, scanner, meteorologia, histórico e perfil.

### **Programação Orientada por Objetos**

O Back-End do Best Umbrella foi desenvolvido em Java, utilizando o framework Spring Boot, estabelecendo a ligação entre o front-end e a base de dados.
Foram implementados:

Arquitetura REST, garantindo uma API modular e simples de consumir

Padrão MVC, separando lógica, dados e apresentação

Princípios de POO, como encapsulamento, herança e modularidade

Utilização de UML para modelar o sistema

Estes conceitos permitiram uma estrutura limpa, escalável e de fácil manutenção.

 ### **Bases de Dados**

O armazenamento de dados utiliza MySQL, com uma estrutura relacional que garante integridade e segurança.
Foram utilizados conceitos como:

Tabelas e relacionamentos

Chaves primárias e estrangeiras

Consultas SQL otimizadas

Garantia de integridade referencial

A organização das entidades (como utilizadores, guarda-chuvas, alugueres, estações, casas de banho, entre outras) assegura um funcionamento eficiente do sistema.

### **Matemática Discreta**

Os conceitos de teoria de conjuntos foram aplicados na estruturação das relações entre entidades, como a ligação entre casas de banho, localizações e outras tabelas da aplicação.
Essa abordagem permitiu:

Melhor consistência nos dados

Estruturas de dados coerentes

Facilidade na modelação das relações

Contribuindo para um sistema fiável e sem ambiguidades.

Projeto de Desenvolvimento Móvel

Esta unidade curricular foi essencial para a organização e evolução do projeto.
Foram utilizadas ferramentas como:

ClickUp para organização e gestão de tarefas

Planeamento por etapas

Reuniões de acompanhamento

Feedbacks contínuos de design e funcionalidade

Aqui também foram desenvolvidas competências como trabalho em equipa, resolução de problemas e gestão do tempo.

### **Competências Comunicacionais**

A comunicação foi uma parte crucial no desenvolvimento do Best Umbrella.
Foram aplicadas técnicas de:

Apresentação clara de ideias

Comunicação entre membros da equipa

Interação com potenciais utilizadores

Coleta e análise de feedback

Estas competências garantiram que o projeto evoluísse alinhado às necessidades reais dos utilizadores




----

## **Requisitos**

### **Funcionais**

* Registo e login seguro.
* Reserva e aluguer via QR Code.
* Consulta de histórico.
* Notificações meteorológicas.
* Avaliação da experiência.

### **Não Funcionais**

* Compatibilidade com Android 10+.
* Conformidade com RGPD.
* Encriptação de dados sensíveis.
* Interface rápida, intuitiva e acessível.
* Base de dados relacional e escalável.

---

## **Mockups**

Os protótipos desenvolvidos no Figma incluem:

* Mapa interativo
* Ecrã de reserva
* Scanner de QR Code
* Histórico detalhado
* Notificações inteligentes

![Image](https://github.com/user-attachments/assets/da434d7e-8a2b-47da-880e-97e8dc918fb3)
![Image](https://github.com/user-attachments/assets/98d4cba2-d12b-4394-b8dc-d8993901a6e7)
![Image](https://github.com/user-attachments/assets/b76acc17-3db9-41ba-b754-77c04f79d93b)
![Image](https://github.com/user-attachments/assets/6a3e8cd6-cb69-46b8-97cb-bbcb7eb15fec)
![Image](https://github.com/user-attachments/assets/3e8cba96-1d2c-44bb-b266-6db6289db222)
![Image](https://github.com/user-attachments/assets/d37c8cb2-235b-468a-ae02-ef2e7021313f)

---

## **Roadmap**

* **Entrega 1 – 05/10/2025:** Ideia inicial, requisitos, mercado e mockups.
* **Entrega 2 – início 11/2025:** Protótipo funcional com autenticação, mapa e QR Code.
* **Entrega 3 – 14/12/2025:** Versão final com API de meteorologia, testes e refinamento UI/UX.
![Image](https://github.com/user-attachments/assets/00cc96c1-20a5-45b4-88e2-8662eb73a03d)
---

## **Conclusão**

O **Best Umbrella** apresenta uma solução tecnológica simples e sustentável para um problema real das cidades.
Com um modelo escalável, baixo custo de implementação e experiência fluida para o utilizador, o projeto está preparado para crescer e apoiar uma mobilidade mais inteligente e consciente.

