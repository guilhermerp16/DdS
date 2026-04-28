package com.cmor.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Cômodo da planta baixa.
 * Possui nome (ex: "Sala", "Quarto") e dimensões internas.
 * Suas paredes são as Arestas que o compõem (podem ser compartilhadas).
 */
@Entity
@Table(name = "comodo")
public class Comodo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private Double largura;
    private Double comprimento;
    private Double altura;

    @OneToMany(mappedBy = "comodo", cascade = CascadeType.ALL)
    private List<Aresta> paredes = new ArrayList<>();

    public Comodo() {}

    public Comodo(String nome, Double largura, Double comprimento, Double altura) {
        this.nome = nome;
        this.largura = largura;
        this.comprimento = comprimento;
        this.altura = altura;
    }

    // ── getters / setters ──────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Double getLargura() { return largura; }
    public void setLargura(Double largura) { this.largura = largura; }

    public Double getComprimento() { return comprimento; }
    public void setComprimento(Double comprimento) { this.comprimento = comprimento; }

    public Double getAltura() { return altura; }
    public void setAltura(Double altura) { this.altura = altura; }

    public List<Aresta> getParedes() { return paredes; }
    public void setParedes(List<Aresta> paredes) { this.paredes = paredes; }
}
