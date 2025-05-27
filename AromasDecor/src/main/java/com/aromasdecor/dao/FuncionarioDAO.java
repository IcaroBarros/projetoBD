package com.aromasdecor.dao;

import com.aromasdecor.database.Conexao;
import com.aromasdecor.model.Funcionario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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

    public void atualizar(Funcionario f) {
        String sql = "UPDATE funcionario SET nome=?, cargo=?, data_nasc=?, telefone=?, cep=?, bairro=?, numero=?, rua=?, cpf_administrador=? WHERE cpf=?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, f.getNome());
            stmt.setString(2, f.getCargo());
            stmt.setDate(3, f.getDataNasc());
            stmt.setString(4, f.getTelefone());
            stmt.setString(5, f.getCep());
            stmt.setString(6, f.getBairro());
            stmt.setString(7, f.getNumero());
            stmt.setString(8, f.getRua());
            stmt.setString(9, f.getCpfAdministrador());
            stmt.setString(10, f.getCpf());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Funcionario buscarPorId(String cpf) {
        String sql = "SELECT * FROM funcionario WHERE cpf = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Funcionario(
                    rs.getString("cpf"),
                    rs.getString("nome"),
                    rs.getString("cargo"),
                    rs.getDate("data_nasc"),
                    rs.getString("telefone"),
                    rs.getString("cep"),
                    rs.getString("bairro"),
                    rs.getString("numero"),
                    rs.getString("rua"),
                    rs.getString("cpf_administrador")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Funcionario> listarTodos() {
        List<Funcionario> lista = new ArrayList<>();
        String sql = "SELECT * FROM funcionario ORDER BY nome";
        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while ( rs.next()) {
                lista.add(new Funcionario(
                    rs.getString("cpf"),
                    rs.getString("nome"),
                    rs.getString("cargo"),
                    rs.getDate("data_nasc"),
                    rs.getString("telefone"),
                    rs.getString("cep"),
                    rs.getString("bairro"),
                    rs.getString("numero"),
                    rs.getString("rua"),
                    rs.getString("cpf_administrador")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public void deletar(String cpf) {
        String sql = "DELETE FROM funcionario WHERE cpf = ?";
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
