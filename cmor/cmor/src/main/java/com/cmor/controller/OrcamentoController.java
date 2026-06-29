package com.cmor.controller;

import com.cmor.model.Orcamento;
import com.cmor.repository.OrcamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orcamentos")
@CrossOrigin(origins = "*") //
public class OrcamentoController {

    @Autowired
    private OrcamentoRepository orcamentoRepository;

    @PostMapping
    public ResponseEntity<Orcamento> criarOrcamento(@RequestBody Orcamento orcamento) {
        Orcamento salvo = orcamentoRepository.save(orcamento);
        return ResponseEntity.ok(salvo);
    }

    @GetMapping("/busca")
    public ResponseEntity<?> buscarOrcamentos(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String nome) {

        if (id != null) {
            Optional<Orcamento> orcamento = orcamentoRepository.findById(id);
            return orcamento.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        } else if (nome != null && !nome.trim().isEmpty()) {
            List<Orcamento> lista = orcamentoRepository.findByNomeUsuarioContainingIgnoreCase(nome);
            return ResponseEntity.ok(lista);
        }

        return ResponseEntity.ok(orcamentoRepository.findAll());
    }

    //Editar um Orçamento existente
    @PutMapping("/{id}")
    public ResponseEntity<Orcamento> atualizarOrcamento(@PathVariable Long id, @RequestBody Orcamento dadosAtualizados) {
        return orcamentoRepository.findById(id).map(orcamento -> {
            orcamento.setNomeUsuario(dadosAtualizados.getNomeUsuario());
            orcamento.setCustoTotalEstimado(dadosAtualizados.getCustoTotalEstimado());
            Orcamento atualizado = orcamentoRepository.save(orcamento);
            return ResponseEntity.ok(atualizado);
        }).orElse(ResponseEntity.notFound().build());
    }

    //Deletar um Orçamento do banco
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarOrcamento(@PathVariable Long id) {
        return orcamentoRepository.findById(id).map(orcamento -> {
            orcamentoRepository.delete(orcamento);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}