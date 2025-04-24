package com.aromasdecor.model;

public class AdministradorAromas {
    private String cpf;
    private String nome;
    private String dataNasc;
    private String telefone;
    private String rua;
    private String bairro;
    private String numero;
    private String cidade;
    private String senha;

    public AdministradorAromas(String cpf, String nome, String dataNasc, String telefone,
                               String rua, String bairro, String numero, String cidade, String senha) {
        this.cpf = cpf;
        this.nome = nome;
        this.dataNasc = dataNasc;
        this.telefone = telefone;
        this.rua = rua;
        this.bairro = bairro;
        this.numero = numero;
        this.cidade = cidade;
        this.senha = senha;
    }

    public String getCpf() { return cpf; }
    public String getNome() { return nome; }
    public String getDataNasc() { return dataNasc; }
    public String getTelefone() { return telefone; }
    public String getRua() { return rua; }
    public String getBairro() { return bairro; }
    public String getNumero() { return numero; }
    public String getCidade() { return cidade; }
    public String getSenha() { return senha; } 
    
}