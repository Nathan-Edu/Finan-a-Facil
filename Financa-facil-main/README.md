# Finança Fácil – WebApp de Controle Financeiro Pessoal

**Entrega da Versão 1**  
Projeto desenvolvido pelos alunos **Juliana Ransani** e **Nathan Alves**  
Disciplina: Desenvolvimento de Aplicações Web – IFPR

---

## 📌 Objetivo

O projeto **Finança Fácil** é uma plataforma Web desenvolvida com Java e Spring Boot, cujo objetivo é facilitar o controle financeiro pessoal. Esta versão entrega a **parte negocial do sistema**, com todas as regras de negócio implementadas e devidamente testadas com testes unitários automatizados.

---

## ⚙️ Funcionalidades Implementadas

- ✅ Cadastro e controle de **despesas** com categorias.
- ✅ Registro de **receitas** e valores recebidos.
- ✅ Gestão de **contas parceladas**, com geração automática de parcelas.
- ✅ Controle de **parcelas**, incluindo vencimento e status de pagamento.
- ✅ Geração e visualização de **alertas** para vencimento de parcelas.
- ✅ Sistema de **login de usuário** com autenticação simples.
- ✅ **Testes unitários** para todas as regras de negócio com cobertura de múltiplos casos.

---

## 🛠️ Tecnologias e Ferramentas

- **Java 24**
- **Spring Boot 3.4.5**
- **Spring Web** – criação da API REST
- **Spring Data JPA** – persistência com Hibernate
- **H2 Database** – banco de dados em memória para desenvolvimento e testes
- **Lombok** – para reduzir código boilerplate
- **JUnit 5** – para testes unitários
- **Mockito** – para simulação de dependências em testes
- **Maven** – gerenciamento de dependências e build

---

## 🔗 Principais Endpoints da API

### 🧾 Despesas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST   | `/despesas` | Cria uma nova despesa |
| GET    | `/despesas` | Lista todas as despesas |
| GET    | `/despesas/{id}` | Busca despesa por ID |
| PUT    | `/despesas/{id}` | Atualiza uma despesa |
| DELETE | `/despesas/{id}` | Remove uma despesa |
| GET    | `/despesas/categoria/{categoria}` | Filtra por categoria (ex: MORADIA) |

### 💰 Receitas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST   | `/receitas` | Cria uma nova receita |
| GET    | `/receitas` | Lista todas as receitas |
| GET    | `/receitas/{id}` | Busca receita por ID |
| PUT    | `/receitas/{id}` | Atualiza uma receita |
| DELETE | `/receitas/{id}` | Remove uma receita |

### 📦 Contas Parceladas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST   | `/contas-parceladas` | Cria uma nova conta parcelada |
| GET    | `/contas-parceladas` | Lista todas |
| GET    | `/contas-parceladas/{id}` | Busca por ID |
| PUT    | `/contas-parceladas/{id}` | Atualiza conta |
| DELETE | `/contas-parceladas/{id}` | Remove conta |

### 📆 Parcelas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| GET    | `/parcelas` | Lista todas as parcelas |
| GET    | `/parcelas/{id}` | Busca parcela por ID |
| PUT    | `/parcelas/{id}/pagar` | Marca como paga |
| GET    | `/parcelas/conta/{contaId}` | Lista parcelas de uma conta específica |

### ⚠️ Alertas
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST   | `/alertas/{usuarioId}` | Cria alerta manual |
| GET    | `/alertas/usuario/{usuarioId}` | Lista alertas por usuário |
| PUT    | `/alertas/{id}/visualizar` | Marca alerta como visualizado |

### 👤 Usuários
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST   | `/usuarios/login` | Login com email e senha |
| POST   | `/usuarios/cadastro` | Cadastro de novo usuário |

---

## 🧪 Testes

- **JUnit 5 + Mockito**
- Testes cobrem os principais serviços:
  - `DespesaService`
  - `ReceitaService`
  - `ContaParceladaService`
  - `ParcelaService`
  - `AlertaService`
  - `UsuarioService`

---

## 📬 Contato

Caso queira mais informações, fale com os autores:

- Juliana Ransani  
- Nathan Alves
