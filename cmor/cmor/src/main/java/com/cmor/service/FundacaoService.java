package com.cmor.service;

import com.cmor.dto.ArestaDTO;
import com.cmor.dto.ConcreteRequestDTO;
import com.cmor.dto.ConcreteResponseDTO;
import com.cmor.model.Aresta;
import com.cmor.repository.ArestaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Etapa 2 — Cálculo do volume de concreto para a fundação (viga baldrame).
 *
 * Fórmula por parede:
 *   Volume (m³) = Largura (espessura) × Altura da viga × Comprimento
 *
 * Largura e Comprimento vêm da aresta; Altura é informada pelo usuário.
 */
@Service
public class FundacaoService {

    private final ArestaRepository arestaRepository;

    public FundacaoService(ArestaRepository arestaRepository) {
        this.arestaRepository = arestaRepository;
    }

    public ConcreteResponseDTO calcularConcreto(ConcreteRequestDTO request) {
        double volumeTotal = 0.0;
        List<Map<String, Object>> detalhes = new ArrayList<>();

        for (ArestaDTO dto : request.getArestas()) {
            // Persiste a aresta no banco para manter histórico (ORM)
            Aresta aresta = toEntity(dto);
            arestaRepository.save(aresta);

            // Volume = L × A × C
            double largura = dto.getEspessura() != null ? dto.getEspessura() : 0.0;
            double comprimento = dto.getComprimento() != null ? dto.getComprimento() : 0.0;
            double alturaViga = request.getAlturaViga();

            double volume = largura * alturaViga * comprimento;
            volumeTotal += volume;

            Map<String, Object> detalhe = new LinkedHashMap<>();
            detalhe.put("aresta", dto.getNome());
            detalhe.put("espessura_m", largura);
            detalhe.put("comprimento_m", comprimento);
            detalhe.put("alturaViga_m", alturaViga);
            detalhe.put("volume_m3", Math.round(volume * 1000.0) / 1000.0);
            detalhes.add(detalhe);
        }

        return new ConcreteResponseDTO(
                Math.round(volumeTotal * 1000.0) / 1000.0,
                request.getAlturaViga(),
                detalhes
        );
    }

    private Aresta toEntity(ArestaDTO dto) {
        Aresta a = new Aresta();
        a.setNome(dto.getNome());
        a.setEspessura(dto.getEspessura());
        a.setComprimento(dto.getComprimento());
        a.setAltura(dto.getAltura());
        a.setTemJanela(dto.getTemJanela());
        a.setAlturaJanela(dto.getAlturaJanela());
        a.setComprimentoJanela(dto.getComprimentoJanela());
        a.setTemPorta(dto.getTemPorta());
        a.setAlturaPorta(dto.getAlturaPorta());
        a.setComprimentoPorta(dto.getComprimentoPorta());
        return a;
    }
}
