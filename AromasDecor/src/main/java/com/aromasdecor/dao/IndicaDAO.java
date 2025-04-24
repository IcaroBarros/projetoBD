package com.aromasdecor.dao;

import com.aromasdecor.database.Conexao;
import com.aromasdecor.model.Indica;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class IndicaDAO {

    public void salvar(Indica indicacao) {
        String sql = "INSERT INTO indica (cpf_indicador, cpf_indicado) VALUES (?, ?)";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, indicacao.getCpfIndicador());
            stmt.setString(2, indicacao.getCpfIndicado());

            stmt.executeUpdate();
            System.out.println("Indicação salva com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao salvar indicação: " + e.getMessage());
        }
    }
}
