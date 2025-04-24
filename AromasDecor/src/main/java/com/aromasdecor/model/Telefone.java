package com.aromasdecor.model;

public class Telefone {
    private String cpfCliente;
    private String telefone;

    public Telefone(String cpfCliente, String telefone) {
        this.cpfCliente = cpfCliente;
        this.telefone = telefone;
    }

    public String getCpfCliente() {return cpfCliente;}
    public String getTelefone() {return telefone;}
}
