Aqui está o **README.md completo e final**, já com a secção de **Base de Dados atualizada** com base no teu script.
Está totalmente pronto para **copiar e colar** 👉

---

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

# **Base de Dados**

A base de dados do **Best Umbrella** foi implementada em **MySQL**, organizada segundo um modelo relacional sólido que garante integridade, desempenho e suporte completo às funcionalidades da aplicação.
O sistema inclui gestão de utilizadores, guarda-chuvas, localização, alugueres, multas, notificações e histórico de eventos.

---

## **Entidades Principais**

### 🟦 **1. Utilizador** (`utilizador`)

Armazena informações dos utilizadores:

* Nome, email, password, telefone
* Data de registo
* Rating

**Relacionamentos:**

* Alugueres (UGEM)
* Multas
* Notificações

---

### 🟧 **2. Guarda-Chuva** (`guardachuva`)

Cada guarda-chuva contém:

* Número identificador
* Data de registo
* Cor (FK → `cor`)
* Tipo (FK → `tipo`)

**Relacionado com:**

* Movimentos entre estações (GE)
* Alugueres (UGEM)

---

### 🟥 **3. Localização**

Sistema geográfico dividido em três níveis:

* **Cidade** (`cidade`)
* **Zona** (`zona`, FK → cidade)
* **Estação** (`estacao`, FK → zona)

  * Nome, latitude, longitude, capacidade

---

### 🟩 **4. Movimentação de Guarda-Chuvas — GE** (`ge`)

Regista entrada e saída de cada guarda-chuva em cada estação:

* Data de entrada
* Data de saída
* Guarda-chuva (FK)
* Estação (FK)

---

### 🟨 **5. Aluguer — UGEM** (`ugem`)

Regista o aluguer feito pelo utilizador:

* Data de início
* Data de fim
* Guarda-chuva (FK)
* Utilizador (FK)

---

### 🟥 **6. Multas — `multa`**

Inclui:

* Utilizador (FK)
* Data de emissão e vencimento
* Motivo
* Valor

---

### 🟪 **7. Ligação entre Aluguer e Multa — MUGEM** (`mugem`)

Liga cada multa ao aluguer que a originou.

---

### 🟫 **8. Histórico de Eventos — UGEME** (`ugeme`)

Regista eventos ocorridos durante o aluguer:

* Estado
* Tipo de evento (início, fim, multa aplicada, etc.)
* Data/hora (timestamp automático)

---

### 🟦 **9. Estado do Guarda-Chuva — `estado`**

Regista estados associados a eventos específicos (FK → `ugeme`).

---

### 🟪 **10. Notificações — `notificacao`**

Representa notificações enviadas ao utilizador, como:

* Alertas meteorológicos
* Confirmações
* Avisos

Contém mensagem, timestamp e FK → utilizador.

---

## **Resumo do Modelo Relacional**

| Categoria                | Tabelas                 |
| ------------------------ | ----------------------- |
| Localização              | cidade, zona, estacao   |
| Guarda-chuva             | guardachuva, cor, tipo  |
| Movimentos               | ge                      |
| Utilizadores e Alugueres | utilizador, ugem, ugeme |
| Multas                   | multa, mugem            |
| Notificações             | notificacao             |
| Estado                   | estado                  |

O modelo implementa todas as foreign keys necessárias para manter integridade referencial entre as entidades.

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

---

Se quiseres, posso também gerar:
📄 **versão PDF**, 🎞️ **PowerPoint**, 🖼️ **diagrama ER**, 🌐 **versão para GitHub com badges**.
É só pedir!
