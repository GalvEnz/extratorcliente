package com.br.norris.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "controle_sincronizacao")

public class ControleSync {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String processo;
    private Integer ultimaPagina;
    private Long ultimoContatoId;
    private LocalDateTime dataExecucao;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getProcesso() { return processo; }
    public void setProcesso(String processo) { this.processo = processo; }
    public Integer getUltimaPagina() { return ultimaPagina; }
    public void setUltimaPagina(Integer ultimaPagina) { this.ultimaPagina = ultimaPagina; }
    public Long getUltimoContatoId() { return ultimoContatoId; }
    public void setUltimoContatoId(Long ultimoContatoId) { this.ultimoContatoId = ultimoContatoId; }
    public LocalDateTime getDataExecucao() { return dataExecucao; }
    public void setDataExecucao(LocalDateTime dataExecucao) { this.dataExecucao = dataExecucao;}
}
