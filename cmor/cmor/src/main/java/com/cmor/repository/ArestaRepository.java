package com.cmor.repository;

import com.cmor.model.Aresta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArestaRepository extends JpaRepository<Aresta, Long> {}
