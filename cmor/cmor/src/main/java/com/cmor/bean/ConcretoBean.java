package com.cmor.bean;

import com.cmor.dto.ArestaDTO;
import com.cmor.dto.ConcreteRequestDTO;
import com.cmor.dto.ConcreteResponseDTO;
import com.cmor.service.FundacaoService;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ConcretoBean — Managed Bean JSF para o cálculo de concreto.
 *
 * Anotações importantes:
 *
 * @Named("concretoBean")
 *   Registra este bean no contexto CDI com o nome "concretoBean".
 *   Na página .xhtml, é acessado via #{concretoBean.propriedade}.
 *
 * @ViewScoped
 *   O bean vive enquanto o usuário estiver na mesma página (View).
 *   Se navegar para outra aba e voltar, o bean é recriado.
 *   É o escopo ideal para formulários com AJAX.
 *
 * @Component
 *   Permite que o Spring injete dependências (@Autowired) neste bean,
 *   integrando o ciclo de vida do Spring com o CDI do JSF via JoinFaces.
 *
 * Implements Serializable: obrigatório para @ViewScoped.
 */
@Named("concretoBean")
@ViewScoped
@Component
public class ConcretoBean implements Serializable {

    // ── Dependências injetadas pelo Spring ─────────────────────────────────
    @Autowired
    private FundacaoService fundacaoService;

    @Autowired
    private OrcamentoSessaoBean orcamentoSessaoBean;

    // ── Estado do formulário ───────────────────────────────────────────────

    /** Altura da viga baldrame (m) — vinculada ao h:inputText no .xhtml */
    private Double alturaViga = 0.4;

    /** Lista de paredes do formulário — iterada pelo c:forEach no .xhtml */
    private List<ParedeForm> paredes = new ArrayList<>();

    /** Resultado do cálculo — nulo até o usuário clicar em "Calcular" */
    private ConcreteResponseDTO resultado;

    /** Custo estimado calculado com base no volume total */
    private Double custoEstimado;

    // ── Ciclo de vida ──────────────────────────────────────────────────────

    /**
     * @PostConstruct é chamado pelo CDI após a injeção de dependências.
     * Inicializa o formulário com uma parede padrão.
     */
    @PostConstruct
    public void init() {
        paredes.add(new ParedeForm("Parede 1"));
    }

    // ── Ações chamadas pelo JSF ────────────────────────────────────────────

    /**
     * Adiciona uma nova parede à lista.
     * Chamado pelo h:commandButton "+ adicionar parede" via f:ajax.
     */
    public void adicionarParede() {
        paredes.add(new ParedeForm("Parede " + (paredes.size() + 1)));
    }

    /**
     * Remove a parede no índice informado.
     * Chamado pelo botão "×" de cada wall-card.
     *
     * @param index índice da parede na lista (passado via EL: #{status.index})
     */
    public void removerParede(int index) {
        if (index >= 0 && index < paredes.size()) {
            paredes.remove(index);
        }
    }

    /**
     * Executa o cálculo de volume de concreto chamando o FundacaoService.
     * O resultado é armazenado em this.resultado e exibido pelo h:panelGroup
     * "resultConcreto" que é atualizado via f:ajax.
     *
     * @return null — permanece na mesma página (navegação AJAX, não redirect)
     */
    public String calcular() {
        if (paredes.isEmpty()) {
            resultado = null;
            return null;
        }

        // Converte ParedeForm → ArestaDTO (camada de serviço não conhece ParedeForm)
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

        ConcreteRequestDTO request = new ConcreteRequestDTO();
        request.setAlturaViga(alturaViga);
        request.setArestas(arestas);

        // Chama o serviço diretamente (sem HTTP) — muito mais eficiente que fetch()
        resultado = fundacaoService.calcularConcreto(request);

        // Custo estimado: R$ 480,00 por m³ (mesmo valor do HTML original)
        custoEstimado = resultado.getVolumeTotalM3() * 480.00;

        // Salva na sessão para pré-preencher a aba Orçamentos
        orcamentoSessaoBean.setUltimoCusto(custoEstimado, "Concreto");

        return null;
    }

    // ── Getters e Setters ──────────────────────────────────────────────────

    public Double getAlturaViga() { return alturaViga; }
    public void setAlturaViga(Double alturaViga) { this.alturaViga = alturaViga; }

    public List<ParedeForm> getParedes() { return paredes; }
    public void setParedes(List<ParedeForm> paredes) { this.paredes = paredes; }

    public ConcreteResponseDTO getResultado() { return resultado; }

    public Double getCustoEstimado() { return custoEstimado; }
}