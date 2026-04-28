package com.cmor.dto;

/**
 * Representa uma aresta (parede) enviada na requisição REST.
 * O cliente não precisa conhecer os IDs internos — apenas as dimensões.
 */
public class ArestaDTO {

    private String nome;
    private Double espessura;   // m
    private Double comprimento; // m
    private Double altura;      // m (pé-direito)

    private Boolean temJanela = false;
    private Double alturaJanela;
    private Double comprimentoJanela;

    private Boolean temPorta = false;
    private Double alturaPorta;
    private Double comprimentoPorta;

    public ArestaDTO() {}

    // ── getters / setters ──────────────────────────────────────────────────

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

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
}
