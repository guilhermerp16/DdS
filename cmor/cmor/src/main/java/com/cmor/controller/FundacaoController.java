package com.cmor.controller;

import com.cmor.dto.ConcreteRequestDTO;
import com.cmor.dto.ConcreteResponseDTO;
import com.cmor.service.FundacaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Etapa 2 — Serviço REST para cálculo de volume de concreto na fundação.
 *
 * POST /api/fundacao/concreto
 * Body (JSON):
 * {
 *   "alturaViga": 0.4,
 *   "arestas": [
 *     { "nome": "a12", "espessura": 0.15, "comprimento": 4.0, "altura": 2.8 },
 *     ...
 *   ]
 * }
 */
@RestController
@RequestMapping("/api/fundacao")
public class FundacaoController {

    private final FundacaoService fundacaoService;

    public FundacaoController(FundacaoService fundacaoService) {
        this.fundacaoService = fundacaoService;
    }

    @PostMapping("/concreto")
    public ResponseEntity<ConcreteResponseDTO> calcularConcreto(
            @Valid @RequestBody ConcreteRequestDTO request) {

        ConcreteResponseDTO response = fundacaoService.calcularConcreto(request);
        return ResponseEntity.ok(response);
    }
}
