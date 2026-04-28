package com.cmor.dto;

import java.util.List;
import java.util.Map;

public class TijoloResponseDTO {

    private Integer quantidadeTotalTijolos;
    private Integer quantidadeSemPerda;
    private Double percentualPerda;
    private List<Map<String, Object>> detalhesPorAresta;

    public TijoloResponseDTO() {}

    public TijoloResponseDTO(Integer quantidadeTotalTijolos, Integer quantidadeSemPerda,
                             Double percentualPerda, List<Map<String, Object>> detalhesPorAresta) {
        this.quantidadeTotalTijolos = quantidadeTotalTijolos;
        this.quantidadeSemPerda = quantidadeSemPerda;
        this.percentualPerda = percentualPerda;
        this.detalhesPorAresta = detalhesPorAresta;
    }

    public Integer getQuantidadeTotalTijolos() { return quantidadeTotalTijolos; }
    public void setQuantidadeTotalTijolos(Integer quantidadeTotalTijolos) { this.quantidadeTotalTijolos = quantidadeTotalTijolos; }

    public Integer getQuantidadeSemPerda() { return quantidadeSemPerda; }
    public void setQuantidadeSemPerda(Integer quantidadeSemPerda) { this.quantidadeSemPerda = quantidadeSemPerda; }

    public Double getPercentualPerda() { return percentualPerda; }
    public void setPercentualPerda(Double percentualPerda) { this.percentualPerda = percentualPerda; }

    public List<Map<String, Object>> getDetalhesPorAresta() { return detalhesPorAresta; }
    public void setDetalhesPorAresta(List<Map<String, Object>> detalhesPorAresta) { this.detalhesPorAresta = detalhesPorAresta; }
}
