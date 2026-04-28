package com.cmor.dto;

import java.util.List;
import java.util.Map;

public class ConcreteResponseDTO {

    private Double volumeTotalM3;
    private Double alturaViga;
    private List<Map<String, Object>> detalhesPorAresta;

    public ConcreteResponseDTO() {}

    public ConcreteResponseDTO(Double volumeTotalM3, Double alturaViga,
                               List<Map<String, Object>> detalhesPorAresta) {
        this.volumeTotalM3 = volumeTotalM3;
        this.alturaViga = alturaViga;
        this.detalhesPorAresta = detalhesPorAresta;
    }

    public Double getVolumeTotalM3() { return volumeTotalM3; }
    public void setVolumeTotalM3(Double volumeTotalM3) { this.volumeTotalM3 = volumeTotalM3; }

    public Double getAlturaViga() { return alturaViga; }
    public void setAlturaViga(Double alturaViga) { this.alturaViga = alturaViga; }

    public List<Map<String, Object>> getDetalhesPorAresta() { return detalhesPorAresta; }
    public void setDetalhesPorAresta(List<Map<String, Object>> detalhesPorAresta) {
        this.detalhesPorAresta = detalhesPorAresta;
    }
}
