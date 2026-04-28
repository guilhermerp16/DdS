package com.cmor.service;

import com.cmor.dto.ArestaDTO;
import com.cmor.dto.TijoloRequestDTO;
import com.cmor.dto.TijoloResponseDTO;
import com.cmor.model.Aresta;
import com.cmor.repository.ArestaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Etapa 3 — Cálculo da quantidade de tijolos das paredes.
 *
 * Lógica:
 *  1. Área bruta da parede  = comprimento × altura
 *  2. Área das aberturas    = (janelas + portas)
 *  3. Área líquida          = área bruta − área das aberturas
 *  4. Área de um tijolo     = comprimento_tijolo × altura_tijolo
 *  5. Tijolos por parede    = área líquida / área_tijolo
 *  6. Com perda             = tijolos × (1 + percentual_perda)
 */
@Service
public class ParedeService {

    private final ArestaRepository arestaRepository;

    public ParedeService(ArestaRepository arestaRepository) {
        this.arestaRepository = arestaRepository;
    }

    public TijoloResponseDTO calcularTijolos(TijoloRequestDTO request) {
        int totalSemPerda = 0;
        List<Map<String, Object>> detalhes = new ArrayList<>();

        double areaTijolo = request.getComprimentoTijolo() * request.getAlturaTijolo();

        for (ArestaDTO dto : request.getArestas()) {
            Aresta aresta = toEntity(dto);
            arestaRepository.save(aresta);

            double comprimento = dto.getComprimento() != null ? dto.getComprimento() : 0.0;
            double altura = dto.getAltura() != null ? dto.getAltura() : 0.0;

            double areaBruta = comprimento * altura;

            // Descontar aberturas
            double areaAberturas = 0.0;
            if (Boolean.TRUE.equals(dto.getTemJanela())
                    && dto.getAlturaJanela() != null && dto.getComprimentoJanela() != null) {
                areaAberturas += dto.getAlturaJanela() * dto.getComprimentoJanela();
            }
            if (Boolean.TRUE.equals(dto.getTemPorta())
                    && dto.getAlturaPorta() != null && dto.getComprimentoPorta() != null) {
                areaAberturas += dto.getAlturaPorta() * dto.getComprimentoPorta();
            }

            double areaLiquida = Math.max(0, areaBruta - areaAberturas);
            int tijolosPorParede = (int) Math.ceil(areaLiquida / areaTijolo);
            totalSemPerda += tijolosPorParede;

            Map<String, Object> detalhe = new LinkedHashMap<>();
            detalhe.put("aresta", dto.getNome());
            detalhe.put("comprimento_m", comprimento);
            detalhe.put("altura_m", altura);
            detalhe.put("areaBruta_m2", Math.round(areaBruta * 100.0) / 100.0);
            detalhe.put("areaAberturas_m2", Math.round(areaAberturas * 100.0) / 100.0);
            detalhe.put("areaLiquida_m2", Math.round(areaLiquida * 100.0) / 100.0);
            detalhe.put("tijolosSemPerda", tijolosPorParede);
            detalhes.add(detalhe);
        }

        double perda = request.getPercentualPerda() != null ? request.getPercentualPerda() : 0.10;
        int totalComPerda = (int) Math.ceil(totalSemPerda * (1 + perda));

        return new TijoloResponseDTO(totalComPerda, totalSemPerda, perda * 100, detalhes);
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
