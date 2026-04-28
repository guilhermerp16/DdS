package com.cmor.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Vértice do grafo G=(V,A) que representa a planta baixa.
 * Cada vértice é um canto/encontro de paredes que receberá um pilar estrutural.
 */
@Entity
@Table(name = "vertice")
public class Vertice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nome/rótulo do vértice (ex: V1, V2 …) */
    private String nome;

    @OneToMany(mappedBy = "verticeOrigem", cascade = CascadeType.ALL)
    private List<Aresta> arestasOrigem = new ArrayList<>();

    @OneToMany(mappedBy = "verticeDestino", cascade = CascadeType.ALL)
    private List<Aresta> arestasDestino = new ArrayList<>();

    public Vertice() {}

    public Vertice(String nome) {
        this.nome = nome;
    }

    // ── getters / setters ──────────────────────────────────────────────────

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public List<Aresta> getArestasOrigem() { return arestasOrigem; }
    public void setArestasOrigem(List<Aresta> arestasOrigem) { this.arestasOrigem = arestasOrigem; }

    public List<Aresta> getArestasDestino() { return arestasDestino; }
    public void setArestasDestino(List<Aresta> arestasDestino) { this.arestasDestino = arestasDestino; }
}
