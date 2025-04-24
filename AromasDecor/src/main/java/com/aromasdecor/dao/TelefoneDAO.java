package com.aromasdecor.dao;

import com.aromasdecor.database.Conexao;
import com.aromasdecor.model.Telefone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TelefoneDAO {

    public void salvar(Telefone telefone) {
        String sql = "INSERT INTO telefone (cpf_cliente, telefone) VALUES (?, ?)";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, telefone.getCpfCliente());
            stmt.setString(2, telefone.getTelefone());

            stmt.executeUpdate();
            System.out.println("Telefone salvo com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao salvar telefone: " + e.getMessage());
        }
    }
}
