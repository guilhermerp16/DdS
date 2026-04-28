package com.cmor.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

/**
 * Payload para POST /api/fundacao/concreto
 *
 * Recebe a lista de arestas (paredes) e a altura da viga baldrame.
 * L e C são obtidos da própria parede; A é informado pelo usuário.
 */
public class ConcreteRequestDTO {

    @NotEmpty(message = "É necessário informar ao menos uma aresta.")
    private List<ArestaDTO> arestas;

    /** Altura da viga baldrame (m) — informada pelo usuário */
    @NotNull(message = "A altura da viga baldrame é obrigatória.")
    @Positive(message = "A altura deve ser positiva.")
    private Double alturaViga;

    public ConcreteRequestDTO() {}

    public List<ArestaDTO> getArestas() { return arestas; }
    public void setArestas(List<ArestaDTO> arestas) { this.arestas = arestas; }

    public Double getAlturaViga() { return alturaViga; }
    public void setAlturaViga(Double alturaViga) { this.alturaViga = alturaViga; }
}
