package com.aromasdecor.model;

public class Indica {
    private String cpfIndicador;
    private String cpfIndicado;

    public Indica(String cpfIndicador, String cpfIndicado) {
        this.cpfIndicador = cpfIndicador;
        this.cpfIndicado = cpfIndicado;
    }

    public String getCpfIndicador() {return cpfIndicador;}
    public String getCpfIndicado() {return cpfIndicado;}
}
