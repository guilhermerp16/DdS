package com.cmor.controller;

import com.cmor.dto.TijoloRequestDTO;
import com.cmor.dto.TijoloResponseDTO;
import com.cmor.service.ParedeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Etapa 3 — Serviço REST para cálculo de quantidade de tijolos.
 *
 * POST /api/paredes/tijolos
 * Body (JSON):
 * {
 *   "alturaTijolo": 0.057,
 *   "larguraTijolo": 0.09,
 *   "comprimentoTijolo": 0.19,
 *   "percentualPerda": 0.10,
 *   "arestas": [
 *     {
 *       "nome": "a12", "comprimento": 4.0, "altura": 2.8,
 *       "temJanela": true, "alturaJanela": 1.2, "comprimentoJanela": 1.5,
 *       "temPorta": false
 *     }
 *   ]
 * }
 */
@RestController
@RequestMapping("/api/paredes")
public class ParedeController {

    private final ParedeService paredeService;

    public ParedeController(ParedeService paredeService) {
        this.paredeService = paredeService;
    }

    @PostMapping("/tijolos")
    public ResponseEntity<TijoloResponseDTO> calcularTijolos(
            @Valid @RequestBody TijoloRequestDTO request) {

        TijoloResponseDTO response = paredeService.calcularTijolos(request);
        return ResponseEntity.ok(response);
    }
}
