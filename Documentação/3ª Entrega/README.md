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
##  Modelo de Domínio  

 modelo inicial é composto por quatro entidades principais que estruturam o sistema:  
- **Utilizador**, que possui identificação única, nome, email, password encriptada e um rating associado.  
- **GuardaChuva**, identificado por um estado e localização, sendo registado em diferentes pontos da cidade.  
- **Aluguer**, que guarda toda a informação relativa ao processo, como as datas de início e fim, custo e associação ao utilizador.  
-  **Ponto de Aluguer**, que representa o local físico e o parceiro responsável pelo guarda-chuva.  

Este modelo poderá ser expandido com novas entidades, como notificações personalizadas e sistema de fidelização.  

---

## Modelo de Pagamento  

Para garantir uma experiência simples, rápida e segura, o **Best Umbrella** aposta em métodos de pagamento digitais amplamente utilizados e fáceis de integrar.  

### Método de Pagamento Disponível  
- **PayPal** → opção internacionalmente reconhecida, ideal para turistas e utilizadores que preferem não utilizar cartões locais.  

### Modelos de Utilização  
- **Pay-per-use (pagar por utilização):** o utilizador paga apenas pelo tempo de utilização do guarda-chuva (ex.: 1€ por 24h).  
- **Depósito reembolsável:** um valor de caução (ex.: 5€) é bloqueado no momento do aluguer e libertado assim que o guarda-chuva é devolvido corretamente. Caso não haja devolução, o depósito cobre o custo de reposição.  

- **Campanhas promocionais:** descontos e primeiros minutos grátis, em colaboração com parceiros locais (universidades, cafés, centros comerciais).  

### Depósito de Segurança  
O **depósito reembolsável** é o elemento-chave do sistema:  
1. O valor da caução é bloqueado no PayPal no momento da reserva.  
2. O utilizador recolhe o guarda-chuva e utiliza-o normalmente.  
3. Quando faz a devolução via QR Code, o sistema liberta automaticamente o depósito.  
4. Se não devolver no prazo definido, o valor é cobrado como penalização, garantindo sustentabilidade ao serviço.  

Este método cria um equilíbrio entre confiança no utilizador e proteção da infraestrutura, tornando o sistema justo e eficiente.  

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

---
### **Programação Orientada por Objetos**

O Back-End do Best Umbrella foi desenvolvido em Java, utilizando o framework Spring Boot, estabelecendo a ligação entre o front-end e a base de dados.
Foram implementados:

Arquitetura REST, garantindo uma API modular e simples de consumir

Padrão MVC, separando lógica, dados e apresentação

Princípios de POO, como encapsulamento, herança e modularidade

Utilização de UML para modelar o sistema

Estes conceitos permitiram uma estrutura limpa, escalável e de fácil manutenção.

---
 ### **Bases de Dados**

O armazenamento de dados utiliza MySQL, com uma estrutura relacional que garante integridade e segurança.
Foram utilizados conceitos como:

Tabelas e relacionamentos

Chaves primárias e estrangeiras

Consultas SQL otimizadas

Garantia de integridade referencial

A organização das entidades (como utilizadores, guarda-chuvas, alugueres, estações, casas de banho, entre outras) assegura um funcionamento eficiente do sistema.

---
### **Matemática Discreta**

Os conceitos de teoria de conjuntos foram aplicados na estruturação das relações entre entidades, como a ligação entre casas de banho, localizações e outras tabelas da aplicação.
Essa abordagem permitiu:

Melhor consistência nos dados

Estruturas de dados coerentes

Facilidade na modelação das relações

Contribuindo para um sistema fiável e sem ambiguidades.

### **Projeto de Desenvolvimento Móvel**

Esta unidade curricular foi essencial para a organização e evolução do projeto.
Foram utilizadas ferramentas como:

ClickUp para organização e gestão de tarefas

Planeamento por etapas

Reuniões de acompanhamento

Feedbacks contínuos de design e funcionalidade

Aqui também foram desenvolvidas competências como trabalho em equipa, resolução de problemas e gestão do tempo.

---
### **Competências Comunicacionais**

A comunicação foi uma parte crucial no desenvolvimento do Best Umbrella.
Foram aplicadas técnicas de:

Apresentação clara de ideias

Comunicação entre membros da equipa

Interação com potenciais utilizadores

Coleta e análise de feedback

Estas competências garantiram que o projeto evoluísse alinhado às necessidades reais dos utilizadores

----
## Documentação da API REST 

A API REST do Best Umbrella é responsável pela comunicação entre a aplicação mobile, o servidor Back-End (Spring Boot) e a base de dados MySQL.
Gere dados relacionados com utilizadores, pontos de aluguer, guarda-chuvas, alugueres, notificações e histórico de utilização.

 **Base URL da API**
https://api.bestumbrella.pt/api/v1

### 1. Endpoints de Utilizador
- Criar Utilizador
    - `POST /api/auth/register`
    - Request
      {
      "nome": "João Silva",
      "email": "joao@gmail.com",
      "password": "1234",
      "telefone": "910000000"
      }
    - Response (exemplo)
      {
      "utilizadorId": 1,
      "nome": "João Silva",
      "email": "joao@gmail.com",
      "telefone": "910000000",
      "dataRegisto": "2025-11-24T10:15:30",
      "rating": 4.7,
      "alugueres": [],
      "notificacoes": [],
      "alertaChuvaAtivo": false,
      "alertaCidade": null,
      "alertaLat": null,
      "alertaLon": null
      }
- Login
    - `POST /api/auth/login`
    - Request
      {
      "email": "joao@gmail.com",
      "password": "1234"
      }
    - Response (exemplo)
      {
      "utilizadorId": 1,
      "nome": "João Silva",
      "email": "joao@gmail.com",
      "telefone": "910000000",
      "dataRegisto": "2025-11-21T09:00:00",
      "rating": 4.7,
      "alugueres": [
      {
      "aluguerId": 20,
      "dataInicio": "2025-11-20T12:30:00",
      "dataFim": null,
      "custo": 0.0,
      "estado": null,
      "guardaChuvaId": 5,
      "pontoInicioId": 2,
      "pontoFimId": null
      }
      ],
      "notificacoes": [],
      "alertaChuvaAtivo": true,
      "alertaCidade": "Lisboa",
      "alertaLat": null,
      "alertaLon": null
      }
- Obter Perfil
    - `GET /api/utilizadores/{id}`
    - Response (exemplo)
      {
      "utilizadorId": 1,
      "nome": "João Silva",
      "email": "joao@gmail.com",
      "telefone": "910000000",
      "dataRegisto": "2025-10-01T08:00:00",
      "rating": 4.9,
      "alugueres": [
      {
      "aluguerId": 18,
      "dataInicio": "2025-11-10T09:00:00",
      "dataFim": "2025-11-10T10:15:00",
      "custo": 0.0,
      "estado": null,
      "guardaChuvaId": 4,
      "pontoInicioId": 1,
      "pontoFimId": 3
      }
      ],
      "notificacoes": [
      {
      "notificacaoId": 7,
      "mensagem": "Chuva prevista às 15h",
      "tipo": "ALERTA_CHUVA",
      "dataEnvio": "2025-11-20T13:00:00",
      "estado": "ENTREGUE"
      }
      ],
      "alertaChuvaAtivo": true,
      "alertaCidade": "Lisboa",
      "alertaLat": null,
      "alertaLon": null
      }

---
### 2. Endpoints de Guarda-Chuva
- Listar guarda-chuvas
    - `GET /api/guardachuvas`
    - Suporta filtro opcional: `GET /api/guardachuvas?estado=DISPONIVEL`
    - Response (exemplo)
      [
      {
      "guardaChuvaId": 5,
      "codigoQr": "GC-00005",
      "corId": 2,
      "tipoId": 1,
      "dataRegisto": "2025-09-15T11:20:00",
      "pontoId": 2,
      "alugueres": []
      }
      ]
- Detalhes de um guarda-chuva
    - `GET /api/guardachuvas/{id}`
    - Response (exemplo)
      {
      "guardaChuvaId": 5,
      "codigoQr": "GC-00005",
      "corId": 2,
      "tipoId": 1,
      "dataRegisto": "2025-09-15T11:20:00",
      "pontoId": 2,
      "alugueres": [
      {
      "aluguerId": 20,
      "dataInicio": "2025-11-20T12:30:00",
      "dataFim": null,
      "custo": 0.0,
      "estado": null,
      "guardaChuvaId": 5,
      "pontoInicioId": 2,
      "pontoFimId": null
      }
      ]
      }
- Obter por código QR
    - `GET /api/guardachuvas/codigo/{codigoQr}`
    - QR PNG: `GET /api/guardachuvas/codigo/{codigoQr}/qrcode?size=256`
    - Response `qrcode` (PNG)
        - `Content-Type: image/png` com o QR gerado
- Criar/Atualizar
    - `POST /api/guardachuvas`
    - `PUT /api/guardachuvas/{id}`

---
### 3. Endpoints de Pontos de Aluguer
- Listar todos os pontos
    - `GET /api/pontos-de-aluguer`
    - Response (exemplo)
      [
      {
      "pontoId": 10,
      "nome": "Oriente Green Campus",
      "latitude": 38.768,
      "longitude": -9.100,
      "capacidade": 20,
      "tipo": "CAMPUS",
      "quantidade": 6
      }
      ]
- Detalhes de um ponto
    - `GET /api/pontos-de-aluguer/{id}`
    - Response (exemplo)
      {
      "pontoId": 10,
      "nome": "Oriente Green Campus",
      "latitude": 38.768,
      "longitude": -9.100,
      "capacidade": 20,
      "tipo": "CAMPUS",
      "quantidade": 6
      }

---
### 4. Endpoints de Aluguer
- Iniciar aluguer
    - `POST /api/alugueres/start`
    - Parâmetros: `utilizadorId`, `guardaChuvaId`, `pontoInicioId`
    - Response (exemplo)
      {
      "aluguerId": 20,
      "utilizadorId": 1,
      "guardaChuvaId": 5,
      "pontoInicioId": 2,
      "pontoFimId": null,
      "dataInicio": "2025-11-20T12:30:00",
      "dataFim": null,
      "custo": 0.10,
      "estado": null
      }
- Iniciar via QR Code
    - `POST /api/alugueres/start-by-qr`
    - Parâmetros: `utilizadorId`, `codigoQr` (ou `qr`), `pontoInicioId`
    - Response (exemplo)
      {
      "aluguerId": 21,
      "utilizadorId": 1,
      "guardaChuvaId": 6,
      "pontoInicioId": 2,
      "pontoFimId": null,
      "dataInicio": "2025-11-24T12:00:00",
      "dataFim": null,
      "custo": 0.0,
      "estado": null
      }
- Finalizar aluguer
    - `POST /api/alugueres/{aluguerId}/end`
    - Parâmetros: `pontoFimId`
    - Response (exemplo)
      {
      "aluguerId": 20,
      "utilizadorId": 1,
      "guardaChuvaId": 5,
      "pontoInicioId": 2,
      "pontoFimId": 3,
      "dataInicio": "2025-11-20T12:30:00",
      "dataFim": "2025-11-20T13:00:00",
      "custo": 0.0,
      "estado": null
      }

---
### 5. Endpoints de Notificações
- Alerta de chuva (gera notificação automática)
    - `POST /api/notificacoes/alerta-chuva`
    - Parâmetros: `utilizadorId` e localização (`city` ou `lat`/`lon`)
    - Response (exemplo)
      {
      "notificacaoId": 12,
      "mensagem": "Chuva prevista às 15h. Prepare o seu guarda-chuva!",
      "tipo": "ALERTA_CHUVA",
      "dataEnvio": "2025-11-24T10:30:00",
      "estado": "ENTREGUE"
      }
    - Caso não haja chuva: `"Sem chuva no local indicado. Nada a notificar."`
- Stream de notificações (SSE)
    - `GET /api/notificacoes/stream?utilizadorId={id}`
    - Exemplo de evento SSE
      data: {"notificacaoId":13,"mensagem":"Chuva moderada em 30min","tipo":"ALERTA_CHUVA","dataEnvio":"2025-11-24T11:00:00","estado":"ENTREGUE"}
- Notificações do utilizador
    - Incluídas em `GET /api/utilizadores/{id}` no campo `notificacoes`

---
### 6. Estrutura das Respostas da API
- As respostas devolvem DTOs diretamente, com códigos HTTP apropriados (`200`, `201`, `400`, `401`, `404`).
- Não há wrapper padrão `status/data/timestamp`.

---
### 7. Autenticação
- Neste modo (beta/demonstração), os endpoints estão abertos e não requerem autenticação.
- Não é necessário enviar `Authorization: Bearer ...`.

---
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

Os protótipos desenvolvidos incluem:

* Mapa interativo
* Ecrã de reserva
* Scanner de QR Code
* Histórico detalhado
* Perfil
<img width="2245" height="1587" alt="Image" src="https://github.com/user-attachments/assets/a7728921-c320-4aa5-9044-bde23f4a7e09" />
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

