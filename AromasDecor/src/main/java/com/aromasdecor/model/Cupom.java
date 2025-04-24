package com.aromasdecor.model;

import java.sql.Date;

public class Cupom {
    private int codigo;
    private Date dataInicio;
    private Date dataVencimento;
    private double valorDesconto;
    private String descricao;
    private String condicoesUso;

    public Cupom(int codigo, Date dataInicio, Date dataVencimento, double valorDesconto, String descricao, String condicoesUso) {
        this.codigo = codigo;
        this.dataInicio = dataInicio;
        this.dataVencimento = dataVencimento;
        this.valorDesconto = valorDesconto;
        this.descricao = descricao;
        this.condicoesUso = condicoesUso;
    }

    public int getCodigo() { return codigo; }
    public Date getDataInicio() { return dataInicio; }
    public Date getDataVencimento() { return dataVencimento; }
    public double getValorDesconto() { return valorDesconto; }
    public String getDescricao() { return descricao; }
    public String getCondicoesUso() { return condicoesUso; }
}
