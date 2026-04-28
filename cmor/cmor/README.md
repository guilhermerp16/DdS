# CMOR — Calculadora de Materiais para Obra Residencial

> Projeto acadêmico desenvolvido para a disciplina **Desenvolvimento de Sistemas** do curso de Ciência da Computação — **UniCEUB**.

Backend REST desenvolvido em **Java + Spring Boot** que calcula o consumo de materiais em fases de obras residenciais a partir de parâmetros fornecidos pelo usuário e fórmulas da engenharia civil.

---

## Índice

- [Tecnologias](#tecnologias)
- [Pré-requisitos](#pré-requisitos)
- [Como executar](#como-executar)
- [Interface web](#interface-web)
- [Modelagem da Planta Baixa](#etapa-1--modelagem-da-planta-baixa)
- [Volume de Concreto](#etapa-2--volume-de-concreto-na-fundação)
- [Quantidade de Tijolos](#etapa-3--quantidade-de-tijolos)
- [Banco de dados H2](#banco-de-dados-h2)
- [Decisões de design](#decisões-de-design)

---

## Sobre o projeto

Empresas de engenharia precisam lidar constantemente com dimensionamento de materiais e fornecer orçamentos baseados em previsão de custos. O **CMOR** é um conjunto de serviços computacionais que calculam o consumo de materiais em determinadas fases de obras residenciais.

A planta baixa da casa é modelada como um **grafo G=(V,A)**, onde:
- os **vértices** representam os encontros de paredes (que receberão pilares estruturais)
- as **arestas** representam as paredes, com espessura, altura, comprimento e indicação de aberturas (janelas e portas)

---

## Tecnologias

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 23 | Linguagem principal |
| Spring Boot | 3.2.5 | Framework web e configuração |
| Spring Data JPA | 3.2.5 | ORM e persistência |
| Hibernate | 6.4.4 | Implementação JPA |
| H2 Database | 2.2.224 | Banco de dados em memória |
| Bean Validation | 3.0 | Validação dos dados de entrada |
| Maven | 3.x | Gerenciamento de dependências |

---

## Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

- [JDK 17+](https://adoptium.net/) (o projeto foi testado com JDK 23)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- [Postman](https://www.postman.com/downloads/) para testar os endpoints diretamente (opcional)

---

## Como executar

1. Clone ou baixe o repositório e abra a pasta no IntelliJ
2. Aguarde o Maven baixar as dependências automaticamente
3. Localize o arquivo `CmorApplication.java` em `src/main/java/com/cmor/`
4. Clique no botão **▶ Run** ao lado da classe `main`
5. Aguarde a mensagem no console:

```
Tomcat started on port 8080 (http)
Started CmorApplication in X seconds
```

6. Acesse `http://localhost:8080` no navegador — a interface da calculadora abrirá automaticamente

---

## Interface web

O projeto inclui uma interface web integrada, servida automaticamente pelo Spring Boot em `http://localhost:8080`.

Ela permite inserir os dados das paredes manualmente e visualizar os resultados de ambas as etapas sem necessidade de ferramentas externas como o Postman.

O arquivo fonte da interface está em:

```
src/main/resources/static/index.html
```

---

## Modelagem da Planta Baixa

A planta baixa é representada como um grafo **G=(V,A)**, onde cada elemento é mapeado para uma entidade JPA persistida automaticamente no banco de dados:

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

### Endpoint

```
POST /api/fundacao/concreto
Content-Type: application/json
```

### Campos da requisição

| Campo | Tipo | Obrigatório | Descrição |
|---|---|---|---|
| `alturaViga` | Double | ✓ | Altura da viga baldrame em metros |
| `arestas` | List | ✓ | Lista de paredes da planta |
| `arestas[].nome` | String | | Identificador da parede (ex: "a12") |
| `arestas[].espessura` | Double | ✓ | Espessura da parede em metros |
| `arestas[].comprimento` | Double | ✓ | Comprimento da parede em metros |
| `arestas[].altura` | Double | | Altura da parede em metros |

### Exemplo de requisição

```json
{
  "alturaViga": 0.4,
  "arestas": [
    { "nome": "a12", "espessura": 0.15, "comprimento": 4.0, "altura": 2.8 },
    { "nome": "a23", "espessura": 0.15, "comprimento": 3.5, "altura": 2.8 },
    { "nome": "a34", "espessura": 0.15, "comprimento": 4.0, "altura": 2.8 },
    { "nome": "a14", "espessura": 0.15, "comprimento": 3.5, "altura": 2.8 }
  ]
}
```

### Exemplo de resposta — `200 OK`

```json
{
  "volumeTotalM3": 0.9,
  "alturaViga": 0.4,
  "detalhesPorAresta": [
    { "aresta": "a12", "espessura_m": 0.15, "comprimento_m": 4.0, "alturaViga_m": 0.4, "volume_m3": 0.24 },
    { "aresta": "a23", "espessura_m": 0.15, "comprimento_m": 3.5, "alturaViga_m": 0.4, "volume_m3": 0.21 },
    { "aresta": "a34", "espessura_m": 0.15, "comprimento_m": 4.0, "alturaViga_m": 0.4, "volume_m3": 0.24 },
    { "aresta": "a14", "espessura_m": 0.15, "comprimento_m": 3.5, "alturaViga_m": 0.4, "volume_m3": 0.21 }
  ]
}
```

---

## Quantidade de Tijolos

Calcula a quantidade de tijolos necessária para assentar as paredes, descontando automaticamente as áreas de janelas e portas e aplicando um percentual de perda por quebra.

### Fórmula

```
1. Área bruta          = comprimento_parede × altura_parede
2. Área das aberturas  = Σ (altura × comprimento) de cada janela e porta
3. Área líquida        = área bruta − área das aberturas
4. Área do tijolo      = comprimento_tijolo × altura_tijolo
5. Tijolos (sem perda) = ⌈ área líquida / área do tijolo ⌉
6. Tijolos (com perda) = ⌈ tijolos_sem_perda × (1 + percentual_perda) ⌉
```


### Exemplo de resposta — `200 OK`

```json
{
  "quantidadeTotalTijolos": 3893,
  "quantidadeSemPerda": 3539,
  "percentualPerda": 10.0,
  "detalhesPorAresta": [
    { "aresta": "a12", "areaBruta_m2": 11.2, "areaAberturas_m2": 1.80, "areaLiquida_m2": 9.40, "tijolosSemPerda": 868  },
    { "aresta": "a23", "areaBruta_m2": 9.8,  "areaAberturas_m2": 1.89, "areaLiquida_m2": 7.91, "tijolosSemPerda": 731  },
    { "aresta": "a34", "areaBruta_m2": 11.2, "areaAberturas_m2": 0.00, "areaLiquida_m2": 11.2, "tijolosSemPerda": 1035 },
    { "aresta": "a14", "areaBruta_m2": 9.8,  "areaAberturas_m2": 0.00, "areaLiquida_m2": 9.80, "tijolosSemPerda": 905  }
  ]
}
```

---

## Banco de dados H2

O projeto usa o banco **H2 em memória** para desenvolvimento, criado automaticamente ao iniciar e destruído ao encerrar a aplicação.

Para inspecionar os dados em tempo real, acesse o console com o servidor rodando:

```
URL:      http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:cmordb
Usuário:  sa
Senha:    (deixar em branco)
```

Para usar um banco persistente em produção, substitua as configurações em `application.properties` pelas de PostgreSQL ou MySQL.

---

## Decisões de design

**Grafo como domínio:** as classes `Vertice`, `Aresta` e `Comodo` modelam fielmente o grafo G=(V,A) descrito no enunciado, com relacionamentos JPA entre elas. Arestas podem ser compartilhadas entre cômodos adjacentes.

**ORM com Spring Data JPA:** toda persistência é feita via repositórios que estendem `JpaRepository`, sem SQL manual. O Hibernate gera e executa o DDL automaticamente com base nas anotações das entidades.

**Padrão DTO:** os endpoints recebem e devolvem DTOs — nunca as entidades JPA diretamente — para desacoplar a API do modelo interno e evitar dependências circulares na serialização JSON.

**Validação declarativa:** as anotações `@NotNull`, `@NotEmpty` e `@Positive` nos DTOs garantem que dados inválidos retornam `400 Bad Request` com mensagem descritiva, sem código manual de validação.

**Interface web integrada:** o arquivo `index.html` em `src/main/resources/static/` é servido automaticamente pelo Spring Boot, dispensando servidor web separado ou ferramentas externas para uso básico da calculadora.

**CORS configurado:** a classe `CorsConfig` libera chamadas de qualquer origem para `/api/**`, permitindo que a interface web e ferramentas como o Postman se comuniquem com o servidor sem bloqueio do navegador.

---

## Autor

Desenvolvido por **Guilherme** — Ciência da Computação · UniCEUB  
Disciplina: Desenvolvimento de Sistemas
