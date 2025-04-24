package com.aromasdecor.model;

import java.sql.Date;

public class CompraAvalia {
    private int numero;
    private Date data;
    private double valorTotal;
    private String avaliacao;
    private int nota;
    private String cpfCliente;
    private int codigoProduto;
    private int codigoCupom;

    public CompraAvalia(int numero, Date data, double valorTotal, String avaliacao, int nota, String cpfCliente, int codigoProduto, int codigoCupom) {
        this.numero = numero;
        this.data = data;
        this.valorTotal = valorTotal;
        this.avaliacao = avaliacao;
        this.nota = nota;
        this.cpfCliente = cpfCliente;
        this.codigoProduto = codigoProduto;
        this.codigoCupom = codigoCupom;
    }

    public int getNumero() { return numero; }
    public Date getData() { return data; }
    public double getValorTotal() { return valorTotal; }
    public String getAvaliacao() { return avaliacao; }
    public int getNota() { return nota; }
    public String getCpfCliente() { return cpfCliente; }
    public int getCodigoProduto() { return codigoProduto; }
    public int getCodigoCupom() { return codigoCupom; }
}
