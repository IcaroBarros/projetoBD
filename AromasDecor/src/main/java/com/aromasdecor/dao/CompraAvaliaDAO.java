package com.aromasdecor.dao;

import com.aromasdecor.database.Conexao;
import com.aromasdecor.model.CompraAvalia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompraAvaliaDAO {

    public void salvar(CompraAvalia compra) {
        String sql = "INSERT INTO compra_avalia (numero, data, valor_total, avaliacao, nota, cpf_cliente, codigo_produto, codigo_cupom) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, compra.getNumero());
            stmt.setDate(2, compra.getData());
            stmt.setDouble(3, compra.getValorTotal());
            stmt.setString(4, compra.getAvaliacao());
            stmt.setInt(5, compra.getNota());
            stmt.setString(6, compra.getCpfCliente());
            stmt.setInt(7, compra.getCodigoProduto());
            stmt.setInt(8, compra.getCodigoCupom());

            stmt.executeUpdate();
            System.out.println("Compra registrada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao registrar compra: " + e.getMessage());
        }
    }

    public int contarCompras() {
        String sql = "SELECT COUNT(*) FROM compra_avalia";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Erro ao contar compras: " + e.getMessage());
        }

        return 0;
    }

    public int contarAvaliacoes() {
        String sql = "SELECT COUNT(*) FROM compra_avalia WHERE avaliacao IS NOT NULL";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.out.println("Erro ao contar avaliações: " + e.getMessage());
        }

        return 0;
    }
    
    public List<CompraAvalia> listarCompras() {
        String sql = "SELECT * FROM compra_avalia";
        List<CompraAvalia> compras = new ArrayList<>();
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CompraAvalia compra = new CompraAvalia(
                        rs.getInt("numero"),
                        rs.getDate("data"),
                        rs.getDouble("valor_total"),
                        rs.getString("avaliacao"),
                        rs.getInt("nota"),
                        rs.getString("cpf_cliente"),
                        rs.getInt("codigo_produto"),
                        rs.getInt("codigo_cupom")
                );
                compras.add(compra);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar compras: " + e.getMessage());
        }
        
        return compras;
    }
    
    public List<CompraAvalia> listarAvaliacoes() {
        String sql = "SELECT * FROM compra_avalia WHERE avaliacao IS NOT NULL";
        List<CompraAvalia> avaliacoes = new ArrayList<>();
        
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                CompraAvalia avaliacao = new CompraAvalia(
                        rs.getInt("numero"),
                        rs.getDate("data"),
                        rs.getDouble("valor_total"),
                        rs.getString("avaliacao"),
                        rs.getInt("nota"),
                        rs.getString("cpf_cliente"),
                        rs.getInt("codigo_produto"),
                        rs.getInt("codigo_cupom")
                );
                avaliacoes.add(avaliacao);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar avaliações: " + e.getMessage());
        }

        return avaliacoes;
    }
    
    public void remover(int numero) {
        String sql = "DELETE FROM compra_avalia WHERE numero = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, numero);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Compra removida com sucesso!");
            } else {
                System.out.println("Nenhuma compra encontrada com o número: " + numero);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao remover compra: " + e.getMessage());
        }
    }
}
