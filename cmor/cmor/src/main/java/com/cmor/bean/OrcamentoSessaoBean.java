package com.cmor.bean;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * OrcamentoSessaoBean — bean de sessão que guarda o último custo calculado.
 *
 * @SessionScoped: vive durante toda a sessão do usuário, sobrevivendo à
 * navegação entre páginas (ao contrário do @ViewScoped que morre ao sair da view).
 *
 * Fluxo:
 *   1. ConcretoBean ou TijolosBean chama setUltimoCusto() após calcular.
 *   2. OrcamentosBean lê getUltimoCusto() no @PostConstruct para pré-preencher o campo.
 */
@Named("orcamentoSessaoBean")
@SessionScoped
@Component
public class OrcamentoSessaoBean implements Serializable {

    private Double ultimoCusto;
    private String origemUltimoCusto; // "Concreto" ou "Tijolos"

    public Double getUltimoCusto() { return ultimoCusto; }

    public String getOrigemUltimoCusto() { return origemUltimoCusto; }

    public void setUltimoCusto(Double custo, String origem) {
        this.ultimoCusto = custo;
        this.origemUltimoCusto = origem;
    }
}