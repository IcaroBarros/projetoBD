package com.aromasdecor.dao;

import com.aromasdecor.database.Conexao;
import com.aromasdecor.model.Favorita;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FavoritaDAO {

    public void salvar(Favorita favorita) {
        String sql = "INSERT INTO favorita (codigo_produto, cpf_cliente) VALUES (?, ?)";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, favorita.getCodigoProduto());
            stmt.setString(2, favorita.getCpfCliente());

            stmt.executeUpdate();
            System.out.println("Favorito salvo com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao salvar favorito: " + e.getMessage());
        }
    }
}
