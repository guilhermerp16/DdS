package com.cmor.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

/**
 * Payload para POST /api/paredes/tijolos
 *
 * Recebe a lista de arestas e as dimensões do tijolo a ser utilizado.
 * A quantidade é calculada descontando aberturas (janelas/portas).
 */
public class TijoloRequestDTO {

    @NotEmpty(message = "É necessário informar ao menos uma aresta.")
    private List<ArestaDTO> arestas;

    /** Altura do tijolo (m) */
    @NotNull @Positive
    private Double alturaTijolo;

    /** Largura do tijolo (m) */
    @NotNull @Positive
    private Double larguraTijolo;

    /** Comprimento do tijolo (m) */
    @NotNull @Positive
    private Double comprimentoTijolo;

    /** Percentual de perda/quebra (padrão 10%) */
    private Double percentualPerda = 0.10;

    public TijoloRequestDTO() {}

    public List<ArestaDTO> getArestas() { return arestas; }
    public void setArestas(List<ArestaDTO> arestas) { this.arestas = arestas; }

    public Double getAlturaTijolo() { return alturaTijolo; }
    public void setAlturaTijolo(Double alturaTijolo) { this.alturaTijolo = alturaTijolo; }

    public Double getLarguraTijolo() { return larguraTijolo; }
    public void setLarguraTijolo(Double larguraTijolo) { this.larguraTijolo = larguraTijolo; }

    public Double getComprimentoTijolo() { return comprimentoTijolo; }
    public void setComprimentoTijolo(Double comprimentoTijolo) { this.comprimentoTijolo = comprimentoTijolo; }

    public Double getPercentualPerda() { return percentualPerda; }
    public void setPercentualPerda(Double percentualPerda) { this.percentualPerda = percentualPerda; }
}
