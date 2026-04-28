package com.cmor.model;

import jakarta.persistence.*;

/**
 * Aresta do grafo G=(V,A) — representa uma PAREDE da planta baixa.
 *
 * Uma aresta conecta dois vértices (pilares) e possui:
 *  - espessura (largura da parede)
 *  - comprimento (distância entre os dois pilares)
 *  - altura (do pé-direito do cômodo adjacente)
 *  - indicadores de janela e porta com suas dimensões
 */
@Entity
@Table(name = "aresta")
public class Aresta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Rótulo da aresta (ex: a12, a23 …) */
    private String nome;

    @ManyToOne
    @JoinColumn(name = "vertice_origem_id")
    private Vertice verticeOrigem;

    @ManyToOne
    @JoinColumn(name = "vertice_destino_id")
    private Vertice verticeDestino;

    /** Espessura da parede (m) */
    private Double espessura;

    /** Comprimento da parede (m) */
    private Double comprimento;

    /** Altura da parede / pé-direito (m) */
    private Double altura;

    // ── Janela ────────────────────────────────────────────────────────────

    private Boolean temJanela = false;
    private Double alturaJanela;
    private Double comprimentoJanela;

    // ── Porta ─────────────────────────────────────────────────────────────

    private Boolean temPorta = false;
    private Double alturaPorta;
    private Double comprimentoPorta;

    /** Cômodo ao qual esta parede pertence (pode ser compartilhada) */
    @ManyToOne
    @JoinColumn(name = "comodo_id")
    private Comodo comodo;

    public Aresta() {}

    // ── getters / setters ──────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Vertice getVerticeOrigem() { return verticeOrigem; }
    public void setVerticeOrigem(Vertice verticeOrigem) { this.verticeOrigem = verticeOrigem; }

    public Vertice getVerticeDestino() { return verticeDestino; }
    public void setVerticeDestino(Vertice verticeDestino) { this.verticeDestino = verticeDestino; }

    public Double getEspessura() { return espessura; }
    public void setEspessura(Double espessura) { this.espessura = espessura; }

    public Double getComprimento() { return comprimento; }
    public void setComprimento(Double comprimento) { this.comprimento = comprimento; }

    public Double getAltura() { return altura; }
    public void setAltura(Double altura) { this.altura = altura; }

    public Boolean getTemJanela() { return temJanela; }
    public void setTemJanela(Boolean temJanela) { this.temJanela = temJanela; }

    public Double getAlturaJanela() { return alturaJanela; }
    public void setAlturaJanela(Double alturaJanela) { this.alturaJanela = alturaJanela; }

    public Double getComprimentoJanela() { return comprimentoJanela; }
    public void setComprimentoJanela(Double comprimentoJanela) { this.comprimentoJanela = comprimentoJanela; }

    public Boolean getTemPorta() { return temPorta; }
    public void setTemPorta(Boolean temPorta) { this.temPorta = temPorta; }

    public Double getAlturaPorta() { return alturaPorta; }
    public void setAlturaPorta(Double alturaPorta) { this.alturaPorta = alturaPorta; }

    public Double getComprimentoPorta() { return comprimentoPorta; }
    public void setComprimentoPorta(Double comprimentoPorta) { this.comprimentoPorta = comprimentoPorta; }

    public Comodo getComodo() { return comodo; }
    public void setComodo(Comodo comodo) { this.comodo = comodo; }
}
