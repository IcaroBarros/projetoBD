package com.aromasdecor.model;

import java.sql.Date;

public class Funcionario {
    private String cpf;
    private String nome;
    private String cargo;
    private Date dataNasc;
    private String telefone;
    private String cep;
    private String bairro;
    private String numero;
    private String rua;
    private String cpfAdministrador;

    public Funcionario(String cpf, String nome, String cargo, Date dataNasc, String telefone, String cep, String bairro, String numero, String rua, String cpfAdministrador) {
        this.cpf = cpf;
        this.nome = nome;
        this.cargo = cargo;
        this.dataNasc = dataNasc;
        this.telefone = telefone;
        this.cep = cep;
        this.bairro = bairro;
        this.numero = numero;
        this.rua = rua;
        this.cpfAdministrador = cpfAdministrador;
    }

    public String getCpf() { return cpf; }
    public String getNome() { return nome; }
    public String getCargo() { return cargo; }
    public Date getDataNasc() { return dataNasc; }
    public String getTelefone() { return telefone; }
    public String getCep() { return cep; }
    public String getBairro() { return bairro; }
    public String getNumero() { return numero; }
    public String getRua() { return rua; }
    public String getCpfAdministrador() { return cpfAdministrador; }
}
