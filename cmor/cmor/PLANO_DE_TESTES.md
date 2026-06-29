# PLANO DE TESTES — CMOR
## Calculadora de Materiais para Obra Residencial

---

## 1. Objetivo do Plano de Testes

Validar que todos os componentes da aplicação CMOR funcionam corretamente, incluindo:
- A interface Jakarta Faces (JSF) responsiva
- Os serviços REST de cálculo de concreto e tijolos
- A persistência de dados no banco H2
- A busca e armazenamento de orçamentos

---

## 2. Escopo dos Testes

### Funcionalidades a Testar

| # | Funcionalidade | Tipo |
|---|---|---|
| T1 | Acesso à página inicial | Interface |
| T2 | Formulário de cálculo de concreto | Interface |
| T3 | Formulário de cálculo de tijolos | Interface |
| T4 | Adição e remoção de paredes | Interface |
| T5 | Cálculo e persistência de orçamento | Integração |
| T6 | Busca de orçamento por número | Funcional |
| T7 | Busca de orçamento por nome do cliente | Funcional |
| T8 | Endpoint REST /api/fundacao/concreto | API |
| T9 | Endpoint REST /api/paredes/tijolos | API |
| T10 | Validação de dados de entrada | Funcional |

---

## 3. Ambiente de Testes

- **Sistema Operacional:** Windows 10
- **JDK:** Java 23
- **IDE:** IntelliJ IDEA 2026.1.1
- **Framework:** Spring Boot 3.2.5
- **Banco de Dados:** H2 em memória
- **Servidor Web:** Tomcat embutido (porta 8080)
- **Navegador:** Google Chrome / Firefox

---

## 4. Casos de Teste

### **T1 — Acesso à Página Inicial**

| Campo | Descrição |
|---|---|
| **ID** | T1 |
| **Objetivo** | Validar que a página inicial carrega quando o servidor está ativo |
| **Pré-requisitos** | Servidor rodando em http://localhost:8080/orcamentos.xhtml |
| **Passos** | 1. Abrir navegador<br>2. Acessar http://localhost:8080/orcamentos.xhtml<br>3. Aguardar carregamento |
| **Resultado Esperado** | Página index.xhtml carrega com formulário de orçamento |
| **Status** | ✅ PASSOU |
| **Data** | 28/06/2026 |
| **Observações** | Redirecionamento automático para index.xhtml funcionando |

---

### **T2 — Formulário de Cálculo de Concreto**

| Campo | Descrição |
|---|---|
| **ID** | T2 |
| **Objetivo** | Validar que o usuário consegue preencher e submeter dados de viga baldrame |
| **Pré-requisitos** | Estar na página inicial |
| **Passos** | 1. Preencher "Nome do cliente": "João Silva"<br>2. Preencher "Altura da viga": 0.4<br>3. Adicionar 4 paredes com dados:<br>   - P1: esp=0.15m, comp=4.0m, alt=2.8m<br>   - P2: esp=0.15m, comp=3.5m, alt=2.8m<br>   - P3: esp=0.15m, comp=4.0m, alt=2.8m<br>   - P4: esp=0.15m, comp=3.5m, alt=2.8m<br>4. Clicar em "Calcular e Gerar Orçamento" |
| **Resultado Esperado** | Sistema calcula volume total = 0.900 m³ |
| **Status** | ✅ PASSOU |
| **Data** | 28/06/2026 |
| **Observações** | Fórmula: 0.15 × 0.4 × (4.0+3.5+4.0+3.5) = 0.900 m³ |

---

### **T3 — Formulário de Cálculo de Tijolos**

| Campo | Descrição |
|---|---|
| **ID** | T3 |
| **Objetivo** | Validar que o sistema calcula quantidade de tijolos descontando aberturas |
| **Pré-requisitos** | Estar na página de tijolos |
| **Passos** | 1. Preencher dimensões do tijolo: 0.19 × 0.057 × 0.09m<br>2. Definir perda: 10%<br>3. Adicionar 4 paredes (mesmas de T2)<br>4. P1 com janela: 1.2m × 1.5m<br>5. P2 com porta: 2.1m × 0.9m<br>6. Clicar em calcular |
| **Resultado Esperado** | Sistema calcula 3.893 tijolos (com 10% de perda) |
| **Status** | ✅ PASSOU |
| **Data** | 28/06/2026 |
| **Observações** | Desconto de aberturas funcionando corretamente |

---

### **T4 — Adição e Remoção de Paredes**

| Campo | Descrição |
|---|---|
| **ID** | T4 |
| **Objetivo** | Validar adição e remoção dinâmica de paredes no formulário |
| **Pré-requisitos** | Estar no formulário |
| **Passos** | 1. Clicar "Adicionar parede" 3 vezes<br>2. Verificar que aparecem P1, P2, P3, P4<br>3. Clicar "×" para remover P3<br>4. Verificar que ficam P1, P2, P4 |
| **Resultado Esperado** | Interface atualiza dinamicamente |
| **Status** | ✅ PASSOU |
| **Data** | 28/06/2026 |
| **Observações** | JSF h:dataTable com h:commandButton funcionando |

---

### **T5 — Cálculo e Persistência de Orçamento**

| Campo | Descrição |
|---|---|
| **ID** | T5 |
| **Objetivo** | Validar que orçamento é salvo no banco de dados |
| **Pré-requisitos** | Realizar T2 com sucesso |
| **Passos** | 1. Após cálculo, anotar número do orçamento (ex: ORC-1782662000000)<br>2. Acessar http://localhost:8080/h2-console<br>3. Executar: SELECT * FROM ORCAMENTO<br>4. Verificar registro com os dados corretos |
| **Resultado Esperado** | Banco H2 contém novo registro com volume=0.900, cliente="João Silva" |
| **Status** | ✅ PASSOU |
| **Data** | 28/06/2026 |
| **Observações** | @PrePersist gera número único automaticamente |

---

### **T6 — Busca por Número do Orçamento**

| Campo | Descrição |
|---|---|
| **ID** | T6 |
| **Objetivo** | Validar busca de orçamento exato por número |
| **Pré-requisitos** | Ter orçamento salvo (T5) |
| **Passos** | 1. Acessar http://localhost:8080/orcamentos.xhtml<br>2. Digitar número do orçamento no campo de busca<br>3. Clicar "Buscar"<br>4. Verificar resultado |
| **Resultado Esperado** | Orçamento aparecer na tabela com dados corretos |
| **Status** | ✅ PASSOU |
| **Data** | 28/06/2026 |
| **Observações** | OrcamentoRepository.findByNumeroOrcamento() funciona |

---

### **T7 — Busca por Nome do Cliente**

| Campo | Descrição |
|---|---|
| **ID** | T7 |
| **Objetivo** | Validar busca contendo nome do cliente (case-insensitive) |
| **Pré-requisitos** | Ter orçamentos salvos com nomes diferentes |
| **Passos** | 1. Buscar por: "João"<br>2. Verificar que aparecem todos com nome contendo "João"<br>3. Buscar por: "silva" (minúscula)<br>4. Verificar que encontra mesmo assim |
| **Resultado Esperado** | Busca retorna todos os registros com "João Silva" |
| **Status** | ✅ PASSOU |
| **Data** | 28/06/2026 |
| **Observações** | Ignorecase=true no findByNomeClienteContaining() |

---

### **T8 — Endpoint REST /api/fundacao/concreto**

| Campo | Descrição |
|---|---|
| **ID** | T8 |
| **Objetivo** | Validar API REST de cálculo de concreto via POST |
| **Pré-requisitos** | Servidor rodando |
| **Passos** | 1. Abrir Postman<br>2. POST http://localhost:8080/api/fundacao/concreto<br>3. Body (JSON):<br>```json<br>{"alturaViga":0.4,"arestas":[{"nome":"a1","espessura":0.15,"comprimento":4,"altura":2.8}]}<br>```<br>4. Enviar |
| **Resultado Esperado** | HTTP 200 com volumeTotalM3: 0.24 |
| **Status** | ✅ PASSOU |
| **Data** | 28/06/2026 |
| **Observações** | Resposta formatada com detalhesPorAresta |

---

### **T9 — Endpoint REST /api/paredes/tijolos**

| Campo | Descrição |
|---|---|
| **ID** | T9 |
| **Objetivo** | Validar API REST de cálculo de tijolos via POST |
| **Pré-requisitos** | Servidor rodando |
| **Passos** | 1. Abrir Postman<br>2. POST http://localhost:8080/api/paredes/tijolos<br>3. Body (JSON com janela):<br>```json<br>{"alturaTijolo":0.057,"larguraTijolo":0.09,"comprimentoTijolo":0.19,"percentualPerda":0.10,"arestas":[{"nome":"a1","comprimento":4,"altura":2.8,"temJanela":true,"alturaJanela":1.2,"comprimentoJanela":1.5}]}<br>```<br>4. Enviar |
| **Resultado Esperado** | HTTP 200 com quantidadeTotalTijolos > 0, desconto de abertura aplicado |
| **Status** | ✅ PASSOU |
| **Data** | 28/06/2026 |
| **Observações** | Abertura de 1.8m² descontada corretamente |

---

### **T10 — Validação de Dados de Entrada**

| Campo | Descrição |
|---|---|
| **ID** | T10 |
| **Objetivo** | Validar que dados inválidos retornam erro apropriado |
| **Pré-requisitos** | Estar no formulário |
| **Passos** | 1. Deixar "Nome do cliente" vazio<br>2. Clicar "Calcular"<br>3. Verificar mensagem de erro<br>4. Preencher com nome válido<br>5. Deixar "Comprimento" vazio numa parede<br>6. Clicar "Calcular"<br>7. Verificar que não deixa calcular |
| **Resultado Esperado** | Mensagens de validação claras em vermelho |
| **Status** | ✅ PASSOU |
| **Data** | 28/06/2026 |
| **Observações** | @NotEmpty, @Positive anotações funcionando |

---

## 5. Resultados Consolidados

### Resumo

| Categoria | Total | Passaram | Falharam |
|---|---|---|---|
| Interface (JSF) | 5 | 5 | 0 |
| Integração | 1 | 1 | 0 |
| Funcional | 3 | 3 | 0 |
| API (REST) | 2 | 2 | 0 |
| **TOTAL** | **10** | **10** | **0** |

### Taxa de Sucesso
**100% (10/10 testes passaram)**

---

## 7. Conclusão

✅ **A aplicação CMOR foi testada com sucesso em todas as etapas.**

Todos os requisitos funcionais foram validados:
- ✅ Interface JSF responsiva e intuitiva
- ✅ Cálculos de concreto e tijolos precisos
- ✅ Persistência de dados no banco H2
- ✅ APIs REST disponíveis para integração
- ✅ Busca de orçamentos funcionando

**Recomendação:** Aplicação pronta para entrega ao professor.

---

**Executado por:** Guilherme Ribeiro de Paula  
**Data:** 29 de junho de 2026  
**Ambiente:** Windows 10 + IntelliJ IDEA 2026.1.1

