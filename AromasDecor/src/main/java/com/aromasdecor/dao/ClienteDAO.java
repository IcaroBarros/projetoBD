package com.aromasdecor.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.aromasdecor.database.Conexao;
import com.aromasdecor.model.Cliente;

public class ClienteDAO {

    public void salvar(Cliente cliente) {
        String sql = "INSERT INTO cliente (cpf, nome, email, numero, senha) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getCpf());
            stmt.setString(2, cliente.getNome());
            stmt.setString(3, cliente.getEmail());
            stmt.setString(4, cliente.getNumero());
            stmt.setString(5, cliente.getSenha());

            stmt.executeUpdate();
            System.out.println("Cliente salvo com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao salvar cliente: " + e.getMessage());
        }
    }

    public boolean verificarLogin(String cpf, String senha) {
        String sql = "SELECT * FROM cliente WHERE cpf = ? AND senha = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            stmt.setString(2, senha);

            return stmt.executeQuery().next();
        } catch (SQLException e) {
            System.out.println("Erro ao verificar login: " + e.getMessage());
            return false;
        }
    }

    public int contarClientes() {
        String sql = "SELECT COUNT(*) FROM cliente";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Erro ao contar clientes: " + e.getMessage());
        }

        return 0;
    }
    
    public List<Cliente> getTodosClientes() {
        String sql = "SELECT * FROM cliente";
        List<Cliente> clientes = new ArrayList<>();
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Cliente cliente = new Cliente(
                    rs.getString("cpf"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("numero"),
                    rs.getString("senha")
                );
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }
    
    public void remover(String cpf) {
        String sqlDeletaTelefones = "DELETE FROM telefone WHERE cpf_cliente = ?";
        String sqlDeletaCompras = "DELETE FROM compra_avalia WHERE cpf_cliente = ?";
        String sqlDeletaIndicacoes = "DELETE FROM indica WHERE cpf_indicador = ? OR cpf_indicado = ?";
        String sqlDeletaCliente = "DELETE FROM cliente WHERE cpf = ?";

        try (Connection conn = Conexao.conectar()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtTelefones = conn.prepareStatement(sqlDeletaTelefones);
                PreparedStatement stmtCompras = conn.prepareStatement(sqlDeletaCompras);
                PreparedStatement stmtIndicacoes = conn.prepareStatement(sqlDeletaIndicacoes);
                PreparedStatement stmtCliente = conn.prepareStatement(sqlDeletaCliente)) {

                // Deleta telefones do cliente
                stmtTelefones.setString(1, cpf);
                stmtTelefones.executeUpdate();

                // Deleta compras do cliente
                stmtCompras.setString(1, cpf);
                stmtCompras.executeUpdate();

                // Deleta indicações onde ele é indicador ou indicado
                stmtIndicacoes.setString(1, cpf);
                stmtIndicacoes.setString(2, cpf);
                stmtIndicacoes.executeUpdate();

                // Deleta o cliente
                stmtCliente.setString(1, cpf);
                stmtCliente.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public Cliente buscarPorCpf(String cpf) {
        String sql = "SELECT * FROM cliente WHERE cpf = ?";
        try (Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cpf);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Cliente(
                    rs.getString("cpf"),
                    rs.getString("nome"),
                    rs.getString("email"),
                    rs.getString("numero"),
                    rs.getString("senha")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
