package com.aromasdecor.dao;

import com.aromasdecor.database.Conexao;
import com.aromasdecor.model.Funcionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class FuncionarioDAO {

    public void salvar(Funcionario funcionario) {
        String sql = "INSERT INTO funcionario (cpf, nome, cargo, data_nasc, telefone, cep, bairro, numero, rua, cpf_administrador) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, funcionario.getCpf());
            stmt.setString(2, funcionario.getNome());
            stmt.setString(3, funcionario.getCargo());
            stmt.setDate(4, funcionario.getDataNasc());
            stmt.setString(5, funcionario.getTelefone());
            stmt.setString(6, funcionario.getCep());
            stmt.setString(7, funcionario.getBairro());
            stmt.setString(8, funcionario.getNumero());
            stmt.setString(9, funcionario.getRua());
            stmt.setString(10, funcionario.getCpfAdministrador());

            stmt.executeUpdate();
            System.out.println("Funcionário salvo com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao salvar funcionário: " + e.getMessage());
        }
    }
}
