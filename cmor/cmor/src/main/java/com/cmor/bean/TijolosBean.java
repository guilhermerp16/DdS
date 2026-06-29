package com.cmor.bean;

import com.cmor.dto.ArestaDTO;
import com.cmor.dto.TijoloRequestDTO;
import com.cmor.dto.TijoloResponseDTO;
import com.cmor.service.ParedeService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * TijolosBean — Managed Bean JSF para o cálculo de tijolos.
 *
 * Mesma estrutura do ConcretoBean, mas delega ao ParedeService.
 * Campos extras: dimensões do tijolo e percentual de perda.
 */
@Named("tijolosBean")
@ViewScoped
@Component
public class TijolosBean implements Serializable {

    @Autowired
    private ParedeService paredeService;

    @Autowired
    private OrcamentoSessaoBean orcamentoSessaoBean;

    // ── Estado do formulário ───────────────────────────────────────────────

    /** Dimensões do tijolo (m) */
    private Double comprimentoTijolo = 0.19;
    private Double alturaTijolo      = 0.057;
    private Double larguraTijolo     = 0.09;

    /** Percentual de perda/quebra — exibido como % (ex: 10 = 10%) */
    private Double percentualPerda   = 10.0;

    private List<ParedeForm>    paredes  = new ArrayList<>();
    private TijoloResponseDTO   resultado;
    private Double              custoEstimado;

    @PostConstruct
    public void init() {
        paredes.add(new ParedeForm("Parede 1"));
    }

    // ── Ações ──────────────────────────────────────────────────────────────

    public void adicionarParede() {
        paredes.add(new ParedeForm("Parede " + (paredes.size() + 1)));
    }

    public void removerParede(int index) {
        if (index >= 0 && index < paredes.size()) {
            paredes.remove(index);
        }
    }

    /**
     * Executa o cálculo de tijolos via ParedeService.
     * O percentualPerda vem do campo como inteiro (ex: 10)
     * e é convertido para decimal (0.10) antes de enviar ao serviço.
     */
    public String calcular() {
        if (paredes.isEmpty()) {
            resultado = null;
            return null;
        }

        List<ArestaDTO> arestas = new ArrayList<>();
        for (ParedeForm p : paredes) {
            ArestaDTO dto = new ArestaDTO();
            dto.setNome(p.getNome());
            dto.setEspessura(p.getEspessura());
            dto.setComprimento(p.getComprimento());
            dto.setAltura(p.getAltura());
            dto.setTemJanela(p.isTemJanela());
            dto.setAlturaJanela(p.isTemJanela() ? p.getAlturaJanela() : null);
            dto.setComprimentoJanela(p.isTemJanela() ? p.getComprimentoJanela() : null);
            dto.setTemPorta(p.isTemPorta());
            dto.setAlturaPorta(p.isTemPorta() ? p.getAlturaPorta() : null);
            dto.setComprimentoPorta(p.isTemPorta() ? p.getComprimentoPorta() : null);
            arestas.add(dto);
        }

        TijoloRequestDTO request = new TijoloRequestDTO();
        request.setComprimentoTijolo(comprimentoTijolo);
        request.setAlturaTijolo(alturaTijolo);
        request.setLarguraTijolo(larguraTijolo);
        // Converte % para decimal: 10% → 0.10
        request.setPercentualPerda(percentualPerda / 100.0);
        request.setArestas(arestas);

        resultado = paredeService.calcularTijolos(request);

        // Custo estimado: R$ 1,40 por tijolo (mesmo valor do HTML original)
        custoEstimado = resultado.getQuantidadeTotalTijolos() * 1.40;

        // Salva na sessão para pré-preencher a aba Orçamentos
        orcamentoSessaoBean.setUltimoCusto(custoEstimado, "Tijolos");

        return null;
    }

    // ── Getters e Setters ──────────────────────────────────────────────────

    public Double getComprimentoTijolo() { return comprimentoTijolo; }
    public void setComprimentoTijolo(Double comprimentoTijolo) { this.comprimentoTijolo = comprimentoTijolo; }

    public Double getAlturaTijolo() { return alturaTijolo; }
    public void setAlturaTijolo(Double alturaTijolo) { this.alturaTijolo = alturaTijolo; }

    public Double getLarguraTijolo() { return larguraTijolo; }
    public void setLarguraTijolo(Double larguraTijolo) { this.larguraTijolo = larguraTijolo; }

    public Double getPercentualPerda() { return percentualPerda; }
    public void setPercentualPerda(Double percentualPerda) { this.percentualPerda = percentualPerda; }

    public List<ParedeForm> getParedes() { return paredes; }
    public void setParedes(List<ParedeForm> paredes) { this.paredes = paredes; }

    public TijoloResponseDTO getResultado() { return resultado; }

    public Double getCustoEstimado() { return custoEstimado; }
}