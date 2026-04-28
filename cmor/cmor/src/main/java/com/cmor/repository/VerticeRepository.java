package com.cmor.repository;

import com.cmor.model.Vertice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerticeRepository extends JpaRepository<Vertice, Long> {}
