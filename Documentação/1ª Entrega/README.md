#  Best Umbrella  

**Projeto Mobile – Universidade Europeia / IADE**  
**Licenciatura em Engenharia Informática – 3º Semestre (2025/2026)**  

---

##  Identificação
- **Grupo:** G03  
- **Elementos:** Fábio Texeira, Feleciano Barata e Taha-Wur Pereira.   
- **Nome do Projeto:** Best Umbrella  

---

##  Descrição  

O **Best Umbrella** é uma aplicação móvel que surgiu para resolver um problema comum em cidades: ser apanhado pela chuva sem proteção e acabar por comprar um guarda-chuva descartável de baixa qualidade.  
A proposta é simples e sustentável: oferecer um serviço de **aluguer temporário de guarda-chuvas** em pontos estratégicos da cidade, como universidades, estações, cafés parceiros e centros comerciais.  

O projeto aposta num modelo inovador e acessível, sem necessidade de infraestruturas caras como máquinas automáticas. Basta um **smartphone** e um **QR Code** para aceder ao serviço.  

---

##  Objetivos  

O principal objetivo do Best Umbrella é disponibilizar uma **experiência urbana prática, sustentável e inteligente**.  
Mais do que apenas um sistema de aluguer, o projeto pretende educar os utilizadores para hábitos de consumo conscientes, diminuindo o desperdício associado a guarda-chuvas descartáveis.  

Diferente de outras soluções internacionais, a app posiciona-se como uma alternativa **flexível, de baixo custo e altamente escalável**, capaz de crescer rapidamente em qualquer cidade através de **parcerias locais**.  

Com a integração de **APIs meteorológicas**, o Best Umbrella não apenas responde a necessidades imediatas, mas também atua de forma **proativa**, enviando notificações quando a chuva está prestes a começar, incentivando a reserva antecipada.  

---

## Público-Alvo  

- **Estudantes e trabalhadores urbanos** que se deslocam frequentemente e necessitam de soluções rápidas.  
- **Turistas**, que preferem alugar em vez de comprar um guarda-chuva que dificilmente irão usar mais de uma vez.  
- **Empresas e estabelecimentos parceiros**, que podem beneficiar de visibilidade e aumento do fluxo de clientes ao oferecerem pontos de recolha/devolução.  

---

##  Pesquisa de Mercado  

Existem empresas internacionais que já exploram o conceito de partilha de guarda-chuvas:  

- **Rentbrella (Brasil):** disponibiliza guarda-chuvas através de **estações automáticas** instaladas em locais estratégicos. Embora eficiente, exige investimento elevado em infraestrutura física.  
- **UmbraCity (Canadá):** opera principalmente em **campus universitários**, funcionando como uma biblioteca de guarda-chuvas para estudantes e professores. É útil em ambientes académicos, mas pouco escalável a nível urbano.  

O **Best Umbrella** diferencia-se por não depender de máquinas ou sistemas centralizados. Em vez disso, aposta em **QR Codes em pontos parceiros**, garantindo **baixo custo de implementação, maior flexibilidade e expansão rápida**.  

---

## Guiões de Teste  

A aplicação foi pensada com diferentes cenários de utilização que simulam situações reais:  

**1. Alugar um guarda-chuva**  
O utilizador abre a aplicação, consulta o mapa e identifica um ponto de aluguer próximo. Ao chegar ao local, faz o scan do QR Code para desbloquear o guarda-chuva e o sistema regista automaticamente o início do aluguer.  

**2. Devolver um guarda-chuva**  
Depois de utilizar, o utilizador escolhe no mapa um ponto de devolução. No local, faz novamente o scan do QR Code e o sistema confirma a entrega, encerrando o aluguer.  

**3. Receber alerta de chuva**  
Com base na API meteorológica, a app envia uma notificação quando a chuva está prestes a começar. O utilizador recebe o alerta no telemóvel e pode reservar de imediato um guarda-chuva num ponto próximo.  

**4. Consultar histórico de alugueres**  
O utilizador pode aceder ao seu perfil e verificar todos os alugueres efetuados, incluindo datas, locais de recolha e devolução, e custos associados.  

---

##  Requisitos  

**Funcionais**  
O sistema deve permitir que o utilizador se registe e faça login de forma segura, que reserve um guarda-chuva antecipadamente, que inicie e encerre um aluguer através da leitura de QR Codes e que consulte o histórico de utilizações. Além disso, a aplicação deve enviar notificações meteorológicas e possibilitar a avaliação da experiência em cada ponto parceiro.  

**Não Funcionais**  
A aplicação deve ser compatível com Android 10 ou superior, garantir conformidade com o RGPD e proteger todos os dados pessoais através de encriptação. A base de dados deverá ser relacional, segura e escalável, e a interface deve apresentar uma navegação intuitiva, rápida e acessível para qualquer utilizador.  

---

##  Modelo de Domínio  

O modelo inicial é composto por quatro entidades principais que estruturam o sistema:  
- O **Utilizador**, que possui identificação única, nome, email, password encriptada e um rating associado.  
- O **GuardaChuva**, identificado por um estado e localização, sendo registado em diferentes pontos da cidade.  
- O **Aluguer**, que guarda toda a informação relativa ao processo, como as datas de início e fim, custo e associação ao utilizador.  
- O **Ponto de Aluguer**, que representa o local físico e o parceiro responsável pelo guarda-chuva.  

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



##  Mockups  

Os primeiros protótipos desenvolvidos no Figma apresentam:  
- Um **mapa interativo** com os pontos de aluguer disponíveis.  
- Um **ecrã de reserva** que confirma a disponibilidade e o tempo de utilização.  
- Um **scanner de QR Code** integrado na aplicação, que simplifica o processo de recolha e devolução.  
- Um **histórico de alugueres** com detalhes sobre datas, locais e custos.  
- Um **sistema de notificações**, que alerta para chuva iminente ou promoções de parceiros.

  
![Image](https://github.com/user-attachments/assets/3540fd9e-6439-4426-8761-bca5a1cc97b9)    ![Image](https://github.com/user-attachments/assets/575034e8-ab35-4604-b1c1-d3ffe0d3069a)
![Image](https://github.com/user-attachments/assets/8b8f09c0-174b-4433-b0c9-070ae1b1d430)   ![Image](https://github.com/user-attachments/assets/71af7c74-99e9-4438-be99-0439fd21a5f2)
 

---


## Roadmap  

- **Entrega 1 (05 de outubro 2025):** Ideia do projeto, pesquisa de mercado, definição de requisitos e mockups iniciais.  
- **Entrega 2 (início de novembro 2025):** Protótipo funcional com autenticação, mapa interativo, sistema de reservas e integração do QR Code.  
- **Entrega 3 (até 14 de dezembro 2025):** Versão final da aplicação com API meteorológica integrada, testes completos, refinamento da interface e apresentação final.  

![Image](https://github.com/user-attachments/assets/00cc96c1-20a5-45b4-88e2-8662eb73a03d)
---

##  Conclusão  

O **Best Umbrella** é mais do que uma simples aplicação de aluguer é uma resposta tecnológica a um problema urbano real. O projeto alia **sustentabilidade, inovação e conveniência**, oferecendo uma alternativa prática e responsável para os utilizadores.  

Com um modelo escalável, sem dependência de infraestruturas complexas, o Best Umbrella está preparado para crescer em diferentes cidades, promovendo **mobilidade urbana sustentável** e incentivando o consumo consciente.  

---








