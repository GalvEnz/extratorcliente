package com.br.norris.repository;

import com.br.norris.entity.BlingToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlingTokenRepository extends JpaRepository<BlingToken, Long> {
}