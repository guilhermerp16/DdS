package com.cmor.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orcamentos")
public class Orcamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Este será o Número do Orçamento solicitado

    @Column(name = "nome_usuario", nullable = false)
    private String nomeUsuario;

    @Column(name = "data_solicitacao")
    private LocalDateTime dataSolicitacao;

    @Column(name = "custo_total_estimado")
    private double custoTotalEstimado;

    // Construtor padrão exigido pelo JPA
    public Orcamento() {
        this.dataSolicitacao = LocalDateTime.now();
    }

    public Orcamento(String nomeUsuario, double custoTotalEstimado) {
        this.nomeUsuario = nomeUsuario;
        this.custoTotalEstimado = custoTotalEstimado;
        this.dataSolicitacao = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

    public LocalDateTime getDataSolicitacao() { return dataSolicitacao; }
    public void setDataSolicitacao(LocalDateTime dataSolicitacao) { this.dataSolicitacao = dataSolicitacao; }

    public double getCustoTotalEstimado() { return custoTotalEstimado; }
    public void setCustoTotalEstimado(double custoTotalEstimado) { this.custoTotalEstimado = custoTotalEstimado; }
}