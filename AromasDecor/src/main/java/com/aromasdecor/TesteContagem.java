package com.aromasdecor;

import com.aromasdecor.dao.ProdutoDAO;
import com.aromasdecor.dao.ClienteDAO;
import com.aromasdecor.dao.CupomDAO;
import com.aromasdecor.dao.CompraAvaliaDAO;

public class TesteContagem {
    public static void main(String[] args) {
        ProdutoDAO produtoDAO = new ProdutoDAO();
        ClienteDAO clienteDAO = new ClienteDAO();
        CupomDAO cupomDAO = new CupomDAO();
        CompraAvaliaDAO compraDAO = new CompraAvaliaDAO();

        System.out.println("Total de produtos: " + produtoDAO.contarProdutos());
        System.out.println("Total de clientes: " + clienteDAO.contarClientes());
        System.out.println("Cupons ativos: " + cupomDAO.contarCuponsAtivos());
        System.out.println("Total de compras: " + compraDAO.contarCompras());
        System.out.println("Total de avaliações: " + compraDAO.contarAvaliacoes());
    }
}
