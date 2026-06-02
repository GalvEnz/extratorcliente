package com.br.norris.repository;

import com.br.norris.entity.ControleSync;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ControleSyncRepository extends JpaRepository<ControleSync, Long> {
    Optional<ControleSync> findTopByProcessoOrderByIdDesc(String processo);
}
