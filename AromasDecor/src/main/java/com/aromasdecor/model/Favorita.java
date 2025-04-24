package com.aromasdecor.model;

public class Favorita {
    private int codigoProduto;
    private String cpfCliente;

    public Favorita(int codigoProduto, String cpfCliente) {
        this.codigoProduto = codigoProduto;
        this.cpfCliente = cpfCliente;
    }

    public int getCodigoProduto() {
        return codigoProduto;
    }

    public String getCpfCliente() {
        return cpfCliente;
    }
}
