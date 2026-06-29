# CMOR — Calculadora de Materiais para Obra Residencial

> Projeto acadêmico desenvolvido para a disciplina **Desenvolvimento de Sistemas** do curso de Ciência da Computação — **UniCEUB**.

Aplicação web desenvolvida em **Java + Spring Boot + Jakarta Faces** que calcula o consumo de materiais em fases de obras residenciais a partir de parâmetros fornecidos pelo usuário e fórmulas da engenharia civil.

---

## Índice

- [Tecnologias](#tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Como executar](#como-executar)
- [Interface web](#interface-web)
- [Funcionalidades](#funcionalidades)
- [Modelagem da Planta Baixa](#modelagem-da-planta-baixa)
- [Volume de Concreto](#volume-de-concreto-na-fundação)
- [Quantidade de Tijolos](#quantidade-de-tijolos)
- [Orçamentos](#orçamentos)
- [Banco de dados H2](#banco-de-dados-h2)
- [API REST](#api-rest)
- [Decisões de design](#decisões-de-design)

---

## Sobre o projeto

Empresas de engenharia precisam lidar constantemente com dimensionamento de materiais e fornecer orçamentos baseados em previsão de custos. O **CMOR** é uma aplicação web completa que calcula o consumo de materiais em determinadas fases de obras residenciais e permite salvar, buscar e gerenciar orçamentos em banco de dados.

A planta baixa da casa é modelada como um **grafo G=(V,A)**, onde:
- os **vértices** representam os encontros de paredes (que receberão pilares estruturais)
- as **arestas** representam as paredes, com espessura, altura, comprimento e indicação de aberturas (janelas e portas)

---

## Tecnologias

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 23 | Linguagem principal |
| Spring Boot | 3.2.5 | Framework web e configuração |
| Jakarta Faces (JSF) | 4.0.6 | Framework de interface web (views `.xhtml`) |
| Mojarra | 4.0.6 | Implementação do Jakarta Faces |
| JoinFaces | 5.3.0 | Integração JSF + Spring Boot |
| Weld | 5.1.2 | Implementação CDI (injeção de dependências JSF) |
| Spring Data JPA | 3.2.5 | ORM e persistência |
| Hibernate | 6.4.4 | Implementação JPA |
| H2 Database | 2.2.224 | Banco de dados em arquivo |
| Bean Validation | 3.0 | Validação dos dados de entrada |
| Maven | 3.x | Gerenciamento de dependências |

---

## Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

- [JDK 17+](https://adoptium.net/) (o projeto foi testado com JDK 23)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- [Postman](https://www.postman.com/downloads/) para testar os endpoints REST diretamente (opcional)

---

## Como executar

1. Clone ou baixe o repositório e abra a pasta `cmor` no IntelliJ
2. Aguarde o Maven baixar as dependências automaticamente
3. Localize o arquivo `CmorApplication.java` em `src/main/java/com/cmor/`
4. Clique no botão **▶ Run** ao lado da classe `main`
5. Aguarde a mensagem no console:

```
Tomcat started on port 8080 (http)
Started CmorApplication in X seconds
```

6. Acesse `http://localhost:8080/concreto.xhtml` no navegador — a interface da calculadora abrirá automaticamente


---

## Interface web

A interface é construída com **Jakarta Faces (JSF)** e servida automaticamente em `http://localhost:8080/concreto.xhtml`. Possui três abas:

- **Concreto** — calcula o volume de concreto para vigas baldrame
- **Tijolos** — calcula a quantidade de tijolos para assentamento de paredes
- **Orçamentos** — salva, busca, edita e exclui orçamentos no banco de dados

Os arquivos de view estão em:

```
src/main/resources/META-INF/resources/
├── concreto.xhtml
├── tijolos.xhtml
├── orcamentos.xhtml
└── resources/
    └── css/
        └── style.css
```

---

## Funcionalidades

### Calculadora de Concreto
- Inserção de múltiplas paredes com nome, espessura, comprimento e altura
- Suporte a janelas e portas por parede (com toggle dinâmico via AJAX)
- Adição e remoção de paredes dinamicamente
- Cálculo do volume total e detalhamento por parede
- Estimativa de custo (R$ 480,00/m³)

### Calculadora de Tijolos
- Mesma estrutura de paredes do concreto
- Campos adicionais para dimensões do tijolo e percentual de perda/quebra
- Desconto automático das áreas de aberturas (janelas e portas)
- Estimativa de custo (R$ 1,40/tijolo)

### Orçamentos
- Ao navegar para a aba Orçamentos, o campo de custo é **pré-preenchido automaticamente** com o último valor calculado (concreto ou tijolos)
- Salvar novos orçamentos com nome do cliente e custo estimado
- Buscar orçamentos por ID numérico ou nome do cliente
- Editar e excluir registros existentes
- Feedback visual de sucesso ou erro após cada operação

---

## Modelagem da Planta Baixa

A planta baixa é representada como um grafo **G=(V,A)**, onde cada elemento é mapeado para uma entidade JPA:

| Conceito da planta | Classe Java | Tabela no banco |
|---|---|---|
| Encontro de paredes / pilar | `Vertice` | `VERTICE` |
| Parede | `Aresta` | `ARESTA` |
| Cômodo (sala, quarto…) | `Comodo` | `COMODO` |

Cada `Aresta` (parede) armazena:

| Campo | Tipo | Descrição |
|---|---|---|
| `espessura` | Double | Largura da parede em metros |
| `comprimento` | Double | Distância entre os dois pilares em metros |
| `altura` | Double | Pé-direito do cômodo em metros |
| `temJanela` | Boolean | Indica se a parede possui janela |
| `alturaJanela` | Double | Altura da janela em metros |
| `comprimentoJanela` | Double | Comprimento da janela em metros |
| `temPorta` | Boolean | Indica se a parede possui porta |
| `alturaPorta` | Double | Altura da porta em metros |
| `comprimentoPorta` | Double | Comprimento da porta em metros |

---

## Volume de Concreto na Fundação

Calcula o volume de concreto necessário para as **vigas baldrame** — elementos horizontais de concreto armado que percorrem o traçado das paredes na fundação.

### Fórmula

```
Volume (m³) = Espessura da parede × Altura da viga × Comprimento da parede
```

A espessura e o comprimento vêm de cada aresta; a altura da viga é informada pelo usuário.

---

## Quantidade de Tijolos

Calcula a quantidade de tijolos para assentar as paredes, descontando automaticamente as áreas de janelas e portas e aplicando um percentual de perda por quebra.

### Fórmula

```
1. Área bruta          = comprimento_parede × altura_parede
2. Área das aberturas  = Σ (altura × comprimento) de cada janela e porta
3. Área líquida        = área bruta − área das aberturas
4. Área do tijolo      = comprimento_tijolo × altura_tijolo
5. Tijolos (sem perda) = ⌈ área líquida / área do tijolo ⌉
6. Tijolos (com perda) = ⌈ tijolos_sem_perda × (1 + percentual_perda) ⌉
```

---

## Orçamentos

Os orçamentos são persistidos no banco H2 em arquivo (sobrevivem ao reinício da aplicação). Cada registro guarda:

| Campo | Tipo | Descrição |
|---|---|---|
| `id` | Long | Identificador gerado automaticamente |
| `nomeUsuario` | String | Nome do cliente |
| `custoTotalEstimado` | Double | Valor estimado em reais |

A busca suporta dois modos: por **ID numérico** (busca exata) ou por **nome** (busca parcial, sem distinção de maiúsculas/minúsculas).

---

## Banco de dados H2

O projeto usa o banco **H2 em arquivo** para persistência entre sessões.

Para inspecionar os dados em tempo real, acesse o console com o servidor rodando:

```
URL:      http://localhost:8080/h2-console
JDBC URL: jdbc:h2:file:./data/cmordb
Usuário:  sa
Senha:    (deixar em branco)
```

---

## API REST

Além da interface JSF, o projeto expõe endpoints REST para integração com ferramentas externas (Postman, etc).

### POST /api/fundacao/concreto

```json
{
  "alturaViga": 0.4,
  "arestas": [
    { "nome": "a12", "espessura": 0.15, "comprimento": 4.0, "altura": 2.8 },
    { "nome": "a23", "espessura": 0.15, "comprimento": 3.5, "altura": 2.8 }
  ]
}
```

### POST /api/parede/tijolos

```json
{
  "comprimentoTijolo": 0.19,
  "alturaTijolo": 0.057,
  "larguraTijolo": 0.09,
  "percentualPerda": 0.10,
  "arestas": [
    { "nome": "a12", "espessura": 0.15, "comprimento": 4.0, "altura": 2.8,
      "temJanela": true, "alturaJanela": 1.2, "comprimentoJanela": 1.5,
      "temPorta": false }
  ]
}
```

### GET/POST /api/orcamentos

| Método | Rota | Descrição |
|---|---|---|
| `GET` | `/api/orcamentos` | Lista todos |
| `POST` | `/api/orcamentos` | Cria novo |
| `PUT` | `/api/orcamentos/{id}` | Atualiza |
| `DELETE` | `/api/orcamentos/{id}` | Remove |
| `GET` | `/api/orcamentos/busca?termo=` | Busca por ID ou nome |

---

## Decisões de design

**Grafo como domínio:** as classes `Vertice`, `Aresta` e `Comodo` modelam fielmente o grafo G=(V,A) descrito no enunciado, com relacionamentos JPA entre elas.

**Jakarta Faces (JSF):** a migração de HTML estático para JSF eliminou a necessidade de fetch/JavaScript manual. Os Managed Beans (`@Named` + `@ViewScoped`) chamam os serviços Java diretamente, sem passar por HTTP — mais simples, mais seguro e sem código de rede duplicado.

**Escopos de bean:** beans de formulário usam `@ViewScoped` (vivem enquanto o usuário está na página). O `OrcamentoSessaoBean` usa `@SessionScoped` para transportar o último custo calculado entre abas sem perda de estado.

**ORM com Spring Data JPA:** toda persistência é feita via repositórios que estendem `JpaRepository`, sem SQL manual.

**Padrão DTO:** os endpoints REST recebem e devolvem DTOs — nunca as entidades JPA diretamente — para desacoplar a API do modelo interno.

**CORS configurado:** a classe `CorsConfig` libera chamadas de qualquer origem para `/api/**`, permitindo uso com Postman ou integração futura com outros clientes.

---

## Autor

Desenvolvido por **Guilherme** — Ciência da Computação · UniCEUB  
Disciplina: Desenvolvimento de Sistemas