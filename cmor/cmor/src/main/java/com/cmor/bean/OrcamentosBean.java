package com.cmor.bean;

import com.cmor.model.Orcamento;
import com.cmor.repository.OrcamentoRepository;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * OrcamentosBean — Managed Bean JSF para gerenciamento de orçamentos.
 *
 * Acessa o OrcamentoRepository diretamente (sem passar pelo Controller REST),
 * pois agora a View e o Backend estão no mesmo processo Java.
 * Isso é uma das grandes vantagens do JSF sobre REST + HTML puro.
 */
@Named("orcamentosBean")
@ViewScoped
@Component
public class OrcamentosBean implements Serializable {

    @Autowired
    private OrcamentoRepository orcamentoRepository;

    @Autowired
    private OrcamentoSessaoBean orcamentoSessaoBean;

    // ── Estado do formulário de salvar/editar ──────────────────────────────
    private String nomeUsuario;
    private Double custoTotal;

    // ── Estado da edição ───────────────────────────────────────────────────
    private boolean modoEdicao = false;
    private Long    idEdicao;

    // ── Estado da busca ────────────────────────────────────────────────────
    private String         termoBusca;
    private List<Orcamento> resultadoBusca = new ArrayList<>();
    private boolean        buscaRealizada  = false;

    // ── Mensagens de feedback ──────────────────────────────────────────────
    private String mensagemSucesso;
    private String mensagemErro;

    @PostConstruct
    public void init() {
        // Carrega todos os orçamentos ao abrir a aba
        resultadoBusca = orcamentoRepository.findAll();
        buscaRealizada = true;

        // Pré-preenche o campo custo com o último valor calculado na sessão
        if (orcamentoSessaoBean.getUltimoCusto() != null) {
            custoTotal = orcamentoSessaoBean.getUltimoCusto();
        }
    }

    // ── Ações ──────────────────────────────────────────────────────────────

    /**
     * Salva um novo orçamento ou atualiza um existente (modo edição).
     * Equivale ao POST /api/orcamentos e PUT /api/orcamentos/{id}.
     */
    public String salvar() {
        mensagemSucesso = null;
        mensagemErro    = null;

        if (nomeUsuario == null || nomeUsuario.trim().isEmpty()) {
            mensagemErro = "Por favor, informe o nome do usuário/cliente.";
            return null;
        }
        if (custoTotal == null) {
            mensagemErro = "Por favor, informe o custo total estimado.";
            return null;
        }

        if (modoEdicao && idEdicao != null) {
            // Modo edição: atualiza registro existente
            orcamentoRepository.findById(idEdicao).ifPresent(orc -> {
                orc.setNomeUsuario(nomeUsuario.trim());
                orc.setCustoTotalEstimado(custoTotal);
                orcamentoRepository.save(orc);
                mensagemSucesso = "✅ <b>Orçamento Atualizado!</b><br/>"
                        + "Nº: <b>" + orc.getId() + "</b> | "
                        + "Cliente: <b>" + orc.getNomeUsuario() + "</b> | "
                        + "Valor: <b>R$ " + String.format("%.2f", orc.getCustoTotalEstimado()) + "</b>";
            });
        } else {
            // Novo orçamento
            Orcamento novo = new Orcamento(nomeUsuario.trim(), custoTotal);
            orcamentoRepository.save(novo);
            mensagemSucesso = "✅ <b>Orçamento Salvo!</b><br/>"
                    + "Nº: <b>" + novo.getId() + "</b> | "
                    + "Cliente: <b>" + novo.getNomeUsuario() + "</b> | "
                    + "Valor: <b>R$ " + String.format("%.2f", novo.getCustoTotalEstimado()) + "</b>";
        }

        cancelarEdicao();
        // Atualiza a lista de resultados após salvar
        resultadoBusca = orcamentoRepository.findAll();
        buscaRealizada = true;

        return null;
    }

    /**
     * Busca orçamentos por ID ou nome.
     * Equivale ao GET /api/orcamentos/busca.
     */
    public String buscar() {
        mensagemSucesso = null;
        mensagemErro    = null;
        buscaRealizada  = true;

        if (termoBusca == null || termoBusca.trim().isEmpty()) {
            resultadoBusca = orcamentoRepository.findAll();
            return null;
        }

        String termo = termoBusca.trim();
        try {
            // Tenta interpretar como ID numérico
            Long id = Long.parseLong(termo);
            resultadoBusca = new ArrayList<>();
            orcamentoRepository.findById(id).ifPresent(resultadoBusca::add);
        } catch (NumberFormatException e) {
            // Busca por nome (contendo, case-insensitive)
            resultadoBusca = orcamentoRepository.findByNomeUsuarioContainingIgnoreCase(termo);
        }

        return null;
    }

    /**
     * Prepara o formulário para edição de um orçamento existente.
     * Chamado pelo botão "Editar" na tabela via EL: #{orcamentosBean.prepararEdicao(orc)}.
     *
     * @param orc objeto Orcamento da linha clicada
     */
    public void prepararEdicao(Orcamento orc) {
        this.idEdicao    = orc.getId();
        this.nomeUsuario = orc.getNomeUsuario();
        this.custoTotal  = orc.getCustoTotalEstimado();
        this.modoEdicao  = true;
        this.mensagemSucesso = null;
        this.mensagemErro    = null;
    }

    /**
     * Cancela o modo edição e limpa o formulário.
     */
    public String cancelarEdicao() {
        modoEdicao   = false;
        idEdicao     = null;
        nomeUsuario  = null;
        custoTotal   = null;
        return null;
    }

    /**
     * Exclui um orçamento pelo ID.
     * Equivale ao DELETE /api/orcamentos/{id}.
     *
     * @param id ID do orçamento a excluir
     */
    public void deletar(Long id) {
        orcamentoRepository.deleteById(id);
        // Atualiza a lista após exclusão
        if (termoBusca != null && !termoBusca.trim().isEmpty()) {
            buscar();
        } else {
            resultadoBusca = orcamentoRepository.findAll();
        }
        mensagemSucesso = null;
        mensagemErro    = null;
    }

    // ── Getters e Setters ──────────────────────────────────────────────────

    public String getNomeUsuario() { return nomeUsuario; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

    public Double getCustoTotal() { return custoTotal; }
    public void setCustoTotal(Double custoTotal) { this.custoTotal = custoTotal; }

    public boolean isModoEdicao() { return modoEdicao; }
    public void setModoEdicao(boolean modoEdicao) { this.modoEdicao = modoEdicao; }

    public Long getIdEdicao() { return idEdicao; }
    public void setIdEdicao(Long idEdicao) { this.idEdicao = idEdicao; }

    public String getTermoBusca() { return termoBusca; }
    public void setTermoBusca(String termoBusca) { this.termoBusca = termoBusca; }

    public List<Orcamento> getResultadoBusca() { return resultadoBusca; }

    public boolean isBuscaRealizada() { return buscaRealizada; }

    public String getMensagemSucesso() { return mensagemSucesso; }
    public String getMensagemErro()    { return mensagemErro;    }
}