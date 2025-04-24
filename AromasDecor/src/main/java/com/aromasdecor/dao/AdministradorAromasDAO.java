package com.aromasdecor.dao;

import com.aromasdecor.database.Conexao;
import com.aromasdecor.model.AdministradorAromas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdministradorAromasDAO {

    public void salvar(AdministradorAromas admin) {
        String sql = "INSERT INTO administrador_aromas (cpf, nome, data_nasc, telefone, rua, bairro, numero, cidade, senha) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, admin.getCpf());
            stmt.setString(2, admin.getNome());
            stmt.setString(3, admin.getDataNasc());
            stmt.setString(4, admin.getTelefone());
            stmt.setString(5, admin.getRua());
            stmt.setString(6, admin.getBairro());
            stmt.setString(7, admin.getNumero());
            stmt.setString(8, admin.getCidade());
            stmt.setString(9, admin.getSenha());

            stmt.executeUpdate();
            System.out.println("Administrador salvo com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao salvar administrador: " + e.getMessage());
        }
    }

    public boolean verificarAdministrador(String cpf, String senha) {
        String sql = "SELECT * FROM administrador_aromas WHERE cpf = ? AND senha = ?";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.setString(2, senha);

            return stmt.executeQuery().next();
        } catch (SQLException e) {
            System.out.println("Erro ao verificar administrador: " + e.getMessage());
            return false;
        }
    }
}