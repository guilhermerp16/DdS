package com.cmor.bean;

import java.io.Serializable;

/**
 * ParedeForm — representa uma parede no formulário JSF.
 *
 * Esta classe é necessária porque o JSF precisa vincular cada campo
 * do formulário (h:inputText) a uma propriedade Java via EL.
 * Ela espelha os campos do ArestaDTO, mas é específica para a camada de View.
 *
 * Implements Serializable: obrigatório para beans com @ViewScoped,
 * pois o JSF pode serializar o estado da View.
 */
public class ParedeForm implements Serializable {

    private String nome        = "Parede";
    private Double espessura   = 0.15;
    private Double comprimento = 4.0;
    private Double altura      = 2.8;

    private boolean temJanela      = false;
    private Double  alturaJanela   = 1.2;
    private Double  comprimentoJanela = 1.5;

    private boolean temPorta       = false;
    private Double  alturaPorta    = 2.1;
    private Double  comprimentoPorta  = 0.9;

    public ParedeForm() {}

    public ParedeForm(String nome) {
        this.nome = nome;
    }

    // ── Getters e Setters ──────────────────────────────────────────────────

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Double getEspessura() { return espessura; }
    public void setEspessura(Double espessura) { this.espessura = espessura; }

    public Double getComprimento() { return comprimento; }
    public void setComprimento(Double comprimento) { this.comprimento = comprimento; }

    public Double getAltura() { return altura; }
    public void setAltura(Double altura) { this.altura = altura; }

    public boolean isTemJanela() { return temJanela; }
    public void setTemJanela(boolean temJanela) { this.temJanela = temJanela; }

    public Double getAlturaJanela() { return alturaJanela; }
    public void setAlturaJanela(Double alturaJanela) { this.alturaJanela = alturaJanela; }

    public Double getComprimentoJanela() { return comprimentoJanela; }
    public void setComprimentoJanela(Double comprimentoJanela) { this.comprimentoJanela = comprimentoJanela; }

    public boolean isTemPorta() { return temPorta; }
    public void setTemPorta(boolean temPorta) { this.temPorta = temPorta; }

    public Double getAlturaPorta() { return alturaPorta; }
    public void setAlturaPorta(Double alturaPorta) { this.alturaPorta = alturaPorta; }

    public Double getComprimentoPorta() { return comprimentoPorta; }
    public void setComprimentoPorta(Double comprimentoPorta) { this.comprimentoPorta = comprimentoPorta; }
}
