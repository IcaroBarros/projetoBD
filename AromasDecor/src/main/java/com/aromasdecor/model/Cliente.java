package com.aromasdecor.model;

public class Cliente {
    private String cpf;
    private String nome;
    private String email;
    private String numero;
    private String senha;

    public Cliente(String cpf, String nome, String email, String numero, String senha) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.numero = numero;
        this.senha = senha;
    }

    public String getCpf() { return cpf; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getNumero() { return numero; }
    public String getSenha() { return senha; }
}
