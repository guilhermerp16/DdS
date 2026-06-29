package com.cmor.repository;

import com.cmor.model.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {
    // Permite buscar orçamentos pelo nome do usuário (ou parte dele)
    List<Orcamento> findByNomeUsuarioContainingIgnoreCase(String nomeUsuario);
}