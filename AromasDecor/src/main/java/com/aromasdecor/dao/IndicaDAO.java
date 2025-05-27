package com.aromasdecor.dao;

import com.aromasdecor.database.Conexao;
import com.aromasdecor.model.Indica;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class IndicaDAO {
    public void salvar(Indica indica) {
        String sql = "INSERT INTO indica (cpf_indicador, cpf_indicado) VALUES (?, ?)";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, indica.getCpfIndicador());
            stmt.setString(2, indica.getCpfIndicado());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Indica> listarPorIndicador(String cpfIndicador) {
        List<Indica> lista = new ArrayList<>();
        String sql = "SELECT * FROM indica WHERE cpf_indicador = ?";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpfIndicador);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(new Indica(
                    rs.getString("cpf_indicador"),
                    rs.getString("cpf_indicado")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean existeIndicacao(String cpfIndicador, String cpfIndicado) {
        String sql = "SELECT 1 FROM indica WHERE cpf_indicador = ? AND cpf_indicado = ?";
        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpfIndicador);
            stmt.setString(2, cpfIndicado);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
