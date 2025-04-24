package com.aromasdecor.model;

import java.sql.Date;

public class Produto {
    private int codigo;
    private String nome;
    private String descricao;
    private double preco;
    private int estoque;
    private Date dataCadastro;

    public Produto(int codigo, String nome, String descricao, double preco, int estoque, Date dataCadastro) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.estoque = estoque;
        this.dataCadastro = dataCadastro;
    }

    public int getCodigo() {return codigo;}
    public String getNome() {return nome;}
    public String getDescricao() {return descricao;}
    public double getPreco() {return preco;}
    public int getEstoque() {return estoque;}
    public Date getDataCadastro() {return dataCadastro;}
  
}
